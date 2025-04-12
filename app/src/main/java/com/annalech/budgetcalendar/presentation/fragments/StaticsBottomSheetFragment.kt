package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentReportsBinding
import com.annalech.budgetcalendar.databinding.StaticsBottomSheetBinding
import com.annalech.budgetcalendar.presentation.viewmodels.ViewModelBudget
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaticsBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: StaticsBottomSheetBinding? = null
    val binding: StaticsBottomSheetBinding
        get() = _binding ?: throw RuntimeException(" StaticsBottomSheetBinding")

    private val viewModelBudget:ViewModelBudget by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  inflater.inflate(R.layout.statics_bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = StaticsBottomSheetBinding.bind(view)

        viewModelBudget.totalCredit.observe(viewLifecycleOwner){
            binding.totalCredit.text  = it.toString()
        }
        viewModelBudget.totalDebitSpending.observe(viewLifecycleOwner){
            binding.totalSpending.text = (-1 * it).toString()
        }


    }




}