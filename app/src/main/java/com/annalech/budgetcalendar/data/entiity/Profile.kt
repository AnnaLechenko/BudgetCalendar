package com.annalech.budgetcalendar.data.entiity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_tabl")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val email: String,
    val profileImageFile: String,
    val bankName: String,
    val currentBalance: Float,
    val initialBalance: Float,
    val primaryBank: Boolean

    )
