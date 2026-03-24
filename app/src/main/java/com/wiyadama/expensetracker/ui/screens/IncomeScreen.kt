package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.wiyadama.expensetracker.data.entity.Income
import com.wiyadama.expensetracker.data.entity.RentalProperty
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.viewmodels.IncomeViewModel
import com.wiyadama.expensetracker.util.CurrencyFormatter
import com.wiyadama.expensetracker.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

private fun getCurrentMonthStart(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

private fun getNextMonthStart(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.add(Calendar.MONTH, 1)
    return calendar.timeInMillis
}

@Composable
fun IncomeScreen(
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val allIncomes by viewModel.allIncomes.collectAsState()
    val rentalProperties by viewModel.rentalProperties.collectAsState()
    val propertiesWithTransactions by viewModel.propertiesWithTransactions.collectAsState()
    val allRentTransactions by viewModel.allRentTransactions.collectAsState()
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddIncomeDialog by remember { mutableStateOf(false) }
    var showAddPropertyDialog by remember { mutableStateOf(false) }
    var editingProperty by remember { mutableStateOf<RentalProperty?>(null) }
    var viewingProperty by remember { mutableStateOf<RentalProperty?>(null) }
    
    val tabs = listOf("House Rent", "IET Salary", "Solar", "Other")
    val categoryTypes = listOf("HOUSE_RENT", "IET_SALARY", "SOLAR", "OTHER")
    
    val filteredIncomes = allIncomes.filter { it.categoryType == categoryTypes[selectedTab] }
    val totalIncome = filteredIncomes.sumOf { it.amountCents }
    
    // Generate current month transactions for all properties on first load
    LaunchedEffect(rentalProperties) {
        rentalProperties.forEach { property ->
            viewModel.generateCurrentMonthTransaction(property)
        }
    }

    // Show property detail screen if viewing a property
    if (viewingProperty != null) {
        val propertyTransactions by viewModel.getPropertyTransactions(viewingProperty!!.id).collectAsState(initial = emptyList())
        
        PropertyDetailScreen(
            property = viewingProperty!!,
            allTransactions = propertyTransactions,
            viewModel = viewModel,
            onBack = { viewingProperty = null },
            onPropertyUpdated = { updatedProperty ->
                viewingProperty = updatedProperty
            },
            onDeleteProperty = {
                viewingProperty?.let { property ->
                    viewModel.deleteProperty(property)
                    viewingProperty = null
                }
            },
            onRecordPayment = { transaction ->
                transaction?.let { tx ->
                    viewModel.recordFullPayment(tx.id)
                }
            },
            onEditPayment = { transaction, amount ->
                viewModel.recordPartialPayment(transaction.id, amount)
            },
            onDeletePayment = { transaction ->
                viewModel.deleteTransaction(transaction)
            }
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Emerald600, Teal600)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Income",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Track your income sources",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        items(tabs.chunked(2)) { tabPair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tabPair.forEachIndexed { pairIndex, title ->
                    val index = tabs.indexOf(title)
                    val isSelected = selectedTab == index
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.White else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 8.dp else 4.dp
                        ),
                        border = if (isSelected) BorderStroke(2.dp, Emerald600) else null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = if (isSelected) 
                                                listOf(Emerald500, Teal500) 
                                            else 
                                                listOf(Slate100, Slate50)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (index) {
                                        0 -> Icons.Default.Home
                                        1 -> Icons.Default.Work
                                        else -> Icons.Default.WbSunny
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Slate500,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) Emerald600 else Slate600,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                if (tabPair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Total ${tabs[selectedTab]}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(totalIncome, "LKR"),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Emerald600,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rental Properties",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    TextButton(onClick = { showAddPropertyDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Property",
                            color = Emerald600,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            val shops = propertiesWithTransactions.filter { it.property.type == "SHOP" }
            val houses = propertiesWithTransactions.filter { it.property.type == "HOUSE" }

            if (shops.isNotEmpty()) {
                item {
                    Text(
                        text = "Shops",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700,
                        modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
                items(shops) { propertyWithTx ->
                    RentalPropertyCard(
                        property = propertyWithTx.property,
                        currentTransaction = propertyWithTx.currentMonthTransaction,
                        onPaymentClick = { },
                        onEditClick = { },
                        onClick = {
                            viewingProperty = propertyWithTx.property
                        },
                        viewModel = viewModel
                    )
                }
            }

            if (houses.isNotEmpty()) {
                item {
                    Text(
                        text = "Houses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700,
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
                items(houses) { propertyWithTx ->
                    RentalPropertyCard(
                        property = propertyWithTx.property,
                        currentTransaction = propertyWithTx.currentMonthTransaction,
                        onPaymentClick = { },
                        onEditClick = { },
                        onClick = {
                            viewingProperty = propertyWithTx.property
                        },
                        viewModel = viewModel
                    )
                }
            }

            if (rentalProperties.isEmpty()) {
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
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Slate300,
                                modifier = Modifier.size(56.dp)
                            )
                            Text(
                                text = "No rental properties yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate500
                            )
                            Text(
                                text = "Add properties to track rental income",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400
                            )
                        }
                    }
                }
            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Income History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    TextButton(onClick = { showAddIncomeDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Emerald600,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Income",
                            color = Emerald600,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (filteredIncomes.isEmpty()) {
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
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Slate300,
                                modifier = Modifier.size(56.dp)
                            )
                            Text(
                                text = "No income records yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate500
                            )
                            Text(
                                text = "Add income to track your earnings",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate400
                            )
                        }
                    }
                }
            } else {
                items(filteredIncomes) { income ->
                    IncomeItemCard(income = income)
                }
            }
        }
    }

    if (showAddIncomeDialog) {
        AddIncomeDialog(
            categoryType = categoryTypes[selectedTab],
            onDismiss = { showAddIncomeDialog = false },
            onConfirm = { amount, category, notes, date ->
                viewModel.addIncome(amount, category, null, notes, date)
                showAddIncomeDialog = false
            }
        )
    }

    if (showAddPropertyDialog) {
        AddPropertyDialog(
            property = editingProperty,
            onDismiss = { 
                showAddPropertyDialog = false
                editingProperty = null
            },
            onConfirm = { property ->
                if (editingProperty != null) {
                    viewModel.updateProperty(property)
                    if (viewingProperty?.id == property.id) {
                        viewingProperty = property
                    }
                } else {
                    viewModel.addProperty(property)
                }
                showAddPropertyDialog = false
                editingProperty = null
            }
        )
    }


}

@Composable
fun ExpandableIncomeCard(
    title: String,
    currentMonthTotal: Int,
    categoryType: String,
    allIncomes: List<Income>
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Filter incomes by category type
    val categoryIncomes = allIncomes.filter { it.categoryType == categoryType }
    
    // Group incomes by month
    val incomesByMonth = categoryIncomes
        .sortedByDescending { it.dateTime }
        .groupBy { income ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = income.dateTime
            "${calendar.get(Calendar.YEAR)}-${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}"
        }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(currentMonthTotal, "LKR"),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Emerald600,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Slate400,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Slate100)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Monthly Breakdown",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                if (incomesByMonth.isEmpty()) {
                    Text(
                        text = "No income history yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate400,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    incomesByMonth.entries.take(6).forEach { (monthKey, monthIncomes) ->
                        val monthTotal = monthIncomes.sumOf { it.amountCents }
                        
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
                                        .format(Date(monthIncomes.first().dateTime)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate900
                                )
                                Text(
                                    text = "${monthIncomes.size} transaction${if (monthIncomes.size != 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            
                            Text(
                                text = CurrencyFormatter.formatWithSymbol(monthTotal, "LKR"),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Emerald600
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyRentSummaryCard(
    properties: List<RentalProperty>,
    allTransactions: List<com.wiyadama.expensetracker.data.entity.RentTransaction>
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Get current month transactions
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val currentMonthStart = calendar.timeInMillis
    calendar.add(Calendar.MONTH, 1)
    val nextMonthStart = calendar.timeInMillis
    
    val currentMonthTransactions = allTransactions.filter { 
        it.dueDate >= currentMonthStart && it.dueDate < nextMonthStart 
    }
    
    val currentMonthCollected = currentMonthTransactions
        .filter { it.status == com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID }
        .sumOf { it.paidAmount } +
        currentMonthTransactions
            .filter { it.status == com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL }
            .sumOf { it.paidAmount }
    
    // Group all transactions by month for history
    val transactionsByMonth = allTransactions
        .sortedByDescending { it.dueDate }
        .groupBy { transaction ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = transaction.dueDate
            "${cal.get(Calendar.YEAR)}-${String.format("%02d", cal.get(Calendar.MONTH) + 1)}"
        }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "This Month Collected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(currentMonthCollected, "LKR"),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Emerald600,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Slate400,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Slate100)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Monthly Breakdown by Property",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                if (transactionsByMonth.isEmpty()) {
                    Text(
                        text = "No payment history yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate400,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    transactionsByMonth.entries.take(6).forEach { (monthKey, monthTransactions) ->
                        var monthExpanded by remember { mutableStateOf(false) }
                        
                        val monthCollected = monthTransactions
                            .filter { it.status == com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID }
                            .sumOf { it.paidAmount } +
                            monthTransactions
                                .filter { it.status == com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL }
                                .sumOf { it.paidAmount }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { monthExpanded = !monthExpanded }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (monthExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        tint = Slate400,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                                            .format(Date(monthTransactions.first().dueDate)),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate900
                                    )
                                }
                                
                                Text(
                                    text = CurrencyFormatter.formatWithSymbol(monthCollected, "LKR"),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Emerald600
                                )
                            }
                            
                            if (monthExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 28.dp, top = 4.dp, bottom = 8.dp)
                                ) {
                                    monthTransactions.forEach { transaction ->
                                        val property = properties.find { it.id == transaction.propertyId }
                                        if (property != null) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 6.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = property.name,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Slate700
                                                    )
                                                    val statusText = when (transaction.status) {
                                                        com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID -> "Paid"
                                                        com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL -> "Partial"
                                                        com.wiyadama.expensetracker.data.entity.RentPaymentStatus.UNPAID -> "Unpaid"
                                                        com.wiyadama.expensetracker.data.entity.RentPaymentStatus.OVERDUE -> "Overdue"
                                                    }
                                                    Text(
                                                        text = statusText,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = when (transaction.status) {
                                                            com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID -> Emerald600
                                                            com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL -> Color(0xFFB45309)
                                                            else -> Slate400
                                                        }
                                                    )
                                                }
                                                
                                                Text(
                                                    text = CurrencyFormatter.formatWithSymbol(transaction.paidAmount, "LKR"),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (transaction.paidAmount > 0) Slate900 else Slate400
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageIncomeCategoriesDialog(
    currentCategories: List<String>,
    onDismiss: () -> Unit
) {
    var categories by remember { mutableStateOf(currentCategories.toMutableList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var editingName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Emerald600
                    )
                    Text("Manage Income Categories")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate50)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = Emerald600,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate900
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { 
                                        editingIndex = index
                                        editingName = category
                                    },
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
                                    onClick = { 
                                        categories = categories.toMutableList().apply { removeAt(index) }
                                    },
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
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Emerald600)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Emerald600,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Category",
                        color = Emerald600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate600)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
    
    if (showAddDialog) {
        var newCategoryName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Income Category") },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Category Name") },
                    placeholder = { Text("e.g., Freelance, Dividends") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCategoryName.isNotBlank()) {
                            categories = categories.toMutableList().apply { add(newCategoryName) }
                            showAddDialog = false
                        }
                    },
                    enabled = newCategoryName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    editingIndex?.let { index ->
        AlertDialog(
            onDismissRequest = { editingIndex = null },
            title = { Text("Edit Category") },
            text = {
                OutlinedTextField(
                    value = editingName,
                    onValueChange = { editingName = it },
                    label = { Text("Category Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editingName.isNotBlank()) {
                            categories = categories.toMutableList().apply { set(index, editingName) }
                            editingIndex = null
                        }
                    },
                    enabled = editingName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingIndex = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RentalPropertyCard(
    property: RentalProperty,
    currentTransaction: com.wiyadama.expensetracker.data.entity.RentTransaction?,
    onPaymentClick: () -> Unit,
    onEditClick: () -> Unit,
    onClick: () -> Unit,
    viewModel: IncomeViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val status = currentTransaction?.status ?: com.wiyadama.expensetracker.data.entity.RentPaymentStatus.UNPAID

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                if (property.imagePath == null) {
                                    Brush.linearGradient(
                                        colors = if (property.type == "SHOP") 
                                            listOf(Teal500, Emerald500) 
                                        else 
                                            listOf(Indigo500, Purple500)
                                    )
                                } else {
                                    Brush.linearGradient(listOf(Slate100, Slate100))
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (property.imagePath != null) {
                            androidx.compose.foundation.Image(
                                painter = coil.compose.rememberAsyncImagePainter(
                                    android.net.Uri.parse(property.imagePath)
                                ),
                                contentDescription = property.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = if (property.type == "SHOP") Icons.Default.Store else Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = property.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                        if (property.currentTenant != null) {
                            Text(
                                text = property.currentTenant,
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (statusText, statusBgColor, statusTextColor) = when (status) {
                                com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID -> 
                                    Triple("Paid", Emerald50, Emerald700)
                                com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL -> 
                                    Triple("Partial", Color(0xFFFEF3C7), Color(0xFFB45309))
                                com.wiyadama.expensetracker.data.entity.RentPaymentStatus.OVERDUE -> 
                                    Triple("Overdue", Red100, Red700)
                                com.wiyadama.expensetracker.data.entity.RentPaymentStatus.UNPAID -> 
                                    Triple("Unpaid", Slate100, Slate700)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = statusBgColor
                            ) {
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = statusTextColor,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            if (status == com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PARTIAL) {
                                currentTransaction?.let { tx ->
                                    Text(
                                        text = "${CurrencyFormatter.formatWithSymbol(tx.paidAmount, "LKR")} / ${CurrencyFormatter.formatWithSymbol(tx.expectedAmount, "LKR")}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate500
                                    )
                                }
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.formatWithSymbol(property.monthlyRent, "LKR"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    Text(
                        text = "/month",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Slate100)
                Spacer(modifier = Modifier.height(12.dp))

                currentTransaction?.let { tx ->
                    if (tx.paidDate != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Payment Date:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                            Text(
                                text = DateUtils.formatDate(tx.paidDate),
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    if (tx.paidAmount > 0 && tx.paidAmount < tx.expectedAmount) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Paid Amount:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                            Text(
                                text = CurrencyFormatter.formatWithSymbol(tx.paidAmount, "LKR"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Emerald700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Remaining:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                            Text(
                                text = CurrencyFormatter.formatWithSymbol(tx.expectedAmount - tx.paidAmount, "LKR"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Red700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (property.advancePayment > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Advance Payment:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(property.advancePayment, "LKR"),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (property.notes != null && property.notes.isNotBlank()) {
                    Text(
                        text = property.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Full Details & History")
                }
            }
        }
    }
}

@Composable
fun IncomeItemCard(income: Income) {
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
                                colors = listOf(Emerald400, Teal400)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = income.notes ?: "Income",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Slate900
                    )
                    Text(
                        text = DateUtils.formatDate(income.dateTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
            Text(
                text = CurrencyFormatter.formatWithSymbol(income.amountCents, "LKR"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeDialog(
    categoryType: String,
    onDismiss: () -> Unit,
    onConfirm: (amountCents: Int, category: String, notes: String, date: Long) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categoryType) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var categoryExpanded by remember { mutableStateOf(false) }
    
    val categories = listOf(
        "IET_SALARY" to "IET Salary",
        "SOLAR" to "Solar Income",
        "OTHER" to "Other Income"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Income") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = categories.find { it.first == selectedCategory }?.second ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedCategory = value
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) amount = it },
                    label = { Text("Amount (LKR)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountCents = (amount.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                    if (amountCents > 0) {
                        onConfirm(amountCents, selectedCategory, notes, selectedDate)
                    }
                },
                enabled = amount.isNotEmpty() && amount.toDoubleOrNull() != null
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyDialog(
    property: RentalProperty? = null,
    onDismiss: () -> Unit,
    onConfirm: (RentalProperty) -> Unit
) {
    var name by remember { mutableStateOf(property?.name ?: "") }
    var type by remember { mutableStateOf(property?.type ?: "SHOP") }
    var tenant by remember { mutableStateOf(property?.currentTenant ?: "") }
    var monthlyRent by remember { mutableStateOf(if (property?.monthlyRent != null) (property.monthlyRent / 100).toString() else "") }
    var advance by remember { mutableStateOf(if (property?.advancePayment != null && property.advancePayment > 0) (property.advancePayment / 100).toString() else "") }
    var notes by remember { mutableStateOf(property?.notes ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (property != null) "Edit Rental Property" else "Add Rental Property") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Property Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (type == "SHOP") "Shop" else "House",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Shop") },
                            onClick = {
                                type = "SHOP"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("House") },
                            onClick = {
                                type = "HOUSE"
                                expanded = false
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = tenant,
                    onValueChange = { tenant = it },
                    label = { Text("Current Tenant") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = monthlyRent,
                    onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) monthlyRent = it },
                    label = { Text("Monthly Rent (LKR)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = advance,
                    onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) advance = it },
                    label = { Text("Advance Payment (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val rentCents = (monthlyRent.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                    val advanceCents = (advance.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                    if (name.isNotEmpty() && rentCents > 0) {
                        val updatedProperty = if (property != null) {
                            // Editing existing property - preserve ID and other fields
                            property.copy(
                                name = name,
                                type = type,
                                currentTenant = tenant.ifBlank { null },
                                monthlyRent = rentCents,
                                advancePayment = advanceCents,
                                notes = notes.ifBlank { null },
                                updatedAt = System.currentTimeMillis()
                            )
                        } else {
                            // Creating new property
                            RentalProperty(
                                name = name,
                                type = type,
                                currentTenant = tenant.ifBlank { null },
                                monthlyRent = rentCents,
                                advancePayment = advanceCents,
                                notes = notes.ifBlank { null }
                            )
                        }
                        onConfirm(updatedProperty)
                    }
                },
                enabled = name.isNotEmpty() && monthlyRent.isNotEmpty() && monthlyRent.toDoubleOrNull() != null
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
