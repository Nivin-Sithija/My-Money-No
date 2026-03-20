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

@Composable
fun IncomeScreen(
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val allIncomes by viewModel.allIncomes.collectAsState()
    val rentalProperties by viewModel.rentalProperties.collectAsState()
    val propertiesWithTransactions by viewModel.propertiesWithTransactions.collectAsState()
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddIncomeDialog by remember { mutableStateOf(false) }
    var showAddPropertyDialog by remember { mutableStateOf(false) }
    var showManageCategoriesDialog by remember { mutableStateOf(false) }
    var editingProperty by remember { mutableStateOf<RentalProperty?>(null) }
    
    val tabs = listOf("House Rent", "IET Salary", "Solar")
    val categoryTypes = listOf("HOUSE_RENT", "IET_SALARY", "SOLAR")
    
    val filteredIncomes = allIncomes.filter { it.categoryType == categoryTypes[selectedTab] }
    val totalIncome = filteredIncomes.sumOf { it.amountCents }
    
    // Generate current month transactions for all properties on first load
    LaunchedEffect(rentalProperties) {
        rentalProperties.forEach { property ->
            viewModel.generateCurrentMonthTransaction(property)
        }
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
                        IconButton(
                            onClick = { showManageCategoriesDialog = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Manage Categories",
                                tint = Color.White
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
                        onPaymentClick = { 
                            propertyWithTx.currentMonthTransaction?.let { tx ->
                                viewModel.togglePaymentStatus(tx.id)
                            }
                        },
                        onEditClick = { 
                            editingProperty = propertyWithTx.property
                            showAddPropertyDialog = true
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
                        onPaymentClick = { 
                            propertyWithTx.currentMonthTransaction?.let { tx ->
                                viewModel.togglePaymentStatus(tx.id)
                            }
                        },
                        onEditClick = { 
                            editingProperty = propertyWithTx.property
                            showAddPropertyDialog = true
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
            onConfirm = { amount, notes, date ->
                viewModel.addIncome(amount, categoryTypes[selectedTab], null, notes, date)
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
                    viewModel.updateProperty(editingProperty!!.copy(
                        name = property.name,
                        type = property.type,
                        currentTenant = property.currentTenant,
                        monthlyRent = property.monthlyRent,
                        advancePayment = property.advancePayment,
                        notes = property.notes
                    ))
                } else {
                    viewModel.addProperty(property)
                }
                showAddPropertyDialog = false
                editingProperty = null
            }
        )
    }

    if (showManageCategoriesDialog) {
        ManageIncomeCategoriesDialog(
            currentCategories = tabs,
            onDismiss = { showManageCategoriesDialog = false }
        )
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
                                Brush.linearGradient(
                                    colors = if (property.type == "SHOP") 
                                        listOf(Teal500, Emerald500) 
                                    else 
                                        listOf(Indigo500, Purple500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (property.type == "SHOP") Icons.Default.Store else Icons.Default.Home,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (status != com.wiyadama.expensetracker.data.entity.RentPaymentStatus.PAID) {
                        Button(
                            onClick = onPaymentClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mark Paid")
                        }
                    }
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
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
    onConfirm: (amountCents: Int, notes: String, date: Long) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Income") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                        onConfirm(amountCents, notes, selectedDate)
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
                        val property = RentalProperty(
                            name = name,
                            type = type,
                            currentTenant = tenant.ifBlank { null },
                            monthlyRent = rentCents,
                            advancePayment = advanceCents,
                            notes = notes.ifBlank { null }
                        )
                        onConfirm(property)
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
