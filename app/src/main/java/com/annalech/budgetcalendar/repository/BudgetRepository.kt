package com.annalech.budgetcalendar.repository

import com.annalech.budgetcalendar.data.BudgetDao
import com.annalech.budgetcalendar.data.entiity.Budget
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao
){

    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

    fun getAllBudgetEntries() = budgetDao.getAllData()

    suspend fun updateBudget(amount:Float, purpose: String, id:Int) =
        budgetDao.ubdateBudget(amount,purpose,id)

    suspend fun deleteEntry(budget: Budget) =
        budgetDao.deleteEntry(budget)

    fun getTotalCredit() = budgetDao.getTotalCredit()
    fun getTotalDebitSpending() = budgetDao.getTotalSpending()


    suspend fun getBudgetEntriesBetweenDates(startDate:Long, endDate:Long) =
        budgetDao.getReportsBetweennDates(startDate,endDate)


}