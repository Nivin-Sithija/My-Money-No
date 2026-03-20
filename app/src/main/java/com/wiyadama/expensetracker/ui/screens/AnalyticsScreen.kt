package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wiyadama.expensetracker.ui.components.CategoryPieChart
import com.wiyadama.expensetracker.ui.components.CategoryBreakdownChart
import com.wiyadama.expensetracker.ui.components.SpendingTrendChart
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.util.getCategoryData
import com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel
import com.wiyadama.expensetracker.util.CurrencyFormatter

enum class AnalyticsPeriod {
    DAILY, WEEKLY, MONTHLY
}

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val totalSpending by viewModel.totalSpending.collectAsState()
    val categoryBreakdown by viewModel.categoryBreakdown.collectAsState()
    val trendData by viewModel.trendData.collectAsState()
    val thisMonthSpending by viewModel.thisMonthSpending.collectAsState()
    val lastMonthSpending by viewModel.lastMonthSpending.collectAsState()
    val averageMonthlySpending by viewModel.averageMonthlySpending.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header with Total Spending
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Indigo600, Purple600)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total Spending",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(totalSpending, "LKR"),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Quick Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickStatCard(
                    title = "This Month",
                    value = CurrencyFormatter.formatWithSymbol(thisMonthSpending, "LKR"),
                    icon = Icons.Default.CalendarMonth,
                    color = Emerald500,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Avg/Month",
                    value = CurrencyFormatter.formatWithSymbol(averageMonthlySpending, "LKR"),
                    icon = Icons.Default.TrendingUp,
                    color = Indigo500,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Spending Trend Chart
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Spending Trend",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    SpendingTrendChart(
                        data = trendData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }
        }

        // Smart Insights
        item {
            Text(
                text = "Smart Insights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
        }
        
        // Top Spending Categories
        items(categoryBreakdown.take(3)) { categoryData ->
            val percentage = if (totalSpending > 0) (categoryData.amount.toFloat() / totalSpending * 100).toInt() else 0
            val catColor = getCategoryData(categoryData.categoryName).iconColor
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(catColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val catData = getCategoryData(categoryData.categoryName)
                            Icon(
                                imageVector = catData.icon,
                                contentDescription = null,
                                tint = catColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = categoryData.categoryName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate900,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = CurrencyFormatter.formatWithSymbol(categoryData.amount, "LKR"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }
                    }
                    
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = catColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = catColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Slate500,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Slate900,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategorySpendingItem(
    categoryName: String,
    amount: Int,
    percentage: Int,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    val catData = getCategoryData(categoryName)
                    Icon(
                        imageVector = catData.icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate900,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(amount, "LKR"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
