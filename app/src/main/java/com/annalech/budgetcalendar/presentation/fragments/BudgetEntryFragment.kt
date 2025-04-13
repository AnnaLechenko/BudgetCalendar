package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.data.entiity.Budget
import com.annalech.budgetcalendar.databinding.FragmentBudgetEntryBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ProfileViewMoodel
import com.annalech.budgetcalendar.presentation.viewmodels.ViewModelBudget
import com.annalech.budgetcalendar.utils.UtilityFunctions.dateStringToMillis
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BudgetEntryFragment : Fragment(R.layout.fragment_budget_entry) {

    private var _binding: FragmentBudgetEntryBinding? = null
    val binding: FragmentBudgetEntryBinding
        get() = _binding ?: throw RuntimeException(" FragmentBudgetEntryBinding is null")

    //аргуемент переданный из навигации календаря
    val args:BudgetEntryFragmentArgs by navArgs()
    private val viewMoodelProfile: ProfileViewMoodel by viewModels()
    private var currentBalance: Float = 0.0f
    private lateinit var bankName: String
    private lateinit var debitOrCredid: String
    private lateinit var remainingBalance: String
    private val viewModelBudget: ViewModelBudget by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Введите изменения счёта ${args.selectDate}"


        //взаимодействие с списком банков
        getProfileDate()
        setSpinnerForDebitOrCredit()
        binding.bankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bankName = parent?.getItemIdAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bankName = "NoBank"
            }
        }

        binding.debitCreditSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    debitOrCredid = parent?.getItemIdAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    debitOrCredid = "Доход"
                }
            }

        //вывод оставшейся суммы
        binding.editAmount.addTextChangedListener { it ->
            it.let {
                val enterAmount = it.toString()
                if (enterAmount.isNotEmpty()) {
                    val changeBalance =  when(debitOrCredid){
                        "Расход" ->{ (currentBalance - enterAmount.toFloat())}
                        else->{ (currentBalance +enterAmount.toFloat())}

                    }

                    remainingBalance = changeBalance.toString()
                    binding.remainingBalance.text = remainingBalance
                }
                else {
                    remainingBalance = currentBalance.toString()
                    binding.remainingBalance.text = remainingBalance
                }
            }
        }


        binding.submitBudgetEntry.setOnClickListener { it ->
            val amount = binding.editAmount.text.toString()
            val purpose = binding.editPurpose.text.toString()
            val currentDate = args.selectDate?: "01/01/2025"
            val date = dateStringToMillis(currentDate).toString()
            val revisedCurrentBalannce = remainingBalance

            submitBudgetEntryToDB(
                bankName,
                debitOrCredid,
                amount,
                purpose,
                date,
                revisedCurrentBalannce
            )
        }


    }

    private fun submitBudgetEntryToDB(
        bankName: String,
        debitOrCredid: String,
        amount: String,
        purpose: String,
        date: String,
        revisedCurrentBalannce: String
    ) {
        var amoountToInsert = amount.toFloatOrNull() ?: 0f
        if (debitOrCredid.equals("Расход")) {
            amoountToInsert = -amoountToInsert
        }
        val creditOrDebit = debitOrCredid

        viewModelBudget.insertBudget(
            Budget(
                date = date,
                bankName = bankName,
                amount = amoountToInsert,
                purpose = purpose,
                creditOrDebit = creditOrDebit
            )
        )

        viewMoodelProfile.updateCurrentBudget(revisedBalance = revisedCurrentBalannce.toFloat())
        Snackbar.make(binding.budgetEntryConstraint,"Сохранено", Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_budgetEntryFragment_to_calendarViewFragment)

    }


    private fun setSpinnerForDebitOrCredit() {
        val listDebitOrCredit = ArrayList<String>()
        listDebitOrCredit.add("Доход")
        listDebitOrCredit.add("Расход")

        val adapterSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listDebitOrCredit)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.debitCreditSpinner.adapter = adapterSpinner
    }

    private fun getProfileDate() {
        viewMoodelProfile.receivedProfileLiveData.observe(viewLifecycleOwner) { it ->
            val bankNames = ArrayList<String>()
            bankNames.add(it[0].bankName)
            currentBalance = it[0].currentBalance
            binding.remainingBalance.text = it[0].currentBalance.toString()

            //добавление в сспиннер инфы
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bankNames)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.bankSpinner.adapter = arrayAdapter

        }

    }


}