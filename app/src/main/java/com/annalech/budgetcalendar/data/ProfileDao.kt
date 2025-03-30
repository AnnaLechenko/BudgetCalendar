package com.annalech.budgetcalendar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annalech.budgetcalendar.data.entiity.Profile

@Dao
interface ProfileDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertProfileData(profile: Profile)

     @Query("SELECT * FROM profile_tabl ORDER BY id DESC")
     fun getProfileData(): LiveData<List<Profile>>

     @Query("UPDATE profile_tabl SET  currentBalance = :revisedBalanceFloat")
     suspend fun updateCurrentBalance(revisedBalanceFloat: Float)



}