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
    
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

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
                onEdit = { transactionToEdit = transaction },
                onDelete = { transactionToDelete = transaction }
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

    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this past transaction?") },
            confirmButton = {
                TextButton(onClick = { 
                    onDeleteTransaction(transactionToDelete!!)
                    transactionToDelete = null 
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) { Text("Cancel") }
            }
        )
    }

    if (transactionToEdit != null) {
        AlertDialog(
            onDismissRequest = { transactionToEdit = null },
            title = { Text("Edit Transaction") },
            text = { Text("Are you sure you want to edit this past transaction?") },
            confirmButton = {
                TextButton(onClick = { 
                    onEditTransaction(transactionToEdit!!)
                    transactionToEdit = null 
                }) { Text("Edit") }
            },
            dismissButton = {
                TextButton(onClick = { transactionToEdit = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun HistoryTransactionItemWithActions(
    transaction: Transaction,
    category: Category?,
    member: Member?,
    shop: Shop?,
    onClick: () -> Unit,
    onEdit: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit
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
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = category?.name ?: "Unknown",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900,
                                modifier = Modifier.weight(1f, fill = false),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "-${CurrencyFormatter.formatWithSymbol(transaction.amountCents, "LKR")}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.deletedAt != null) Red900 else Slate900
                            )
                        }
                        val subtitleParts = mutableListOf<String>()
                        val description = transaction.merchantName ?: transaction.notes
                        if (!description.isNullOrBlank()) subtitleParts.add(description)
                        if (member != null) subtitleParts.add(member.name)
                        if (shop != null) subtitleParts.add(shop.name)
                        
                        if (subtitleParts.isNotEmpty()) {
                            Text(
                                text = subtitleParts.joinToString(" • "),
                                style = MaterialTheme.typography.bodySmall,
                                color = Indigo500,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = DateUtils.formatCompactDate(transaction.dateTime),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            maxLines = 1
                        )
                        if (transaction.deletedAt != null) {
                            Text(
                                text = "Deleted on ${DateUtils.formatDateTime(transaction.deletedAt)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Red500,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                if (transaction.deletedAt == null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { onEdit(transaction) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Indigo600,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = { onDelete(transaction) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Red600,
                                modifier = Modifier.size(18.dp)
                            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (startDate: Long?, endDate: Long?, categoryId: Long?, memberId: Long?, shopId: Long?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Teal600
                    )
                    Text(
                        text = "Filter Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Coming soon: Advanced filtering options",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
                
                Divider(color = Slate200)
                
                Text(
                    text = "Available filters will include:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500,
                    fontWeight = FontWeight.SemiBold
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Date Range",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Member",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Shop",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Teal600),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("OK")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
