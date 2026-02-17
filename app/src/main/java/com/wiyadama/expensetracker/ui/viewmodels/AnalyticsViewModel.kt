package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import com.wiyadama.expensetracker.data.repository.TransactionRepository
import com.wiyadama.expensetracker.ui.components.CategorySpending
import com.wiyadama.expensetracker.ui.components.TrendDataPoint
import com.wiyadama.expensetracker.ui.screens.AnalyticsPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(AnalyticsPeriod.DAILY)
    val selectedPeriod: StateFlow<AnalyticsPeriod> = _selectedPeriod

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    val totalSpending: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions ->
            transactions.filter { it.deletedAt == null }
                .sumOf { it.amountCents }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val categoryBreakdown: StateFlow<List<CategorySpending>> = combine(
        transactionRepository.getAllTransactions(),
        categoryRepository.getRootCategories()
    ) { transactions, categories ->
        val activeTransactions = transactions.filter { it.deletedAt == null }
        val total = activeTransactions.sumOf { it.amountCents }.toFloat()
        
        if (total == 0f || categories.isEmpty()) return@combine emptyList()

        val categoryMap = categories.associateBy { it.id }
        val spendingByCategory = activeTransactions
            .filter { it.deletedAt == null }
            .groupBy { it.categoryId }
            .mapNotNull { (categoryId, txns) ->
                val category = categoryMap[categoryId] ?: return@mapNotNull null
                val amount = txns.sumOf { it.amountCents }
                val percentage = (amount / total) * 100f
                
                CategorySpending(
                    categoryName = category.name,
                    amount = amount,
                    color = androidx.compose.ui.graphics.Color(category.color),
                    percentage = percentage
                )
            }
            .sortedByDescending { it.amount }
            .take(8)

        spendingByCategory
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendData: StateFlow<List<TrendDataPoint>> = combine(
        _selectedPeriod,
        transactionRepository.getAllTransactions()
    ) { period, transactions ->
        val activeTransactions = transactions.filter { it.deletedAt == null }
        val calendar = Calendar.getInstance()
        val dateFormat = when (period) {
            AnalyticsPeriod.DAILY -> SimpleDateFormat("EEE", Locale.getDefault())
            AnalyticsPeriod.WEEKLY -> SimpleDateFormat("'W'w", Locale.getDefault())
            AnalyticsPeriod.MONTHLY -> SimpleDateFormat("MMM", Locale.getDefault())
        }

        val dataPoints = when (period) {
            AnalyticsPeriod.DAILY -> {
                // Last 7 days
                (0..6).map { daysAgo ->
                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
                    val startOfDay = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis
                    val endOfDay = calendar.apply {
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                    }.timeInMillis

                    val amount = activeTransactions
                        .filter { it.dateTime in startOfDay..endOfDay }
                        .sumOf { it.amountCents }

                    TrendDataPoint(
                        label = dateFormat.format(Date(startOfDay)),
                        amount = amount
                    )
                }.reversed()
            }
            AnalyticsPeriod.WEEKLY -> {
                // Last 4 weeks
                (0..3).map { weeksAgo ->
                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.add(Calendar.WEEK_OF_YEAR, -weeksAgo)
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                    val startOfWeek = calendar.timeInMillis
                    calendar.add(Calendar.DAY_OF_WEEK, 6)
                    val endOfWeek = calendar.timeInMillis

                    val amount = activeTransactions
                        .filter { it.dateTime in startOfWeek..endOfWeek }
                        .sumOf { it.amountCents }

                    TrendDataPoint(
                        label = dateFormat.format(Date(startOfWeek)),
                        amount = amount
                    )
                }.reversed()
            }
            AnalyticsPeriod.MONTHLY -> {
                // Last 6 months
                (0..5).map { monthsAgo ->
                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.add(Calendar.MONTH, -monthsAgo)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    val startOfMonth = calendar.timeInMillis
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    val endOfMonth = calendar.timeInMillis

                    val amount = activeTransactions
                        .filter { it.dateTime in startOfMonth..endOfMonth }
                        .sumOf { it.amountCents }

                    TrendDataPoint(
                        label = dateFormat.format(Date(startOfMonth)),
                        amount = amount
                    )
                }.reversed()
            }
        }

        dataPoints
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Period comparison data
    val thisMonthSpending: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startOfMonth = calendar.timeInMillis
            
            transactions
                .filter { it.deletedAt == null && it.dateTime >= startOfMonth }
                .sumOf { it.amountCents }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val lastMonthSpending: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions ->
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startOfLastMonth = calendar.timeInMillis
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            val endOfLastMonth = calendar.timeInMillis
            
            transactions
                .filter { it.deletedAt == null && it.dateTime in startOfLastMonth..endOfLastMonth }
                .sumOf { it.amountCents }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val averageMonthlySpending: StateFlow<Int> = transactionRepository.getAllTransactions()
        .map { transactions ->
            val activeTransactions = transactions.filter { it.deletedAt == null }
            if (activeTransactions.isEmpty()) return@map 0
            
            val oldestTransaction = activeTransactions.minByOrNull { it.dateTime }?.dateTime ?: return@map 0
            val now = System.currentTimeMillis()
            val monthsDiff = ((now - oldestTransaction) / (30L * 24 * 60 * 60 * 1000)).toInt() + 1
            
            if (monthsDiff == 0) return@map 0
            
            activeTransactions.sumOf { it.amountCents } / monthsDiff
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Insights
    data class Insight(
        val title: String,
        val subtitle: String,
        val value: String
    )

    val insights: StateFlow<List<Insight>> = combine(
        transactionRepository.getAllTransactions(),
        categoryRepository.getRootCategories()
    ) { transactions, categories ->
        val activeTransactions = transactions.filter { it.deletedAt == null }
        if (activeTransactions.isEmpty() || categories.isEmpty()) return@combine emptyList()

        val insightsList = mutableListOf<Insight>()
        
        // Highest Spend Category
        val categoryMap = categories.associateBy { it.id }
        val highestSpendCategory = activeTransactions
            .groupBy { it.categoryId }
            .map { (categoryId, txns) ->
                categoryMap[categoryId] to txns.sumOf { it.amountCents }
            }
            .maxByOrNull { it.second }
        
        if (highestSpendCategory != null && highestSpendCategory.first != null) {
            insightsList.add(
                Insight(
                    title = "Highest Spend",
                    subtitle = highestSpendCategory.first!!.name,
                    value = "LKR ${highestSpendCategory.second / 100}.${(highestSpendCategory.second % 100).toString().padStart(2, '0')}"
                )
            )
        }
        
        // Most Frequent Category
        val mostFrequentCategory = activeTransactions
            .groupBy { it.categoryId }
            .map { (categoryId, txns) ->
                categoryMap[categoryId] to txns.size
            }
            .maxByOrNull { it.second }
        
        if (mostFrequentCategory != null && mostFrequentCategory.first != null) {
            insightsList.add(
                Insight(
                    title = "Most Frequent",
                    subtitle = mostFrequentCategory.first!!.name,
                    value = "${mostFrequentCategory.second} transactions"
                )
            )
        }
        
        // Average Transaction
        val avgTransaction = activeTransactions.sumOf { it.amountCents } / activeTransactions.size
        insightsList.add(
            Insight(
                title = "Avg Transaction",
                subtitle = "Per expense",
                value = "LKR ${avgTransaction / 100}.${(avgTransaction % 100).toString().padStart(2, '0')}"
            )
        )
        
        insightsList
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            categoryRepository.getRootCategories().collect { cats ->
                _categories.value = cats
            }
        }
    }

    fun setPeriod(period: AnalyticsPeriod) {
        _selectedPeriod.value = period
    }
}
