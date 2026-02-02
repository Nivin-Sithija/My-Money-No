package com.wiyadama.expensetracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.backup.BackupManager
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import com.wiyadama.expensetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class CategoryWithStats(
    val category: Category,
    val totalSpent: Int,
    val transactionCount: Int
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    val totalExpenses: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions -> 
            transactions.filter { it.deletedAt == null }.sumOf { it.amountCents }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val transactionCount: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions -> transactions.count { it.deletedAt == null } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentTransactions: StateFlow<List<Transaction>> =
        transactionRepository.getAllTransactions()
            .map { transactions -> 
                transactions
                    .filter { it.deletedAt == null }
                    .sortedByDescending { it.dateTime }
                    .take(10)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoriesWithStats: StateFlow<List<CategoryWithStats>> = combine(
        categoryRepository.getRootCategories(),
        transactionRepository.getAllTransactions()
    ) { categories, transactions ->
        categories.map { category ->
            val categoryTransactions = transactions.filter { 
                it.categoryId == category.id && it.deletedAt == null 
            }
            CategoryWithStats(
                category = category,
                totalSpent = categoryTransactions.sumOf { it.amountCents },
                transactionCount = categoryTransactions.size
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _backupFiles = MutableStateFlow<List<File>>(emptyList())
    val backupFiles: StateFlow<List<File>> = _backupFiles.asStateFlow()
    
    private val _isCreatingBackup = MutableStateFlow(false)
    val isCreatingBackup: StateFlow<Boolean> = _isCreatingBackup.asStateFlow()
    
    private val _isRestoringBackup = MutableStateFlow(false)
    val isRestoringBackup: StateFlow<Boolean> = _isRestoringBackup.asStateFlow()
    
    fun loadBackupFiles() {
        viewModelScope.launch {
            _backupFiles.value = backupManager.listBackups()
        }
    }
    
    fun createBackup(onComplete: (Result<File>) -> Unit) {
        viewModelScope.launch {
            _isCreatingBackup.value = true
            val result = backupManager.createBackup()
            _isCreatingBackup.value = false
            loadBackupFiles()
            onComplete(result)
        }
    }
    
    fun restoreBackup(file: File, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isRestoringBackup.value = true
            val result = backupManager.restoreBackup(file)
            _isRestoringBackup.value = false
            onComplete(result)
        }
    }
}

