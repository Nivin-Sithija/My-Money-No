package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import com.wiyadama.expensetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

import kotlinx.coroutines.ExperimentalCoroutinesApi

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CategoryDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categoryId = MutableStateFlow<Long?>(null)
    
    fun setCategoryId(id: Long) {
        _categoryId.value = id
    }
    
    val category: StateFlow<Category?> = _categoryId.flatMapLatest { id ->
        if (id != null) {
            categoryRepository.getCategoryByIdFlow(id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val transactions: StateFlow<List<Transaction>> = _categoryId.flatMapLatest { id ->
        if (id != null) {
            transactionRepository.getTransactionsByCategory(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val totalSpent: StateFlow<Int> = transactions
        .map { txList -> txList.filter { it.deletedAt == null }.sumOf { it.amountCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val transactionCount: StateFlow<Int> = transactions
        .map { txList -> txList.count { it.deletedAt == null } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
