package com.annalech.budgetcalendar.repository

import com.annalech.budgetcalendar.data.ProfileDao
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    val profileDao: ProfileDao
){
}