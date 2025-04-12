package com.annalech.budgetcalendar.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annalech.budgetcalendar.data.entiity.Budget
import com.annalech.budgetcalendar.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelBudget @Inject constructor(
    val  budgetRepository: BudgetRepository
) :ViewModel(){

    val allBudgetEntriesLD = budgetRepository.getAllBudgetEntries()
    val totalCredit = budgetRepository.getTotalCredit()
    val  totalDebitSpending  = budgetRepository.getTotalDebitSpending()

    fun insertBudget(budget: Budget) = viewModelScope.launch {
        budgetRepository.insertBudget(budget)
    }

    fun updateBudget(amount:Float, purpose: String, id:Int) = viewModelScope.launch {
        budgetRepository.updateBudget(
            amount,
            purpose,
            id
        )
    }

    fun deleteBudgetEntry(budget: Budget) = viewModelScope.launch {
        budgetRepository.deleteEntry(budget)
    }


}