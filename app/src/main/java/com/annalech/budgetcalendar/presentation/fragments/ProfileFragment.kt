package com.annalech.budgetcalendar.presentation.fragments

import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.annalech.budgetcalendar.R

import com.annalech.budgetcalendar.databinding.FragmentProfileBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ProfileViewMoodel
import com.annalech.budgetcalendar.utils.InternalStoragePhoto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

    //content provider
    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
        resolveUri ->
            resolveUri?.let {
                uri = it
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
            saveImageToInternalSrorage("profile", bitmap)
    }

    private fun saveImageToInternalSrorage(fileName: String, bitmap: Bitmap): Boolean {
            return try {
                requireContext().openFileOutput("$fileName.jpg", MODE_PRIVATE).use {
                   outputStream->
                    if(bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)){
                        throw IOException("Could not save Bitmap")
                    }
                }
                true
            }catch (e:IOException){
                e.printStackTrace()
                false
            }
    }


    private suspend fun loadImageFromInternalStorage():List<InternalStoragePhoto>{
        return withContext(Dispatchers.IO){
            val files = requireContext().filesDir.listFiles()
            files.filter {
                it.canRead() && it.isFile && it.name.endsWith(".jpg")
            }.map { it->
               val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bitmap)
            }
        }

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
    }


}