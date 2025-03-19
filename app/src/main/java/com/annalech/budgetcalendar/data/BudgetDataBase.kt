package com.annalech.budgetcalendar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.annalech.budgetcalendar.data.entiity.Budget
import com.annalech.budgetcalendar.data.entiity.Profile

@Database(
    entities = [Budget::class , Profile::class ],
    version = 1,
    exportSchema = false)
abstract class BudgetDataBase : RoomDatabase() {

    abstract fun getBudgetDao(): BudgetDao

    abstract fun getProfileDao(): ProfileDao

}