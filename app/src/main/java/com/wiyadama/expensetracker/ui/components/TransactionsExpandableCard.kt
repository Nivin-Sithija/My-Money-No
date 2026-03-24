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
import com.wiyadama.expensetracker.ui.screens.CategoryWithStats
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.util.getCategoryData
import com.wiyadama.expensetracker.util.CurrencyFormatter

@Composable
fun TransactionsExpandableCard(
    transactionCount: Int,
    categoriesWithStats: List<CategoryWithStats>,
    recentTransactions: List<com.wiyadama.expensetracker.data.entity.Transaction>,
    allCategories: List<Category>,
    members: List<Member>,
    shops: List<Shop>
) {
    var expanded by remember { mutableStateOf(false) }
    
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
                        text = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
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
                
                // Top Categories
                Text(
                    text = "Top Categories",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                categoriesWithStats.take(3).forEach { categoryWithStats ->
                    val categoryData = getCategoryData(categoryWithStats.category.name)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
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
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(categoryData.iconColor.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryData.icon,
                                    contentDescription = null,
                                    tint = categoryData.iconColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = categoryWithStats.category.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate700
                            )
                        }
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(categoryWithStats.totalSpent, "LKR"),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        )
                    }
                }
                
                if (categoriesWithStats.isEmpty()) {
                    Text(
                        text = "No transactions yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
