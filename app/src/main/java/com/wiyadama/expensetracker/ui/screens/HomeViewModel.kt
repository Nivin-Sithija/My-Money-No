package com.wiyadama.expensetracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.backup.BackupManager
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import com.wiyadama.expensetracker.data.repository.MemberRepository
import com.wiyadama.expensetracker.data.repository.ShopRepository
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
    private val memberRepository: MemberRepository,
    private val shopRepository: ShopRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    private val currentMonthStart = com.wiyadama.expensetracker.util.DateUtils.getStartOfMonth(System.currentTimeMillis())
    
    private val allTransactions = transactionRepository.getAllTransactions()
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), replay = 1)
    
    private val allCategories = categoryRepository.getAllCategories()
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), replay = 1)

    val totalExpenses: StateFlow<Int> = combine(
        allTransactions,
        allCategories
    ) { transactions, categories ->
        val bankCardCategory = categories.find { it.name == "Bank Card Payments" }
        val bankCardSubcategoryIds = categories.filter { it.parentId == bankCardCategory?.id }.map { it.id }
        val excludedCategoryIds = setOfNotNull(bankCardCategory?.id) + bankCardSubcategoryIds
        
        transactions.filter { 
            it.deletedAt == null && 
            it.dateTime >= currentMonthStart &&
            it.categoryId !in excludedCategoryIds
        }.sumOf { it.amountCents }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val transactionCount: StateFlow<Int> = combine(
        allTransactions,
        allCategories
    ) { transactions, categories ->
        val bankCardCategory = categories.find { it.name == "Bank Card Payments" }
        val bankCardSubcategoryIds = categories.filter { it.parentId == bankCardCategory?.id }.map { it.id }
        val excludedCategoryIds = setOfNotNull(bankCardCategory?.id) + bankCardSubcategoryIds
        
        transactions.count { 
            it.deletedAt == null && 
            it.dateTime >= currentMonthStart &&
            it.categoryId !in excludedCategoryIds
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recentTransactions: StateFlow<List<Transaction>> =
        allTransactions
            .map { transactions -> 
                transactions
                    .filter { it.deletedAt == null && it.dateTime >= currentMonthStart }
                    .sortedByDescending { it.dateTime }
                    .take(10)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val members: StateFlow<List<Member>> = memberRepository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shops: StateFlow<List<Shop>> = shopRepository.getAllShops()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategoriesList: StateFlow<List<Category>> = allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoriesWithStats: StateFlow<List<CategoryWithStats>> = combine(
        allCategories,
        allTransactions
    ) { allCategories, transactions ->
        // Only show main categories (parentId == null)
        val mainCategories = allCategories.filter { it.parentId == null }
        val subcategoriesMap = allCategories.filter { it.parentId != null }.groupBy { it.parentId }
        
        mainCategories.map { mainCategory ->
            // Get transactions for main category
            val mainCategoryTransactions = transactions.filter { 
                it.categoryId == mainCategory.id && it.deletedAt == null && it.dateTime >= currentMonthStart
            }
            
            // Get transactions for all subcategories
            val subcategoryIds = subcategoriesMap[mainCategory.id]?.map { it.id } ?: emptyList()
            val subcategoryTransactions = transactions.filter { 
                it.categoryId in subcategoryIds && it.deletedAt == null && it.dateTime >= currentMonthStart
            }
            
            // Combine main category and subcategory transactions
            val allCategoryTransactions = mainCategoryTransactions + subcategoryTransactions
            
            CategoryWithStats(
                category = mainCategory,
                totalSpent = allCategoryTransactions.sumOf { it.amountCents },
                transactionCount = allCategoryTransactions.size
            )
        }.filter { it.transactionCount > 0 }.sortedByDescending { it.transactionCount }
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

