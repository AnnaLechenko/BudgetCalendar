package com.annalech.budgetcalendar.data
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
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

    @Query("UPDATE budget_tabl SET amount =:amount, purpose = :purpose WHERE id=:id")
    suspend fun ubdateBudget(amount:Float, purpose:String, id:Int)

    @Delete
    suspend fun deleteEntry(budget: Budget)

    @Query("SELECT  SUM(amount)  FROM budget_tabl WHERE creditOrDebit = '1'")
    fun getTotalCredit():LiveData<Float>

    @Query("SELECT  SUM(amount)  FROM budget_tabl WHERE creditOrDebit = '0'")
    fun getTotalSpending():LiveData<Float>


}