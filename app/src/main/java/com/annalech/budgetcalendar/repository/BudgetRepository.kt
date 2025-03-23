package com.annalech.budgetcalendar.repository

import com.annalech.budgetcalendar.data.BudgetDao
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao
){
}