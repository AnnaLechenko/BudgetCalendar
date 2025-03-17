package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding

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
    }


}