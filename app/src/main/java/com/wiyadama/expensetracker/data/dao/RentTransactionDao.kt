package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.RentTransaction
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RentTransactionDao {
    @Query("SELECT * FROM rent_transactions ORDER BY dueDate DESC")
    fun getAllTransactions(): Flow<List<RentTransaction>>
    
    @Query("SELECT * FROM rent_transactions WHERE propertyId = :propertyId ORDER BY dueDate DESC")
    fun getTransactionsByProperty(propertyId: Long): Flow<List<RentTransaction>>
    
    @Query("SELECT * FROM rent_transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): RentTransaction?
    
    @Query("SELECT * FROM rent_transactions WHERE status = :status ORDER BY dueDate DESC")
    fun getTransactionsByStatus(status: RentPaymentStatus): Flow<List<RentTransaction>>
    
    @Query("SELECT * FROM rent_transactions WHERE propertyId = :propertyId AND status = :status ORDER BY dueDate DESC")
    fun getTransactionsByPropertyAndStatus(propertyId: Long, status: RentPaymentStatus): Flow<List<RentTransaction>>
    
    @Query("SELECT * FROM rent_transactions WHERE dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<RentTransaction>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: RentTransaction): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<RentTransaction>)
    
    @Update
    suspend fun update(transaction: RentTransaction)
    
    @Delete
    suspend fun delete(transaction: RentTransaction)
    
    @Query("DELETE FROM rent_transactions WHERE propertyId = :propertyId")
    suspend fun deleteByProperty(propertyId: Long)
    
    @Query("UPDATE rent_transactions SET status = :status, paidAmount = :paidAmount, paidDate = :paidDate, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updatePaymentStatus(id: Long, status: RentPaymentStatus, paidAmount: Int, paidDate: Long?, updatedAt: Long)
}
