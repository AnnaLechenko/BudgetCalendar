package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentBudgetEntryBinding
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding
import com.annalech.budgetcalendar.databinding.FragmentReportsBinding
import com.annalech.budgetcalendar.presentation.adapter.ReportsAdapter
import com.annalech.budgetcalendar.presentation.viewmodels.ViewModelBudget
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReportsFragment :Fragment(R.layout.fragment_reports){

    private var _binding: FragmentReportsBinding ?= null
    val binding:FragmentReportsBinding
        get() = _binding ?: throw  RuntimeException(" FragmentReportsBinding is null")

    private val viewModelBudget: ViewModelBudget by viewModels()
    private lateinit var adapterReports : ReportsAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()

    }

    private fun initializeRecyclerView() {
adapterReports = ReportsAdapter()
binding.rcvReports.apply {
    layoutManager = LinearLayoutManager(requireContext())
    adapter = adapterReports
}
    }


}