package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.wiyadama.expensetracker.ui.components.CategoryBreakdownChart
import com.wiyadama.expensetracker.ui.components.MonthlyComparisonChart
import com.wiyadama.expensetracker.ui.components.SpendingTrendChart
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel
import com.wiyadama.expensetracker.util.CurrencyFormatter
import com.wiyadama.expensetracker.util.DateUtils

enum class AnalyticsPeriod {
    DAILY, WEEKLY, MONTHLY
}

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val totalSpending by viewModel.totalSpending.collectAsState()
    val categoryBreakdown by viewModel.categoryBreakdown.collectAsState()
    val trendData by viewModel.trendData.collectAsState()
    val thisMonthSpending by viewModel.thisMonthSpending.collectAsState()
    val lastMonthSpending by viewModel.lastMonthSpending.collectAsState()
    val averageMonthlySpending by viewModel.averageMonthlySpending.collectAsState()
    val insights by viewModel.insights.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
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
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Track your spending patterns",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Period Selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PeriodButton(
                        text = "Daily",
                        isSelected = selectedPeriod == AnalyticsPeriod.DAILY,
                        onClick = { viewModel.setPeriod(AnalyticsPeriod.DAILY) },
                        modifier = Modifier.weight(1f)
                    )
                    PeriodButton(
                        text = "Weekly",
                        isSelected = selectedPeriod == AnalyticsPeriod.WEEKLY,
                        onClick = { viewModel.setPeriod(AnalyticsPeriod.WEEKLY) },
                        modifier = Modifier.weight(1f)
                    )
                    PeriodButton(
                        text = "Monthly",
                        isSelected = selectedPeriod == AnalyticsPeriod.MONTHLY,
                        onClick = { viewModel.setPeriod(AnalyticsPeriod.MONTHLY) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Spending Trend Chart
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Spending Trend",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            )
                            Text(
                                text = when (selectedPeriod) {
                                    AnalyticsPeriod.DAILY -> "Last 7 days"
                                    AnalyticsPeriod.WEEKLY -> "Last 4 weeks"
                                    AnalyticsPeriod.MONTHLY -> "Last 6 months"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Indigo50
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = Indigo600,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "+12%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Indigo600,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Spending Trend Chart
                    SpendingTrendChart(
                        data = trendData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Top Categories
        item {
            Text(
                text = "Top Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
            )
        }

        // Category Distribution
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Category Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Pie Chart
                    CategoryPieChart(
                        data = categoryBreakdown,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Category breakdown
                    CategoryBreakdownChart(
                        data = categoryBreakdown,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Comparison
        item {
            Text(
                text = "Period Comparison",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val thisMonthTrend = if (lastMonthSpending > 0) {
                        ((thisMonthSpending - lastMonthSpending).toFloat() / lastMonthSpending * 100).toInt()
                    } else 0
                    
                    val avgTrend = if (averageMonthlySpending > 0) {
                        ((thisMonthSpending - averageMonthlySpending).toFloat() / averageMonthlySpending * 100).toInt()
                    } else 0
                    
                    val lastMonthTrend = if (averageMonthlySpending > 0) {
                        ((lastMonthSpending - averageMonthlySpending).toFloat() / averageMonthlySpending * 100).toInt()
                    } else 0
                    
                    ComparisonItem(
                        label = "This Month",
                        amount = thisMonthSpending,
                        trend = thisMonthTrend,
                        trendUp = thisMonthTrend > 0
                    )
                    ComparisonItem(
                        label = "Last Month",
                        amount = lastMonthSpending,
                        trend = lastMonthTrend,
                        trendUp = lastMonthTrend > 0
                    )
                    ComparisonItem(
                        label = "Average",
                        amount = averageMonthlySpending,
                        trend = avgTrend,
                        trendUp = avgTrend > 0
                    )
                }
            }
        }

        // Insights
        item {
            Text(
                text = "Insights",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(insights.size) { index ->
                    val insight = insights[index]
                    InsightCard(
                        title = insight.title,
                        subtitle = insight.subtitle,
                        value = insight.value,
                        icon = when (index) {
                            0 -> Icons.Default.TrendingUp
                            1 -> Icons.Default.Repeat
                            else -> Icons.Default.Warning
                        },
                        gradientColors = when (index) {
                            0 -> listOf(Orange500, Amber500)
                            1 -> listOf(Blue500, Teal500)
                            else -> listOf(Red500, Pink500)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Indigo600 else Slate100,
            contentColor = if (isSelected) Color.White else Slate700
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
fun CategoryBreakdownItem(
    name: String,
    percentage: Int,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
                    .size(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = Slate700
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress bar
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Slate100)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage / 100f)
                        .background(color)
                )
            }
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.width(40.dp)
            )
        }
    }
}

@Composable
fun ComparisonItem(
    label: String,
    amount: Int,
    trend: Int,
    trendUp: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Slate500
            )
            Text(
                text = CurrencyFormatter.formatWithSymbol(amount, "LKR"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (trendUp) Emerald50 else Red50
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (trendUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (trendUp) Emerald600 else Red600,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${if (trendUp) "+" else ""}$trend%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (trendUp) Emerald600 else Red600,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun InsightCard(
    title: String,
    subtitle: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>
) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradientColors))
                .padding(20.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
