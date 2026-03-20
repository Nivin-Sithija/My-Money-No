package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter

data class CategorySpending(
    val categoryName: String,
    val amount: Int,
    val color: Color,
    val percentage: Float
)

data class TrendDataPoint(
    val label: String,
    val amount: Int
)

@Composable
fun CategoryPieChart(
    data: List<CategorySpending>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = Slate400,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "No spending data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate400
                )
            }
        }
        return
    }

    // Create a simple visual pie chart representation
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Visual pie representation using Canvas
            Box(
                modifier = Modifier
                    .size(160.dp)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    var startAngle = -90f
                    data.forEach { category ->
                        val sweepAngle = (category.percentage / 100f) * 360f
                        if (sweepAngle > 0f) {
                            drawArc(
                                color = category.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true
                            )
                            startAngle += sweepAngle
                        }
                    }
                }
                
                // Center circle
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = "${data.size}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Slate900,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "categories",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Legend
            data.take(5).forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(category.color)
                        )
                        Text(
                            text = category.categoryName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "${category.percentage.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate600,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@Composable
fun SpendingTrendChart(
    data: List<TrendDataPoint>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate400
            )
        }
        return
    }

    val chartEntryModel = remember(data) {
        ChartEntryModelProducer(
            data.mapIndexed { index, point ->
                FloatEntry(
                    x = index.toFloat(),
                    y = (point.amount / 100f) // Convert cents to rupees
                )
            }
        )
    }

    ProvideChartStyle {
        val model = chartEntryModel.getModel()
        if (model != null) {
            Chart(
                chart = lineChart(),
                model = model,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
fun CategoryBreakdownChart(
    data: List<CategorySpending>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No spending data",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate400
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Simplified pie chart representation using rows
        data.forEach { category ->
            CategorySpendingRow(
                categoryName = category.categoryName,
                amount = category.amount,
                color = category.color,
                percentage = category.percentage
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CategorySpendingRow(
    categoryName: String,
    amount: Int,
    color: Color,
    percentage: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                    .clip(CircleShape)
                    .background(color)
            )
            Column {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Slate900
                )
                Text(
                    text = "${percentage.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )
            }
        }
        Text(
            text = CurrencyFormatter.formatWithSymbol(amount, "LKR"),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Slate900
        )
    }
}

@Composable
fun MonthlyComparisonChart(
    data: List<TrendDataPoint>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate400
            )
        }
        return
    }

    val chartEntryModel = remember(data) {
        ChartEntryModelProducer(
            data.mapIndexed { index, point ->
                FloatEntry(
                    x = index.toFloat(),
                    y = (point.amount / 100f)
                )
            }
        )
    }

    ProvideChartStyle {
        val model = chartEntryModel.getModel()
        if (model != null) {
            Chart(
                chart = columnChart(),
                model = model,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}
