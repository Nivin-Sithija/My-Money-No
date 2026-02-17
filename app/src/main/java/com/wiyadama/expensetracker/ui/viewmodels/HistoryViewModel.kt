package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.TransactionRepository
import com.wiyadama.expensetracker.ui.components.TransactionFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _activeFilters = MutableStateFlow(TransactionFilters())
    val activeFilters: StateFlow<TransactionFilters> = _activeFilters.asStateFlow()

    init {
        loadAllTransactions()
    }

    private fun loadAllTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            transactionRepository.getAllTransactions().collect { txList ->
                _transactions.value = applyLocalFilters(txList)
                _isLoading.value = false
            }
        }
    }
    
    fun applyFilters(filters: TransactionFilters) {
        _activeFilters.value = filters
        viewModelScope.launch {
            _isLoading.value = true
            transactionRepository.getAllTransactions().collect { txList ->
                _transactions.value = applyLocalFilters(txList)
                _isLoading.value = false
            }
        }
    }
    
    private fun applyLocalFilters(transactions: List<Transaction>): List<Transaction> {
        val filters = _activeFilters.value
        var filtered = transactions
        
        // Date range filter
        if (filters.startDate != null) {
            filtered = filtered.filter { it.dateTime >= filters.startDate }
        }
        if (filters.endDate != null) {
            // Add 1 day to include the end date fully
            val endOfDay = filters.endDate + (24 * 60 * 60 * 1000) - 1
            filtered = filtered.filter { it.dateTime <= endOfDay }
        }
        
        // Category filter
        if (filters.categoryId != null) {
            filtered = filtered.filter { it.categoryId == filters.categoryId }
        }
        
        // Member filter
        if (filters.memberId != null) {
            filtered = filtered.filter { it.memberId == filters.memberId }
        }
        
        // Shop filter
        if (filters.shopId != null) {
            filtered = filtered.filter { it.shopId == filters.shopId }
        }
        
        return filtered
    }
    
    fun clearFilters() {
        _activeFilters.value = TransactionFilters()
        loadAllTransactions()
    }
    
    fun hasActiveFilters(): Boolean {
        val filters = _activeFilters.value
        return filters.startDate != null || filters.endDate != null || 
               filters.categoryId != null || filters.memberId != null || 
               filters.shopId != null
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val updatedTransaction = transaction.copy(deletedAt = System.currentTimeMillis())
            transactionRepository.updateTransaction(updatedTransaction)
        }
    }

    fun filterByDateRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByDateRange(startDate, endDate).collect { txList ->
                _transactions.value = txList
            }
        }
    }

    fun filterByCategory(categoryId: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByCategory(categoryId).collect { txList ->
                _transactions.value = txList
            }
        }
    }

    fun filterByMember(memberId: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByMember(memberId).collect { txList ->
                _transactions.value = txList
            }
        }
    }

    fun filterByShop(shopId: Long) {
        viewModelScope.launch {
            transactionRepository.getTransactionsByShop(shopId).collect { txList ->
                _transactions.value = txList
            }
        }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.softDeleteTransaction(transactionId)
        }
    }

    fun restoreTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.restoreTransaction(transactionId)
        }
    }
}
