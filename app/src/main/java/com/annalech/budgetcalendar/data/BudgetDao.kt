package com.annalech.budgetcalendar.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.annalech.budgetcalendar.data.entiity.Budget

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT * FROM budget_tabl ORDER BY id DESC")
    fun getAllData(): LiveData<List<Budget>>


}