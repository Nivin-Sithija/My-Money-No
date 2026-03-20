package com.wiyadama.expensetracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun MembersScreen(
    members: List<Member>,
    shops: List<Shop>,
    transactions: List<Transaction>,
    categories: List<Category>,
    onAddMember: () -> Unit,
    onAddShop: () -> Unit,
    onMemberClick: (Member) -> Unit,
    onShopClick: (Shop) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Members", "Shops")

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
                        text = "Members & Shops",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Manage your members and shops",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Tab Selector
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
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Button(
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Indigo600 else Slate50,
                                contentColor = if (isSelected) Color.White else Slate700
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (isSelected) 4.dp else 0.dp
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (index == 0) Icons.Default.People else Icons.Default.Store,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        when (selectedTabIndex) {
            // Members Tab
            0 -> {
                // Add Member Button
                item {
                    Button(
                        onClick = onAddMember,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple600
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Text(
                                text = "Add New Member",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (members.isEmpty()) {
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
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = Slate300,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "No members yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate500
                                )
                                Text(
                                    text = "Add members to track individual expenses",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate400
                                )
                            }
                        }
                    }
                } else {
                    items(members) { member ->
                        val memberTransactions = transactions.filter {
                            it.memberId == member.id && it.deletedAt == null
                        }.sortedByDescending { it.dateTime }

                        ExpandableMemberCard(
                            member = member,
                            transactions = memberTransactions,
                            categories = categories,
                            onClick = { onMemberClick(member) }
                        )
                    }
                }
            }

            // Shops Tab
            1 -> {
                // Add Shop Button
                item {
                    Button(
                        onClick = onAddShop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Teal600
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Text(
                                text = "Add New Shop",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (shops.isEmpty()) {
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
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = Slate300,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = "No shops yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate500
                                )
                                Text(
                                    text = "Add shops to track where you spend",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate400
                                )
                            }
                        }
                    }
                } else {
                    items(shops) { shop ->
                        val shopTransactions = transactions.filter {
                            it.shopId == shop.id && it.deletedAt == null
                        }.sortedByDescending { it.dateTime }

                        ExpandableShopCard(
                            shop = shop,
                            transactions = shopTransactions,
                            categories = categories,
                            onClick = { onShopClick(shop) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableMemberCard(
    member: Member,
    transactions: List<Transaction>,
    categories: List<Category>,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val totalSpent = transactions.sumOf { it.amountCents }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Row: Avatar + Name + Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Purple500, Pink500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.name.firstOrNull()?.toString()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        Text(
                            text = "${transactions.size} transactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(totalSpent, "LKR"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expanded Transaction History
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(color = Slate100, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (transactions.isEmpty()) {
                        Text(
                            text = "No transactions yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                    } else {
                        // Show recent transactions (max 10)
                        transactions.take(10).forEach { tx ->
                            val category = categories.find { it.id == tx.categoryId }
                            val catData = getCategoryData(category?.name ?: "")
                            MiniTransactionRow(
                                description = tx.notes?.ifBlank { tx.merchantName ?: "Expense" } ?: tx.merchantName ?: "Expense",
                                categoryName = category?.name ?: "Unknown",
                                amount = tx.amountCents,
                                date = tx.dateTime,
                                icon = catData.icon,
                                iconColor = catData.iconColor,
                                bgColors = catData.bgColors
                            )
                        }
                        }
                            Text(
                                text = "View Details & Settings →",
                                style = MaterialTheme.typography.bodySmall,
                                color = Purple600,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp)
                                    .clickable { onClick() }
                            )
                    }
                }
            }
        }
    }


@Composable
fun ExpandableShopCard(
    shop: Shop,
    transactions: List<Transaction>,
    categories: List<Category>,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val totalSpent = transactions.sumOf { it.amountCents }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Row: Icon + Name + Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Shop Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Teal500, Emerald500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = shop.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        if (shop.address != null && shop.address.isNotBlank()) {
                            Text(
                                text = shop.address,
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400,
                                maxLines = 1
                            )
                        } else {
                            Text(
                                text = "${transactions.size} transactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(totalSpent, "LKR"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expanded Transaction History
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(color = Slate100, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (transactions.isEmpty()) {
                        Text(
                            text = "No transactions at this shop yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate400,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                    } else {
                        transactions.take(10).forEach { tx ->
                            val category = categories.find { it.id == tx.categoryId }
                            val catData = getCategoryData(category?.name ?: "")
                            MiniTransactionRow(
                                description = tx.notes?.ifBlank { tx.merchantName ?: "Expense" } ?: tx.merchantName ?: "Expense",
                                categoryName = category?.name ?: "Unknown",
                                amount = tx.amountCents,
                                date = tx.dateTime,
                                icon = catData.icon,
                                iconColor = catData.iconColor,
                                bgColors = catData.bgColors
                            )
                        }
                        if (transactions.size > 10) {
                            Text(
                                text = "And ${transactions.size - 10} more transactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = Teal600,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                            )
                        }
                        
                        Text(
                            text = "View Details & History →",
                            style = MaterialTheme.typography.bodySmall,
                            color = Teal600,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(top = 12.dp, start = 8.dp)
                                .clickable { onClick() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopDetailScreen(
    shop: Shop,
    transactions: List<Transaction>,
    categories: List<Category>,
    totalExpenses: Int,
    transactionCount: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    BackHandler(onBack = onBack)

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
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Shop Icon
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = shop.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (shop.address != null && shop.address.isNotBlank()) {
                        Text(
                            text = shop.address,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Expenses
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Indigo500, Purple500))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Total Spent",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(totalExpenses, "LKR"),
                            style = MaterialTheme.typography.titleLarge,
                            color = Slate900,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Transactions
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Teal500, Emerald500))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Transactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$transactionCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = Slate900,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Actions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate100,
                        contentColor = Slate700
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Edit Details")
                    }
                }

                Button(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red50,
                        contentColor = Red600
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Delete")
                    }
                }
            }
        }

        // Transactions List
        if (transactions.isNotEmpty()) {
            item {
                Text(
                    text = "Transaction History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            items(transactions) { tx ->
                val category = categories.find { it.id == tx.categoryId }
                val catData = getCategoryData(category?.name ?: "")
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    MiniTransactionRow(
                        description = tx.notes?.ifBlank { tx.merchantName ?: "Expense" } ?: tx.merchantName ?: "Expense",
                        categoryName = category?.name ?: "Unknown",
                        amount = tx.amountCents,
                        date = tx.dateTime,
                        icon = catData.icon,
                        iconColor = catData.iconColor,
                        bgColors = catData.bgColors
                    )
                }
            }
        }
    }
}

@Composable
fun MiniTransactionRow(
    description: String,
    categoryName: String,
    amount: Int,
    date: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColors: List<Color>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
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
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.linearGradient(bgColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Slate800,
                    maxLines = 1
                )
                Text(
                    text = "$categoryName • ${DateUtils.formatDate(date)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate400
                )
            }
        }
        Text(
            text = "-${CurrencyFormatter.formatWithSymbol(amount, "LKR")}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Slate700
        )
    }
}

@Composable
fun MemberDetailScreen(
    member: Member,
    transactions: List<Transaction>,
    categories: List<Category>,
    totalExpenses: Int,
    transactionCount: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    BackHandler(onBack = onBack)

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
                            colors = listOf(Purple600, Pink600)
                        )
                    )
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Member Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.name.firstOrNull()?.toString()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Expenses
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Indigo500, Purple500))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Total Expenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(totalExpenses, "LKR"),
                            style = MaterialTheme.typography.titleLarge,
                            color = Slate900,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Transactions
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(Teal500, Emerald500))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Transactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$transactionCount",
                            style = MaterialTheme.typography.titleLarge,
                            color = Slate900,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Actions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Purple600
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Edit", fontWeight = FontWeight.Medium)
                    }
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Red500
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Delete", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Transactions List
        if (transactions.isNotEmpty()) {
            item {
                Text(
                    text = "Transaction History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            items(transactions) { tx ->
                val category = categories.find { it.id == tx.categoryId }
                val catData = getCategoryData(category?.name ?: "")
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    MiniTransactionRow(
                        description = tx.notes?.ifBlank { tx.merchantName ?: "Expense" } ?: tx.merchantName ?: "Expense",
                        categoryName = category?.name ?: "Unknown",
                        amount = tx.amountCents,
                        date = tx.dateTime,
                        icon = catData.icon,
                        iconColor = catData.iconColor,
                        bgColors = catData.bgColors
                    )
                }
            }
        }
    }
}
