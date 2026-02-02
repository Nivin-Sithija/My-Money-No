package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE deletedAt IS NULL ORDER BY dateTime DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE deletedAt IS NULL ORDER BY dateTime DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int = 10): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?

    @Query("SELECT * FROM transactions WHERE dateTime >= :startDate AND dateTime < :endDate AND deletedAt IS NULL ORDER BY dateTime DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId AND deletedAt IS NULL ORDER BY dateTime DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE memberId = :memberId AND deletedAt IS NULL ORDER BY dateTime DESC")
    fun getTransactionsByMember(memberId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE shopId = :shopId AND deletedAt IS NULL ORDER BY dateTime DESC")
    fun getTransactionsByShop(shopId: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC")
    fun getTrashedTransactions(): Flow<List<Transaction>>

    @Query("SELECT SUM(amountCents) FROM transactions WHERE deletedAt IS NULL")
    suspend fun getTotalExpenses(): Int?

    @Query("SELECT SUM(amountCents) FROM transactions WHERE categoryId = :categoryId AND deletedAt IS NULL")
    suspend fun getTotalExpensesByCategory(categoryId: Long): Int?

    @Query("SELECT COUNT(*) FROM transactions WHERE categoryId = :categoryId AND deletedAt IS NULL")
    suspend fun getTransactionCountByCategory(categoryId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("UPDATE transactions SET deletedAt = :timestamp WHERE id = :id")
    suspend fun softDeleteTransaction(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE transactions SET deletedAt = NULL WHERE id = :id")
    suspend fun restoreTransaction(id: Long)

    @Query("DELETE FROM transactions WHERE deletedAt IS NOT NULL AND deletedAt < :beforeTimestamp")
    suspend fun purgeOldTrashedTransactions(beforeTimestamp: Long)
}
