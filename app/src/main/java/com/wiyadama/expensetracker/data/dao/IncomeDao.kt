package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Query("SELECT * FROM incomes ORDER BY dateTime DESC")
    fun getAllIncomes(): Flow<List<Income>>

    @Query("SELECT * FROM incomes WHERE categoryType = :categoryType ORDER BY dateTime DESC")
    fun getIncomesByCategory(categoryType: String): Flow<List<Income>>

    @Query("SELECT * FROM incomes WHERE propertyId = :propertyId ORDER BY dateTime DESC")
    fun getIncomesByProperty(propertyId: Long): Flow<List<Income>>

    @Query("SELECT * FROM incomes WHERE id = :id")
    suspend fun getIncomeById(id: Long): Income?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income): Long

    @Update
    suspend fun updateIncome(income: Income)

    @Delete
    suspend fun deleteIncome(income: Income)

    @Query("SELECT SUM(amountCents) FROM incomes WHERE categoryType = :categoryType")
    fun getTotalByCategory(categoryType: String): Flow<Int?>
}
