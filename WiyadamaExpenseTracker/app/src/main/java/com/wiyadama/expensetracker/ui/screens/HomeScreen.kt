package com.wiyadama.expensetracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.ui.components.CategoryCard
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.util.getCategoryData
import com.wiyadama.expensetracker.util.CurrencyFormatter
import com.wiyadama.expensetracker.util.DateUtils

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCategoryClick: (Long) -> Unit = {}
) {
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val transactionCount by viewModel.transactionCount.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    val categoriesWithStats by viewModel.categoriesWithStats.collectAsState()
    val backupFiles by viewModel.backupFiles.collectAsState()
    val isCreatingBackup by viewModel.isCreatingBackup.collectAsState()
    val isRestoringBackup by viewModel.isRestoringBackup.collectAsState()
    
    var showBackupDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.loadBackupFiles()
    }
    
    if (showBackupDialog) {
        com.wiyadama.expensetracker.ui.components.BackupDialog(
            backupFiles = backupFiles,
            onDismiss = { showBackupDialog = false },
            onCreateBackup = {
                viewModel.createBackup { result ->
                    // Show success/error message if needed
                }
            },
            onRestoreBackup = { file ->
                viewModel.restoreBackup(file) { result ->
                    showBackupDialog = false
                }
            },
            isCreatingBackup = isCreatingBackup,
            isRestoringBackup = isRestoringBackup
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Expense Tracker",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
                IconButton(
                    onClick = { showBackupDialog = true },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Indigo100)
                ) {
                    Icon(
                        imageVector = Icons.Default.Backup,
                        contentDescription = "Backup & Restore",
                        tint = Indigo600
                    )
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Expenses Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Indigo500, Purple500)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Total Expenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(totalExpenses, "LKR"),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "This month",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Transactions Count Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
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
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "February 2026",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        // Categories Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900
                )
                TextButton(onClick = { /* TODO: Navigate to all categories */ }) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Indigo500,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Indigo500,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Category Cards Grid
        items(categoriesWithStats.chunked(2)) { statsPair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                statsPair.forEach { stats ->
                    CategoryCardItem(
                        category = stats.category,
                        totalExpense = stats.totalSpent,
                        transactionCount = stats.transactionCount,
                        modifier = Modifier.weight(1f),
                        onClick = { onCategoryClick(stats.category.id) }
                    )
                }
                // Fill empty space if odd number
                if (statsPair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Recent Expenses Section
        item {
            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
            )
        }

        items(recentTransactions) { transaction ->
            val category = categoriesWithStats.find { it.category.id == transaction.categoryId }?.category
            RecentTransactionItem(
                merchantName = transaction.merchantName ?: "Expense",
                categoryName = category?.name ?: "Unknown",
                notes = transaction.notes,
                amount = transaction.amountCents,
                date = transaction.dateTime
            )
        }
    }
}

@Composable
fun CategoryCardItem(
    category: Category,
    totalExpense: Int,
    transactionCount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Map category to icon and colors
    val (icon, gradientColors, bgColors, iconColor) = when (category.name) {
        "Food & Dining" -> CategoryData(
            Icons.Default.Restaurant,
            CategoryColors.FoodDining,
            BackgroundColors.FoodDiningBg,
            Orange400
        )
        "Transport" -> CategoryData(
            Icons.Default.DirectionsCar,
            CategoryColors.Transport,
            BackgroundColors.TransportBg,
            Blue400
        )
        "Shopping" -> CategoryData(
            Icons.Default.ShoppingBag,
            CategoryColors.Shopping,
            BackgroundColors.ShoppingBg,
            Purple400
        )
        "Bills & Utilities" -> CategoryData(
            Icons.Default.Bolt,
            CategoryColors.Bills,
            BackgroundColors.BillsBg,
            Yellow500
        )
        "Entertainment" -> CategoryData(
            Icons.Default.MusicNote,
            CategoryColors.Entertainment,
            BackgroundColors.EntertainmentBg,
            Pink400
        )
        "Travel" -> CategoryData(
            Icons.Default.Flight,
            CategoryColors.Travel,
            BackgroundColors.TravelBg,
            Teal400
        )
        "Grocery" -> CategoryData(
            Icons.Default.ShoppingCart,
            CategoryColors.FoodDining,
            BackgroundColors.FoodDiningBg,
            Emerald400
        )
        "Telephone Bills" -> CategoryData(
            Icons.Default.Phone,
            CategoryColors.Bills,
            BackgroundColors.BillsBg,
            Indigo400
        )
        else -> CategoryData(
            Icons.Default.Category,
            listOf(Slate400, Slate500),
            listOf(Slate50, Slate100),
            Slate600
        )
    }

    CategoryCard(
        name = category.name,
        icon = icon,
        totalExpense = totalExpense,
        transactions = transactionCount,
        gradientColors = gradientColors,
        bgGradientColors = bgColors,
        iconColor = iconColor,
        onClick = onClick,
        modifier = modifier
    )
}

data class CategoryData(
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val bgColors: List<Color>,
    val iconColor: Color
)

@Composable
fun RecentTransactionItem(
    merchantName: String,
    categoryName: String,
    notes: String?,
    amount: Int,
    date: Long
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Orange400, Amber400)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = merchantName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Slate900
                    )
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Indigo500
                    )
                    if (notes != null && notes.isNotBlank()) {
                        Text(
                            text = notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = DateUtils.formatDate(date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
            Text(
                text = CurrencyFormatter.formatWithSymbol(amount, "LKR"),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
        }
    }
}
