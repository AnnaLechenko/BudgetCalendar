package com.annalech.budgetcalendar.presentation.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.data.entiity.Profile

import com.annalech.budgetcalendar.databinding.FragmentProfileBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ProfileViewMoodel
import com.annalech.budgetcalendar.utils.Constants
import com.annalech.budgetcalendar.utils.Constants.PREFERENCE_NAME
import com.annalech.budgetcalendar.utils.Constants.PREFERENCE_PROFILE_EXISTANCE_KEY
import com.annalech.budgetcalendar.utils.InternalStoragePhoto
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException(" FragmentProfileBinding is null")

    private val viewModel: ProfileViewMoodel by viewModels()
    private lateinit var uri: Uri
    private lateinit var bitmap: Bitmap
    private lateinit var myPref:SharedPreferences


    //content provider
    //обработчик выбора изображений
    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) { resolveUri ->
            resolveUri?.let {
                uri = it
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
            saveImageToInternalSrorage("profile", bitmap)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //подключение настроек приложения
        myPref = requireContext().getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)

        if (myPref.contains(PREFERENCE_PROFILE_EXISTANCE_KEY)){
            chahgeViewVisibilityPostRegistration()
        }else{
            chahgeViewVisibilityForRegistration()
        }

        binding.profileImage.setOnClickListener { it ->
            takePhoto.launch("image/*")
        }

        viewModel.receivedProfileLiveData.observe(viewLifecycleOwner){listProfile->
            listProfile?.let { list->
                if (list.isNotEmpty()){
                    viewLifecycleOwner.lifecycleScope.launch {

                        val listOfImage = loadImageFromInternalStorage()
                        for (image in listOfImage){
                            if (image.nameImage.contains("profile")){
                                Glide.with(requireContext())
                                    .load(image.bitmap)
                                    .circleCrop()
                                    .into(binding.profileImage)
                            }
                        }



                        binding.inputBankName.setText(list[0].bankName)
                        binding.inputInitialBalance.setText(list[0].initialBalance.toString())
                        binding.inputCurrentBalance.setText(list[0].currentBalance.toString())
                        binding.profileName.setText(list[0].name)
                        binding.profileEmail.setText(list[0].email)
                        binding.materialCheckBox.isChecked = list[0].primaryBank

                        chahgeViewVisibilityPostRegistration()
                    }
                }else{
                    Toast.makeText(
                        requireContext(),
                         "Complete Profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }

        //при нажатии кнопки сохранение данных профиля
        binding.submitProfile.setOnClickListener {
            submitData(
                binding.profileName.text.toString(),
                binding.profileEmail.text.toString(),
                binding.inputBankName.text.toString(),
                binding.inputInitialBalance.text.toString(),
                binding.materialCheckBox.isChecked,
            )
        }

        binding.updateCurrentBalance.setOnClickListener {
            submitData(
                binding.profileName.text.toString(),
                binding.profileEmail.text.toString(),
                binding.inputBankName.text.toString(),
               binding.inputCurrentBalance.text.toString(),
                binding.materialCheckBox.isChecked,
            )
        }
    }








    private fun submitData(profileName: String,
                           profileEmail: String,
                           bankName: String,
                           initialBal: String,
                           checked: Boolean) {

        // Проверяем, инициализирована ли переменная uri
        if (!::uri.isInitialized) {
            // Можно показать Toast, чтобы уведомить пользователя, или обработать это как-то иначе
            Toast.makeText(requireContext(), "Пожалуйста, выберите изображение профиля", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.insertProfileData(
            Profile(
                name = profileName,
               email = profileEmail,
                profileImageFile = uri.toString(),
                bankName = bankName,
                initialBalance = initialBal.toFloat(),
                currentBalance = initialBal.toFloat(),
                primaryBank = checked
            )
        )

        //изменение настроек приложения
        val editor = myPref.edit()
        editor.putBoolean(PREFERENCE_PROFILE_EXISTANCE_KEY, true)
        editor.apply()

        //переход на след.фрагмент
        findNavController().navigate(R.id.action_profileFragment_to_calendarViewFragment)

    }


    //сохранение изображение в локальное хранилище в нужном формате
    private fun saveImageToInternalSrorage(fileName: String, bitmap: Bitmap): Boolean {
        return try {
            requireContext().openFileOutput("$fileName.jpg", MODE_PRIVATE).use { outputStream ->
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                    throw IOException("Could not save Bitmap")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    //загрузка изображения из локального хранилища
    private suspend fun loadImageFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = requireContext().filesDir.listFiles()
            files.filter {
                it.canRead() && it.isFile && it.name.endsWith(".jpg")
            }.map { it ->
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bitmap)
            }
        }

    }

    private fun chahgeViewVisibilityForRegistration() {
        binding.submitProfile.visibility = View.VISIBLE
        binding.updateCurrentBalance.visibility = View.GONE
        binding.balanceLayout.visibility = View.GONE
        binding.updateCurrentBalance.visibility = View.GONE
    }

    private fun chahgeViewVisibilityPostRegistration() {

        binding.updateCurrentBalance.visibility = View.VISIBLE
        binding.balanceLayout.visibility = View.VISIBLE

        binding.updateCurrentBalance.visibility = View.VISIBLE
        binding.updateCurrentBalance.isEnabled = true
        binding.submitProfile.visibility = View.GONE

  //      binding.inputBankName.visibility = View.GONE
        binding.inputInitialBalance.visibility = View.GONE


    }


}