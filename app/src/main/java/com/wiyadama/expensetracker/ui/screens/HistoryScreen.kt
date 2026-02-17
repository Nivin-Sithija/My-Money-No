package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.wiyadama.expensetracker.ui.util.getCategoryData
import com.wiyadama.expensetracker.util.CurrencyFormatter
import com.wiyadama.expensetracker.util.DateUtils

@Composable
fun HistoryScreen(
    transactions: List<Transaction>,
    categories: List<Category>,
    members: List<Member>,
    shops: List<Shop>,
    onTransactionClick: (Transaction) -> Unit,
    onFilterClick: () -> Unit,
    onEditTransaction: (Transaction) -> Unit = {},
    onDeleteTransaction: (Transaction) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredTransactions = remember(transactions, searchQuery) {
        if (searchQuery.isBlank()) {
            transactions
        } else {
            transactions.filter { tx ->
                val categoryName = categories.find { it.id == tx.categoryId }?.name ?: ""
                val shopName = shops.find { it.id == tx.shopId }?.name ?: ""
                val memberName = members.find { it.id == tx.memberId }?.name ?: ""
                
                (tx.merchantName?.contains(searchQuery, ignoreCase = true) == true) ||
                (tx.notes?.contains(searchQuery, ignoreCase = true) == true) ||
                (categoryName.contains(searchQuery, ignoreCase = true)) ||
                (shopName.contains(searchQuery, ignoreCase = true)) ||
                (memberName.contains(searchQuery, ignoreCase = true))
            }
        }
    }
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
                            colors = listOf(Teal600, Emerald600)
                        )
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "All your transactions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        
        // Search Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search transactions...") },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Search, 
                            contentDescription = "Search",
                            tint = Slate400
                        ) 
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Teal500,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(1.dp, Slate200, RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Slate900
                    )
                }
            }
        }

        // Transactions List
        item {
            Text(
                text = "All Transactions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
            )
        }

        items(filteredTransactions, key = { it.id }) { transaction ->
            val category = categories.find { it.id == transaction.categoryId }
            val member = members.find { it.id == transaction.memberId }
            val shop = shops.find { it.id == transaction.shopId }
            HistoryTransactionItemWithActions(
                transaction = transaction,
                category = category,
                member = member,
                shop = shop,
                onClick = { onTransactionClick(transaction) },
                onEdit = { onEditTransaction(transaction) },
                onDelete = { onDeleteTransaction(transaction) }
            )
        }

        if (transactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No transactions yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = Slate500
                        )
                        Text(
                            text = "Start adding your expenses to see them here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate400
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryTransactionItemWithActions(
    transaction: Transaction,
    category: Category?,
    member: Member?,
    shop: Shop?,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val catData = getCategoryData(category?.name ?: "")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.deletedAt != null) Red50.copy(alpha = 0.3f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                                if (transaction.deletedAt != null) {
                                    Brush.linearGradient(colors = listOf(Red400, Red500))
                                } else {
                                    Brush.linearGradient(colors = catData.bgColors)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (transaction.deletedAt != null) Icons.Default.Delete else catData.icon,
                            contentDescription = null,
                            tint = if (transaction.deletedAt != null) Color.White else catData.iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = category?.name ?: "Unknown",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        )
                        // Show description + member/shop subtitle
                        val subtitleParts = mutableListOf<String>()
                        val description = transaction.merchantName ?: transaction.notes
                        if (!description.isNullOrBlank()) subtitleParts.add(description)
                        if (member != null) subtitleParts.add(member.name)
                        if (shop != null) subtitleParts.add(shop.name)
                        
                        Text(
                            text = subtitleParts.joinToString(" \u2022 "),
                            style = MaterialTheme.typography.bodySmall,
                            color = Indigo500,
                            maxLines = 1
                        )
                        Text(
                            text = DateUtils.formatDate(transaction.dateTime),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400
                        )
                        if (transaction.deletedAt != null) {
                            Text(
                                text = "Deleted on ${DateUtils.formatDateTime(transaction.deletedAt)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Red500,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "-${CurrencyFormatter.formatWithSymbol(transaction.amountCents, "LKR")}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (transaction.deletedAt != null) Red900 else Slate900
                        )
                    }
                    
                    if (transaction.deletedAt == null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = onEdit,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Indigo600,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Red600,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Expanded full details
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Slate200)
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(
                    modifier = Modifier.padding(start = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Amount
                    DetailRow(
                        icon = Icons.Default.CreditCard,
                        label = "Amount",
                        value = CurrencyFormatter.formatWithSymbol(transaction.amountCents, "LKR")
                    )
                    
                    // Category
                    DetailRow(
                        icon = Icons.Default.Category,
                        label = "Category",
                        value = category?.name ?: "Unknown"
                    )
                    
                    // Member
                    if (member != null) {
                        DetailRow(
                            icon = Icons.Default.Person,
                            label = "Member",
                            value = member.name
                        )
                    }
                    
                    // Shop
                    if (shop != null) {
                        DetailRow(
                            icon = Icons.Default.Store,
                            label = "Shop",
                            value = shop.name
                        )
                    }
                    
                    // Payment Method
                    DetailRow(
                        icon = Icons.Default.Payment,
                        label = "Payment",
                        value = transaction.paymentMethod
                    )
                    
                    // Date & Time
                    DetailRow(
                        icon = Icons.Default.Schedule,
                        label = "Date & Time",
                        value = DateUtils.formatDateTime(transaction.dateTime)
                    )
                    
                    // Notes
                    if (transaction.notes != null && transaction.notes.isNotBlank()) {
                        DetailRow(
                            icon = Icons.Default.Description,
                            label = "Notes",
                            value = transaction.notes
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Slate400,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = Slate700,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HistoryTransactionItem(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.deletedAt != null) Red50 else Color.White
        ),
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (transaction.deletedAt != null)
                                Brush.linearGradient(listOf(Red400, Pink400))
                            else
                                Brush.linearGradient(listOf(Teal500, Emerald500))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (transaction.deletedAt != null) Icons.Default.Delete else Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = transaction.notes?.ifBlank { "Expense" } ?: "Expense",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (transaction.deletedAt != null) Red900 else Slate900
                    )
                    Text(
                        text = DateUtils.formatDateTime(transaction.dateTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (transaction.deletedAt != null) Red600 else Slate400
                    )
                    if (transaction.deletedAt != null) {
                        Text(
                            text = "Deleted on ${DateUtils.formatDateTime(transaction.deletedAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Red500,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "-${CurrencyFormatter.formatWithSymbol(transaction.amountCents, "LKR")}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (transaction.deletedAt != null) Red900 else Slate900
                )
                if (transaction.deletedAt != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Red100
                    ) {
                        Text(
                            text = "Deleted",
                            style = MaterialTheme.typography.bodySmall,
                            color = Red700,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (startDate: Long?, endDate: Long?, categoryId: Long?, memberId: Long?, shopId: Long?) -> Unit
) {
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var selectedCategory by remember { mutableStateOf<Long?>(null) }
    var selectedMember by remember { mutableStateOf<Long?>(null) }
    var selectedShop by remember { mutableStateOf<Long?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = Slate50)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Teal600, Emerald600)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter Transactions",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Date Range",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Date range picker will be implemented",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate500
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Category selector",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate500
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Member",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Member selector",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate500
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Shop",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Shop selector",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            startDate = null
                            endDate = null
                            selectedCategory = null
                            selectedMember = null
                            selectedShop = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Slate700
                        )
                    ) {
                        Text(
                            text = "Clear",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Button(
                        onClick = {
                            onApplyFilters(startDate, endDate, selectedCategory, selectedMember, selectedShop)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Teal600
                        )
                    ) {
                        Text(
                            text = "Apply Filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
