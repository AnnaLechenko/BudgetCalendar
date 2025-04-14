package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.data.entiity.Budget
import com.annalech.budgetcalendar.databinding.FragmentReportsBinding
import com.annalech.budgetcalendar.databinding.UpdateBudgetBottomSheetBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ViewModelBudget
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateBudgetBottomSheetFragment(
    val currentBudgetItem: Budget
) : BottomSheetDialogFragment() {


    private var _binding: UpdateBudgetBottomSheetBinding? = null
    val binding: UpdateBudgetBottomSheetBinding
        get() = _binding ?: throw RuntimeException("  UpdateBudgetBottomSheetBinding is null")

    val viewModelBudget: ViewModelBudget by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.update_budget_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = UpdateBudgetBottomSheetBinding.bind(view)
        binding.updateAmount.setText((currentBudgetItem.amount.toString()))
        binding.updatePerpose.setText(currentBudgetItem.purpose)
        binding.updateBudgetEntry.setOnClickListener {
            val updateAmount = binding.updateAmount.text.toString()
            val updatePur = binding.updatePerpose.text.toString()
            val id = currentBudgetItem.id ?: 0

            viewModelBudget.updateBudget(
                updateAmount.toFloat(),
                updatePur,
                id
            )

            dismiss()

        }


    }
}