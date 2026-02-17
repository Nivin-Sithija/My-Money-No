package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.TransactionDao
import com.wiyadama.expensetracker.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getRecentTransactions(limit: Int = 10): Flow<List<Transaction>> =
        transactionDao.getRecentTransactions(limit)

    suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)

    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(categoryId)

    fun getTransactionsByMember(memberId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)

    fun getTransactionsByShop(shopId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByShop(shopId)

    fun getTrashedTransactions(): Flow<List<Transaction>> =
        transactionDao.getTrashedTransactions()

    suspend fun getTotalExpenses(): Int = transactionDao.getTotalExpenses() ?: 0

    suspend fun getTotalExpensesByCategory(categoryId: Long): Int =
        transactionDao.getTotalExpensesByCategory(categoryId) ?: 0

    suspend fun getTransactionCountByCategory(categoryId: Long): Int =
        transactionDao.getTransactionCountByCategory(categoryId)

    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    suspend fun softDeleteTransaction(id: Long) =
        transactionDao.softDeleteTransaction(id)

    suspend fun restoreTransaction(id: Long) =
        transactionDao.restoreTransaction(id)

    suspend fun purgeOldTrashedTransactions(beforeTimestamp: Long) =
        transactionDao.purgeOldTrashedTransactions(beforeTimestamp)
}
