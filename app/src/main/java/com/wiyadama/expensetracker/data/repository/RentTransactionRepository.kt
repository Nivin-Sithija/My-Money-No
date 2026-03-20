package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.RentTransactionDao
import com.wiyadama.expensetracker.data.entity.RentTransaction
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RentTransactionRepository @Inject constructor(
    private val rentTransactionDao: RentTransactionDao
) {
    fun getAllTransactions(): Flow<List<RentTransaction>> = 
        rentTransactionDao.getAllTransactions()
    
    fun getTransactionsByProperty(propertyId: Long): Flow<List<RentTransaction>> = 
        rentTransactionDao.getTransactionsByProperty(propertyId)
    
    suspend fun getTransactionById(id: Long): RentTransaction? = 
        rentTransactionDao.getTransactionById(id)
    
    fun getTransactionsByStatus(status: RentPaymentStatus): Flow<List<RentTransaction>> = 
        rentTransactionDao.getTransactionsByStatus(status)
    
    fun getTransactionsByPropertyAndStatus(propertyId: Long, status: RentPaymentStatus): Flow<List<RentTransaction>> = 
        rentTransactionDao.getTransactionsByPropertyAndStatus(propertyId, status)
    
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<RentTransaction>> = 
        rentTransactionDao.getTransactionsByDateRange(startDate, endDate)
    
    suspend fun insert(transaction: RentTransaction): Long = 
        rentTransactionDao.insert(transaction)
    
    suspend fun insertAll(transactions: List<RentTransaction>) = 
        rentTransactionDao.insertAll(transactions)
    
    suspend fun update(transaction: RentTransaction) = 
        rentTransactionDao.update(transaction)
    
    suspend fun delete(transaction: RentTransaction) = 
        rentTransactionDao.delete(transaction)
    
    suspend fun deleteByProperty(propertyId: Long) = 
        rentTransactionDao.deleteByProperty(propertyId)
    
    suspend fun updatePaymentStatus(
        id: Long, 
        status: RentPaymentStatus, 
        paidAmount: Int, 
        paidDate: Long?
    ) {
        rentTransactionDao.updatePaymentStatus(
            id = id,
            status = status,
            paidAmount = paidAmount,
            paidDate = paidDate,
            updatedAt = System.currentTimeMillis()
        )
    }
    
    suspend fun recordPayment(
        transactionId: Long,
        amountPaid: Int,
        paymentDate: Long = System.currentTimeMillis()
    ) {
        val transaction = getTransactionById(transactionId) ?: return
        
        val newPaidAmount = transaction.paidAmount + amountPaid
        val newStatus = when {
            newPaidAmount >= transaction.expectedAmount -> RentPaymentStatus.PAID
            newPaidAmount > 0 -> RentPaymentStatus.PARTIAL
            else -> RentPaymentStatus.UNPAID
        }
        
        updatePaymentStatus(transactionId, newStatus, newPaidAmount, paymentDate)
    }
}
