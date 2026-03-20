package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.IncomeDao
import com.wiyadama.expensetracker.data.entity.Income
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao
) {
    fun getAllIncomes(): Flow<List<Income>> = incomeDao.getAllIncomes()

    fun getIncomesByCategory(categoryType: String): Flow<List<Income>> =
        incomeDao.getIncomesByCategory(categoryType)

    fun getIncomesByProperty(propertyId: Long): Flow<List<Income>> =
        incomeDao.getIncomesByProperty(propertyId)

    suspend fun getIncomeById(id: Long): Income? = incomeDao.getIncomeById(id)

    suspend fun insertIncome(income: Income): Long = incomeDao.insertIncome(income)

    suspend fun updateIncome(income: Income) = incomeDao.updateIncome(income)

    suspend fun deleteIncome(income: Income) = incomeDao.deleteIncome(income)

    fun getTotalByCategory(categoryType: String): Flow<Int?> =
        incomeDao.getTotalByCategory(categoryType)
}
