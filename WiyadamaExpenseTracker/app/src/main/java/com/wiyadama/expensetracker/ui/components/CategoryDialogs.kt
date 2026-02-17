package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    category: Category? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, color: Int, parentId: Long?) -> Unit,
    parentCategories: List<Category> = emptyList()
) {
    var categoryName by remember { mutableStateOf(category?.name ?: "") }
    var selectedColor by remember { mutableStateOf(Color(category?.color ?: Indigo500.hashCode())) }
    // Only set parent if we are editing and it has a parent, or default to null
    var selectedParent by remember { 
        mutableStateOf(if (category?.parentId != null) parentCategories.find { it.id == category.parentId } else null) 
    }
    var showColorPicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Text(
                    text = if (category == null) "Add Category" else "Edit Category",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                // Category Name
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Indigo600,
                        focusedLabelColor = Indigo600
                    )
                )

                // Color Picker Button
                OutlinedButton(
                    onClick = { showColorPicker = !showColorPicker },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(selectedColor)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Select Color", color = Slate700)
                }

                // Color Picker
                if (showColorPicker) {
                    ColorPicker(
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )
                }


                // Parent Category Selector (optional)
                // Filter out self to prevent circular dependency
                val validParents = parentCategories.filter { it.id != category?.id }
                if (validParents.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedParent?.name ?: "None (Root Category)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Parent Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo600,
                                focusedLabelColor = Indigo600
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("None (Root Category)") },
                                onClick = {
                                    selectedParent = null
                                    expanded = false
                                }
                            )
                            validParents.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedParent = category
                                        expanded = false
                                    }
                                )
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
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (categoryName.isNotBlank()) {
                                onSave(
                                    categoryName,
                                    selectedColor.hashCode(),
                                    selectedParent?.id
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = categoryName.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Indigo500, Purple500, Pink500, Red500, Orange500,
        Amber500, Yellow500, Green500, Teal500,
        Blue500, Slate500
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) }
                    .then(
                        if (color == selectedColor) {
                            Modifier.background(Color.White.copy(alpha = 0.3f))
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (color == selectedColor) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCategoryDialog(
    category: Category,
    transactionCount: Int,
    availableCategories: List<Category>,
    onDismiss: () -> Unit,
    onConfirmDelete: (reassignToCategoryId: Long?) -> Unit
) {
    var selectedReassignCategory by remember { mutableStateOf<Category?>(null) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Red500,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = "Delete Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate600
                        )
                    }
                }

                Divider(color = Slate200)

                // Warning Message
                if (transactionCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Red50
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Red700
                            )
                            Text(
                                text = "This category has $transactionCount transaction(s). You must reassign them to another category before deleting.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Red900
                            )
                        }
                    }

                    // Reassign Category Selector
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedReassignCategory?.name ?: "Select category",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Reassign transactions to") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Indigo600,
                                focusedLabelColor = Indigo600
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            availableCategories
                                .filter { it.id != category.id }
                                .forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.name) },
                                        onClick = {
                                            selectedReassignCategory = cat
                                            expanded = false
                                        }
                                    )
                                }
                        }
                    }
                } else {
                    Text(
                        text = "Are you sure you want to delete this category? This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate600
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (transactionCount == 0 || selectedReassignCategory != null) {
                                onConfirmDelete(selectedReassignCategory?.id)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = transactionCount == 0 || selectedReassignCategory != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Red600)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
