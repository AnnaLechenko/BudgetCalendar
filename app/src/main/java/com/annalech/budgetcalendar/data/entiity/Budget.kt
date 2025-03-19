package com.annalech.budgetcalendar.data.entiity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "budget_tabl")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date: String,
    val bankName: String,
    val amount: Float,
    val purpose: String,
    val creditOrDebit: String


)
