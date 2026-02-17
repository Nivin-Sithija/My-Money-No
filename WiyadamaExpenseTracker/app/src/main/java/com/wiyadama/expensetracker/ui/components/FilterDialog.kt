package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class TransactionFilters(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val categoryId: Long? = null,
    val memberId: Long? = null,
    val shopId: Long? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentFilters: TransactionFilters,
    categories: List<Category>,
    members: List<Member>,
    shops: List<Shop>,
    onDismiss: () -> Unit,
    onApply: (TransactionFilters) -> Unit
) {
    var filters by remember { mutableStateOf(currentFilters) }
    var showDateRangePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = filters.startDate,
        initialSelectedEndDateMillis = filters.endDate
    )
    
    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        filters = filters.copy(
                            startDate = dateRangePickerState.selectedStartDateMillis,
                            endDate = dateRangePickerState.selectedEndDateMillis
                        )
                        showDateRangePicker = false
                    }
                ) {
                    Text("OK", color = Indigo600)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("Cancel", color = Slate700)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(top = 16.dp),
                title = {
                    Text(
                        text = "Select date range",
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                headline = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                    ) {
                        Text(
                            text = if (dateRangePickerState.selectedStartDateMillis != null) 
                                dateFormatter.format(Date(dateRangePickerState.selectedStartDateMillis!!))
                                else "Start Date",
                             style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = " - ",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = if (dateRangePickerState.selectedEndDateMillis != null) 
                                dateFormatter.format(Date(dateRangePickerState.selectedEndDateMillis!!))
                                else "End Date",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Slate900,
                    headlineContentColor = Slate900,
                    weekdayContentColor = Slate700,
                    subheadContentColor = Slate700,
                    yearContentColor = Slate700,
                    currentYearContentColor = Indigo600,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Indigo600,
                    dayContentColor = Slate900,
                    disabledDayContentColor = Slate300,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Indigo600,
                    todayContentColor = Indigo600,
                    todayDateBorderColor = Indigo600
                )
            )
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = Indigo600,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Filter Transactions",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                    }
                    
                    if (filters != TransactionFilters()) {
                        TextButton(
                            onClick = { filters = TransactionFilters() }
                        ) {
                            Text(
                                text = "Clear All",
                                color = Red600,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date Range
                    FilterSection(
                        title = "Date Range",
                        icon = Icons.Default.CalendarMonth
                    ) {
                        OutlinedButton(
                            onClick = { showDateRangePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when {
                                        filters.startDate != null && filters.endDate != null -> 
                                            "${dateFormatter.format(Date(filters.startDate!!))} - ${dateFormatter.format(Date(filters.endDate!!))}"
                                        else -> "Select date range"
                                    },
                                    color = if (filters.startDate != null) Slate900 else Slate400
                                )
                                Icon(
                                    imageVector = if (filters.startDate != null) Icons.Default.Close else Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Slate400
                                )
                            }
                        }
                        
                        if (filters.startDate != null || filters.endDate != null) {
                            TextButton(
                                onClick = { filters = filters.copy(startDate = null, endDate = null) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Clear dates", color = Red600)
                            }
                        }
                    }
                    
                    // Category Filter
                    FilterSection(
                        title = "Category",
                        icon = Icons.Default.Category
                    ) {
                        var categoryExpanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = categories.find { it.id == filters.categoryId }?.name ?: "All categories",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
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
                                DropdownMenuItem(
                                    text = { Text("All categories") },
                                    onClick = {
                                        filters = filters.copy(categoryId = null)
                                        categoryExpanded = false
                                    }
                                )
                                categories.filter { it.parentId == null }.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            filters = filters.copy(categoryId = category.id)
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Member Filter
                    FilterSection(
                        title = "Member",
                        icon = Icons.Default.Person
                    ) {
                        var memberExpanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = memberExpanded,
                            onExpandedChange = { memberExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = members.find { it.id == filters.memberId }?.name ?: "All members",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = memberExpanded)
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
                                expanded = memberExpanded,
                                onDismissRequest = { memberExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All members") },
                                    onClick = {
                                        filters = filters.copy(memberId = null)
                                        memberExpanded = false
                                    }
                                )
                                members.forEach { member ->
                                    DropdownMenuItem(
                                        text = { Text(member.name) },
                                        onClick = {
                                            filters = filters.copy(memberId = member.id)
                                            memberExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Shop Filter
                    FilterSection(
                        title = "Shop",
                        icon = Icons.Default.Store
                    ) {
                        var shopExpanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = shopExpanded,
                            onExpandedChange = { shopExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = shops.find { it.id == filters.shopId }?.name ?: "All shops",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = shopExpanded)
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
                                expanded = shopExpanded,
                                onDismissRequest = { shopExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All shops") },
                                    onClick = {
                                        filters = filters.copy(shopId = null)
                                        shopExpanded = false
                                    }
                                )
                                shops.forEach { shop ->
                                    DropdownMenuItem(
                                        text = { Text(shop.name) },
                                        onClick = {
                                            filters = filters.copy(shopId = shop.id)
                                            shopExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    Button(
                        onClick = { onApply(filters) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo600
                        )
                    ) {
                        Text(
                            text = "Apply Filters",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Indigo600,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Slate700
            )
        }
        content()
    }
}
