package com.annalech.budgetcalendar.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.annalech.budgetcalendar.R
import com.annalech.budgetcalendar.databinding.FragmentCalendarViewBinding
import com.annalech.budgetcalendar.databinding.FragmentProfileBinding

class ProfileFragment :Fragment(R.layout.fragment_profile){

    private var _binding: FragmentProfileBinding ?= null
    val binding: FragmentProfileBinding
        get() = _binding ?: throw  RuntimeException(" FragmentProfileBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}