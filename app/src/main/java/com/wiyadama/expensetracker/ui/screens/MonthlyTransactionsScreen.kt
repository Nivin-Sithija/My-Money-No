package com.wiyadama.expensetracker.ui.screens

import androidx.activity.compose.BackHandler
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
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyTransactionsScreen(
    transactions: List<Transaction>,
    categories: List<Category>,
    members: List<Member>,
    shops: List<Shop>,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    
    // Group transactions by month
    val transactionsByMonth = transactions
        .sortedByDescending { it.dateTime }
        .groupBy { transaction ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = transaction.dateTime
            "${calendar.get(Calendar.YEAR)}-${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}"
        }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            "Monthly Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${transactions.size} total transactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Slate900,
                    navigationIconContentColor = Slate900
                )
            )
        },
        containerColor = Slate50
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            transactionsByMonth.forEach { (monthKey, monthTransactions) ->
                item {
                    MonthSection(
                        monthKey = monthKey,
                        transactions = monthTransactions,
                        categories = categories,
                        members = members,
                        shops = shops
                    )
                }
            }
        }
    }
}

@Composable
fun MonthSection(
    monthKey: String,
    transactions: List<Transaction>,
    categories: List<Category>,
    members: List<Member>,
    shops: List<Shop>
) {
    val monthTotal = transactions.sumOf { it.amountCents }
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = transactions.first().dateTime
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(transactions.first().dateTime))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Month Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = monthName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "${transactions.size} transaction${if (transactions.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(monthTotal, "LKR"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Red600,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Slate100)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Transaction List
            transactions.sortedByDescending { it.dateTime }.forEach { transaction ->
                val category = categories.find { it.id == transaction.categoryId }
                val member = members.find { it.id == transaction.memberId }
                val shop = shops.find { it.id == transaction.shopId }
                
                TransactionItem(
                    transaction = transaction,
                    category = category,
                    member = member,
                    shop = shop
                )
                
                if (transaction != transactions.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    category: Category?,
    member: Member?,
    shop: Shop?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(category?.let { Color(it.color).copy(alpha = 0.1f) } ?: Slate100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = category?.let { Color(it.color) } ?: Slate400,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Transaction Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category?.name ?: "Unknown",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                member?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
                shop?.let {
                    Text(
                        text = "• ${it.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
            Text(
                text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(transaction.dateTime)),
                style = MaterialTheme.typography.bodySmall,
                color = Slate400,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        
        // Amount
        Text(
            text = CurrencyFormatter.formatWithSymbol(transaction.amountCents, "LKR"),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Red600
        )
    }
}
