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
            combine(
                categoryRepository.getAllCategories(),
                transactionRepository.getAllTransactions()
            ) { allCategories, allTx ->
                val relevantIds = setOf(id) + allCategories.filter { it.parentId == id }.map { it.id }
                allTx.filter { it.categoryId in relevantIds }.sortedByDescending { it.dateTime }
            }
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

    private val currentMonthStart = com.wiyadama.expensetracker.util.DateUtils.getStartOfMonth(System.currentTimeMillis())
    // Getting proper start of previous month
    private val previousMonthStart = com.wiyadama.expensetracker.util.DateUtils.getStartOfMonth(currentMonthStart - 86400000L) // 1 day before current month start 

    val currentMonthTotal: StateFlow<Int> = transactions
        .map { txList -> txList.filter { it.deletedAt == null && it.dateTime >= currentMonthStart }.sumOf { it.amountCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val previousMonthTotal: StateFlow<Int> = transactions
        .map { txList -> txList.filter { it.deletedAt == null && it.dateTime >= previousMonthStart && it.dateTime < currentMonthStart }.sumOf { it.amountCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
        
    val subcategoriesWithStats: StateFlow<List<Pair<Category, Int>>> = _categoryId.flatMapLatest { id ->
        if (id != null) {
            combine(
                categoryRepository.getAllCategories(),
                transactionRepository.getAllTransactions()
            ) { allCategories, allTx ->
                val subcats = allCategories.filter { it.parentId == id }
                subcats.map { sub ->
                    val txs = allTx.filter { it.categoryId == sub.id && it.deletedAt == null }
                    val total = txs.sumOf { it.amountCents }
                    Pair(sub, total)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
