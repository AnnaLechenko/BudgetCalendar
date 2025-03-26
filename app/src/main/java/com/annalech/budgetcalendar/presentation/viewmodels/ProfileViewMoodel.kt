package com.annalech.budgetcalendar.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annalech.budgetcalendar.data.entiity.Profile
import com.annalech.budgetcalendar.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewMoodel @Inject constructor(
    val profileRepository: ProfileRepository
):ViewModel(){


    val receivedProfileLiveData= profileRepository.getProfile()

   fun insertProfileData(profile: Profile) = viewModelScope.launch {
       profileRepository.insertProfileData(profile)
   }

}