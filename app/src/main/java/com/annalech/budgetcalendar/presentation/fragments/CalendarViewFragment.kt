package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarViewFragment :Fragment(R.layout.fragment_calendar_view){

    private var _binding: FragmentCalendarViewBinding ?= null
    val binding: FragmentCalendarViewBinding
        get() = _binding ?: throw  RuntimeException(" FragmentCalendarViewBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarViewBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Enter Your Budget"
        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month+1}/${year}"
            val action =  CalendarViewFragmentDirections
                .actionCalendarViewFragmentToBudgetEntryFragment(selectedDate)
            findNavController().navigate(action)
        }
    }




}