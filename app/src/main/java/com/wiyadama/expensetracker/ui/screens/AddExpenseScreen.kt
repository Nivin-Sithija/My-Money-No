package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.ui.components.AddShopDialog
import com.wiyadama.expensetracker.ui.components.MemberDialog
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.util.getCategoryData
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onDismiss: () -> Unit,
    onSave: (amount: Int, categoryId: Long, subcategoryId: Long?, memberId: Long?, shopId: Long?, notes: String, date: Long) -> Unit,
    onAddShop: (name: String, address: String, imagePath: String?, onSuccess: (Shop) -> Unit) -> Unit,
    onAddMember: (name: String, color: Int, imagePath: String?, onSuccess: (Member) -> Unit) -> Unit,
    categories: List<Category>,
    members: List<Member>,
    shops: List<Shop>,
    editingTransaction: Transaction? = null
) {
    var amount by remember { mutableStateOf(editingTransaction?.let { (it.amountCents / 100.0).toString() } ?: "") }
    var selectedCategory by remember { mutableStateOf(categories.find { it.id == editingTransaction?.categoryId }) }
    var selectedMember by remember { mutableStateOf(members.find { it.id == editingTransaction?.memberId }) }
    var selectedShop by remember { mutableStateOf(shops.find { it.id == editingTransaction?.shopId }) }
    var description by remember { mutableStateOf(editingTransaction?.notes ?: "") }
    var selectedDate by remember { mutableLongStateOf(editingTransaction?.dateTime ?: System.currentTimeMillis()) }
    
    var showMemberDialog by remember { mutableStateOf(false) }
    var showShopDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }
    
    val mainCategories = categories.filter { it.parentId == null }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Compact Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Indigo600, Purple600)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (editingTransaction != null) "Edit Expense" else "Add Expense",
                            style = MaterialTheme.typography.titleLarge,
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

                // Compact Form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Selector (First)
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCategoryPicker = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = selectedCategory?.let { getCategoryData(it.name).icon } ?: Icons.Default.Category,
                                    contentDescription = null,
                                    tint = selectedCategory?.let { getCategoryData(it.name).iconColor } ?: Slate400
                                )
                                Text(
                                    text = selectedCategory?.name ?: "Select Category",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (selectedCategory != null) Slate900 else Slate400
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Slate400
                            )
                        }
                    }

                    // Amount Input (After Category)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) amount = it },
                        label = { Text("Amount (LKR)") },
                        leadingIcon = {
                            Icon(Icons.Default.AttachMoney, contentDescription = null)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo500,
                            unfocusedBorderColor = Slate200
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo500,
                            unfocusedBorderColor = Slate200
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Compact Row for Member, Shop, Date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Member
                        CompactSelectorCard(
                            icon = Icons.Default.Person,
                            label = selectedMember?.name ?: "Member",
                            onClick = { showMemberDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Shop
                        CompactSelectorCard(
                            icon = Icons.Default.Store,
                            label = selectedShop?.name ?: "Shop",
                            onClick = { showShopDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Date
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Slate600
                                )
                                Text(
                                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate)),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate900
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Slate400
                            )
                        }
                    }
                }

                // Bottom Buttons
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Slate600
                            )
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                val amountCents = (amount.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                                if (amountCents > 0 && selectedCategory != null) {
                                    onSave(
                                        amountCents,
                                        selectedCategory!!.id,
                                        null,
                                        selectedMember?.id,
                                        selectedShop?.id,
                                        description,
                                        selectedDate
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = amount.isNotEmpty() && amount.toDoubleOrNull() != null && selectedCategory != null,
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showMemberDialog) {
        MemberDialog(
            onDismiss = { showMemberDialog = false },
            onConfirm = { name, color, imagePath ->
                onAddMember(name, color, imagePath) { newMember ->
                    selectedMember = newMember
                    showMemberDialog = false
                }
            }
        )
    }

    if (showShopDialog) {
        AddShopDialog(
            onDismiss = { showShopDialog = false },
            onConfirm = { name, address, imagePath ->
                onAddShop(name, address, imagePath) { newShop ->
                    selectedShop = newShop
                    showShopDialog = false
                }
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialDate = selectedDate
        )
    }

    if (showCategoryPicker) {
        CategoryPickerDialog(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
                showCategoryPicker = false
            },
            onDismiss = { showCategoryPicker = false }
        )
    }
}

@Composable
fun CompactSelectorCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Slate600,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Slate700,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerDialog(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    val mainCategories = categories.filter { it.parentId == null }
    val subcategoriesMap = categories.filter { it.parentId != null }.groupBy { it.parentId }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Category", style = MaterialTheme.typography.titleMedium) },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                mainCategories.forEach { mainCategory ->
                    item {
                        val catData = getCategoryData(mainCategory.name)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCategorySelected(mainCategory) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedCategory?.id == mainCategory.id) Indigo50 else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(catData.iconColor.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = catData.icon,
                                        contentDescription = null,
                                        tint = catData.iconColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = mainCategory.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate900
                                )
                            }
                        }
                    }
                    
                    // Show subcategories
                    subcategoriesMap[mainCategory.id]?.let { subcategories ->
                        items(subcategories) { subCategory ->
                            val subCatData = getCategoryData(subCategory.name)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp)
                                    .clickable { onCategorySelected(subCategory) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedCategory?.id == subCategory.id) Indigo50 else Slate50
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(subCatData.iconColor.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = subCatData.icon,
                                            contentDescription = null,
                                            tint = subCatData.iconColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = subCategory.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (selectedCategory?.id == subCategory.id) FontWeight.SemiBold else FontWeight.Normal,
                                        color = Slate700
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    initialDate: Long
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { date ->
                        onDateSelected(date)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
