package com.annalech.budgetcalendar.repository

import com.annalech.budgetcalendar.data.ProfileDao
import com.annalech.budgetcalendar.data.entiity.Profile
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    val profileDao: ProfileDao
){

    fun getProfile()  = profileDao.getProfileData()

    suspend fun insertProfileData(profile: Profile) = profileDao.insertProfileData(profile)

    suspend fun updateCurrentBalance(revisedBalance: Float) = profileDao.updateCurrentBalance(revisedBalance)


}