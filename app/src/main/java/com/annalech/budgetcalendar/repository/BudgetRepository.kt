package com.annalech.budgetcalendar.repository

import com.annalech.budgetcalendar.data.BudgetDao
import com.annalech.budgetcalendar.data.entiity.Budget
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao
){

    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

    fun getAllBudgetEntries() = budgetDao.getAllData()

}