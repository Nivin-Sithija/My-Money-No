package com.wiyadama.expensetracker.ui.screens

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
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddIncomeDialog by remember { mutableStateOf(false) }
    var showAddPropertyDialog by remember { mutableStateOf(false) }
    var showManageCategoriesDialog by remember { mutableStateOf(false) }
    
    val tabs = listOf("House Rent", "IET Salary", "Solar")
    val categoryTypes = listOf("HOUSE_RENT", "IET_SALARY", "SOLAR")
    
    val filteredIncomes = allIncomes.filter { it.categoryType == categoryTypes[selectedTab] }
    val totalIncome = filteredIncomes.sumOf { it.amountCents }

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
                        val isSelected = selectedTab == index
                        Button(
                            onClick = { selectedTab = index },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Emerald600 else Slate50,
                                contentColor = if (isSelected) Color.White else Slate700
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (isSelected) 4.dp else 0.dp
                            )
                        ) {
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

            val shops = rentalProperties.filter { it.type == "SHOP" }
            val houses = rentalProperties.filter { it.type == "HOUSE" }

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
                items(shops) { property ->
                    RentalPropertyCard(
                        property = property,
                        onPaymentClick = { 
                            viewModel.updatePropertyPayment(property.id, System.currentTimeMillis(), property.monthlyRent)
                        },
                        onEditClick = { },
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
                items(houses) { property ->
                    RentalPropertyCard(
                        property = property,
                        onPaymentClick = { 
                            viewModel.updatePropertyPayment(property.id, System.currentTimeMillis(), property.monthlyRent)
                        },
                        onEditClick = { },
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
            onDismiss = { showAddPropertyDialog = false },
            onConfirm = { property ->
                viewModel.addProperty(property)
                showAddPropertyDialog = false
            }
        )
    }
}

@Composable
fun RentalPropertyCard(
    property: RentalProperty,
    onPaymentClick: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: IncomeViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val isPaid = property.lastPaidDate?.let {
        val calendar = Calendar.getInstance()
        val lastPaidCalendar = Calendar.getInstance().apply { timeInMillis = it }
        calendar.get(Calendar.MONTH) == lastPaidCalendar.get(Calendar.MONTH) &&
        calendar.get(Calendar.YEAR) == lastPaidCalendar.get(Calendar.YEAR)
    } ?: false

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
                            if (isPaid) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Emerald50
                                ) {
                                    Text(
                                        text = "Paid",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Emerald700,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Red100
                                ) {
                                    Text(
                                        text = "Unpaid",
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

                if (property.lastPaidDate != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Last Payment:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = DateUtils.formatDate(property.lastPaidDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
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
                    if (!isPaid) {
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
                text = "+${CurrencyFormatter.formatWithSymbol(income.amountCents, "LKR")}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Emerald600
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
    onDismiss: () -> Unit,
    onConfirm: (RentalProperty) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("SHOP") }
    var tenant by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var advance by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Rental Property") },
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
