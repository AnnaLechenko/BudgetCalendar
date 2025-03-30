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
        activity?.title = "Enter budget for: ${args.selectDate}"


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
                    debitOrCredid = "Debit"
                }
            }

        //вывод оставшейся суммы
        binding.editAmount.addTextChangedListener { it ->
            it?.let {
                val enterAmount = it.toString()
                val amount = if (debitOrCredid.equals("Debit")) {
                    (currentBalance - enterAmount.toFloat())
                } else {
                    (currentBalance + enterAmount.toFloat())
                }
                binding.remainingBalance.text = amount.toString()
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
        var amoountToInsert = amount.toFloat()
        if (debitOrCredid.equals("Debit")) {
            amoountToInsert = -1 * amoountToInsert
        }

        viewModelBudget.insertBudget(
            Budget(
                date = date,
                bankName = bankName,
                amount = amoountToInsert,
                purpose = purpose,
                creditOrDebit = debitOrCredid
            )
        )

        viewMoodelProfile.updateCurrentBudget(revisedBalance = revisedCurrentBalannce.toFloat())
        Snackbar.make(binding.budgetEntryConstraint,"Entry added", Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_budgetEntryFragment_to_calendarViewFragment)

    }


    private fun setSpinnerForDebitOrCredit() {
        val listDebitOrCredit = ArrayList<String>()
        listDebitOrCredit.add("Debit")
        listDebitOrCredit.add("Credit")
        val adapterSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listDebitOrCredit)
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
            binding.bankSpinner.adapter = arrayAdapter

        }

    }


}