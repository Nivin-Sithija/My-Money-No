package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
    onAddShop: (name: String, address: String, onSuccess: (Shop) -> Unit) -> Unit,
    onAddMember: (name: String, color: Int, onSuccess: (Member) -> Unit) -> Unit,
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
    
    // Dialog states
    var showMemberDialog by remember { mutableStateOf(false) }
    var showShopDialog by remember { mutableStateOf(false) }
    
    // Categorize categories
    val mainCategories = categories.filter { it.parentId == null }
    val isBillCategory = selectedCategory?.name?.contains("Bill", ignoreCase = true) == true || 
                        selectedCategory?.name in listOf("Bills & Utilities", "Water", "Electricity", "Telephone Bills")

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
                                colors = listOf(Indigo600, Purple600)
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
                            text = "Add Expense",
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

                // Scrollable content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Category Selection - NOW AT TOP
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Category,
                                        contentDescription = null,
                                        tint = Slate600,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Category",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Slate900,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                var categoryExpanded by remember { mutableStateOf(false) }
                                
                                ExposedDropdownMenuBox(
                                    expanded = categoryExpanded,
                                    onExpandedChange = { categoryExpanded = it }
                                ) {
                                    OutlinedTextField(
                                        value = selectedCategory?.name ?: "Select Category",
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                                        },
                                        leadingIcon = selectedCategory?.let { category ->
                                            {
                                                val (icon, _, _, iconColor) = getCategoryData(category.name)
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = iconColor
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Indigo500,
                                            unfocusedBorderColor = Slate200
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = categoryExpanded,
                                        onDismissRequest = { categoryExpanded = false }
                                    ) {
                                        mainCategories.forEach { category ->
                                            val (icon, _, _, iconColor) = getCategoryData(category.name)
                                            DropdownMenuItem(
                                                text = {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = icon,
                                                            contentDescription = null,
                                                            tint = iconColor,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                        Text(category.name)
                                                    }
                                                },
                                                onClick = {
                                                    selectedCategory = category
                                                    categoryExpanded = false
                                                },
                                                leadingIcon = {
                                                    if (selectedCategory?.id == category.id) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = null,
                                                            tint = Indigo600
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Amount Input - NOW BELOW CATEGORY
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Amount",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Slate600,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Slate400
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedTextField(
                                        value = amount,
                                        onValueChange = { 
                                            if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                                                amount = it
                                            }
                                        },
                                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Slate900
                                        ),
                                        placeholder = {
                                            Text(
                                                "0.00",
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = Slate300
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Indigo500,
                                            unfocusedBorderColor = Slate200
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Description
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Slate600,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Slate600,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    placeholder = { Text("What did you spend on?", color = Slate400) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Indigo500,
                                        unfocusedBorderColor = Slate200
                                    )
                                )
                            }
                        }
                    }

                    // Date
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Slate600,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Date",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Slate600
                                        )
                                        Text(
                                            text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate)),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Slate900,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Slate400
                                )
                            }
                        }
                    }

                    // Member Selection
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Slate600,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Assign to Member (Optional)",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Slate600,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    // Add New Member Button
                                    TextButton(
                                        onClick = { showMemberDialog = true },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = Indigo600,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Add New",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Indigo600,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (members.isEmpty()) {
                                        // Empty state
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            color = Slate50,
                                            border = BorderStroke(1.dp, Slate200)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "No members yet. Click 'Add New' to create one.",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Slate500
                                                )
                                            }
                                        }
                                    } else {
                                        members.forEach { member ->
                                            Surface(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { 
                                                        selectedMember = if (selectedMember?.id == member.id) null else member 
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                color = if (selectedMember?.id == member.id) Indigo50 else Slate50,
                                                border = if (selectedMember?.id == member.id) 
                                                    BorderStroke(2.dp, Indigo500) else null
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AccountCircle,
                                                        contentDescription = null,
                                                        tint = if (selectedMember?.id == member.id) Indigo500 else Slate400,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = member.name,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = if (selectedMember?.id == member.id) Indigo900 else Slate900,
                                                        fontWeight = if (selectedMember?.id == member.id) FontWeight.SemiBold else FontWeight.Normal
                                                    )
                                                }
                                            }
                                        }
                                    }
                                
                                }
                            }
                        }
                    }

                    // Shop Selection (only if not a bill category)
                    if (!isBillCategory) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Store,
                                                contentDescription = null,
                                                tint = Slate600,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Shop (Optional)",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Slate600,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        // Add New Shop Button
                                        TextButton(
                                            onClick = { showShopDialog = true },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                tint = Teal600,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Add New",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Teal600,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (shops.isEmpty()) {
                                            // Empty state
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                color = Slate50,
                                                border = BorderStroke(1.dp, Slate200)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(16.dp),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = "No shops yet. Click 'Add New' to create one.",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Slate500
                                                    )
                                                }
                                            }
                                        } else {
                                            shops.take(5).forEach { shop ->
                                            Surface(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { 
                                                        selectedShop = if (selectedShop?.id == shop.id) null else shop 
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                color = if (selectedShop?.id == shop.id) Teal50 else Slate50,
                                                border = if (selectedShop?.id == shop.id) 
                                                    BorderStroke(2.dp, Teal500) else null
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Storefront,
                                                        contentDescription = null,
                                                        tint = if (selectedShop?.id == shop.id) Teal500 else Slate400,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = shop.name,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = if (selectedShop?.id == shop.id) Teal900 else Slate900,
                                                            fontWeight = if (selectedShop?.id == shop.id) FontWeight.SemiBold else FontWeight.Normal
                                                        )
                                                        if (!shop.address.isNullOrEmpty()) {
                                                            Text(
                                                                text = shop.address,
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = Slate500
                                                            )
                                                        }
                                                    } // end Column(modifier = Modifier.weight(1f))
                                                } // end Row(modifier = Modifier.padding(16.dp))
                                            } // end Surface (shop item)
                                        } // end shops.take(5).forEach
                                    } // end else (shops not empty)
                                } // end Column(verticalArrangement = Arrangement.spacedBy(8.dp))
                            } // end Column(modifier = Modifier...padding(20.dp))
                        } // end Card (shop selection card)
                    } // end item
                } // end if (!isBillCategory)
            } // end LazyColumn


            // Bottom Button
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
                        .padding(24.dp),
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
                        enabled = amount.isNotEmpty() && amount.toDoubleOrNull() != null && selectedCategory != null,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo600,
                            disabledContainerColor = Slate300
                        )
                    ) {
                        Text("Add Expense", fontWeight = FontWeight.SemiBold)
                    }
                } // end Row (buttons)
            } // end Surface (bottom button)
        } // end Column (main content)
    } // end Card
} // end Surface (outer)

    // Dialogs
    if (showMemberDialog) {
        MemberDialog(
            onDismiss = { showMemberDialog = false },
            onConfirm = { name, color ->
                onAddMember(name, color) { newMember ->
                    selectedMember = newMember
                    showMemberDialog = false
                }
            }
        )
    }

    if (showShopDialog) {
        AddShopDialog(
            onDismiss = { showShopDialog = false },
            onConfirm = { name, address ->
                onAddShop(name, address) { newShop ->
                    selectedShop = newShop
                    showShopDialog = false
                }
            }
        )
    }
} // end AddExpenseScreen
@Composable
fun CategorySelectCard(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit
) {
    val (icon, gradientColors, bgColors, iconColor) = getCategoryData(category.name)

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Indigo50 else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 4.dp else 2.dp),
        border = if (selected) BorderStroke(2.dp, Indigo500) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(colors = bgColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.name,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) Indigo900 else Slate700,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 2
            )
        }
    }
}
