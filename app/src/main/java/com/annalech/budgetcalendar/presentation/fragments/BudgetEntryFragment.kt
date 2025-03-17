package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentBudgetEntryBinding
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding

class BudgetEntryFragment :Fragment(R.layout.fragment_budget_entry){

    private var _binding: FragmentBudgetEntryBinding ?= null
    val binding:FragmentBudgetEntryBinding
        get() = _binding ?: throw  RuntimeException(" FragmentBudgetEntryBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetEntryBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}