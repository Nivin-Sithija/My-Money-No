package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpandableTransactionsCard(
    transactionCount: Int,
    recentTransactions: List<com.wiyadama.expensetracker.data.entity.Transaction>,
    allCategories: List<Category>,
    members: List<Member>,
    shops: List<Shop>
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Group transactions by month
    val transactionsByMonth = recentTransactions
        .sortedByDescending { it.dateTime }
        .groupBy { transaction ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = transaction.dateTime
            "${calendar.get(Calendar.YEAR)}-${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}"
        }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Teal500, Emerald500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                    Text(
                        text = transactionCount.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Slate900,
                        maxLines = 1
                    )
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Slate400,
                    modifier = Modifier.size(20.dp).padding(top = 8.dp)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Slate100)
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Monthly Breakdown",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (transactionsByMonth.isEmpty()) {
                    Text(
                        text = "No transactions yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    transactionsByMonth.entries.take(6).forEach { (monthKey, monthTransactions) ->
                        val monthTotal = monthTransactions.sumOf { it.amountCents }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                                        .format(Date(monthTransactions.first().dateTime)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate900
                                )
                                Text(
                                    text = "${monthTransactions.size} transaction${if (monthTransactions.size != 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            
                            Text(
                                text = CurrencyFormatter.formatWithSymbol(monthTotal, "LKR"),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            )
                        }
                    }
                }
            }
        }
    }
}
