package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.activity.compose.BackHandler
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.ui.components.CategoryDialog
import com.wiyadama.expensetracker.ui.components.DeleteCategoryDialog
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: CategoryManagementViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }
    var deleteTransactionCount by remember { mutableIntStateOf(0) }

    // Handling Delete Click with count fetching
    val coroutineScope = rememberCoroutineScope()
    
    // Helper to fetch count and show dialog
    fun prepareDelete(category: Category) {
        coroutineScope.launch {
            val count = viewModel.getTransactionCount(category.id)
            deleteTransactionCount = count
            showDeleteDialog = category
        }
    }

    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Slate900,
                    navigationIconContentColor = Slate900
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    editingCategory = null
                    showCategoryDialog = true 
                },
                containerColor = Indigo600,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        },
        containerColor = Slate50
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            val rootCategories = categories.filter { it.parentId == null }
            items(rootCategories) { category ->
                Column {
                    CategoryItem(
                        category = category,
                        onEdit = {
                            editingCategory = category
                            showCategoryDialog = true
                        },
                        onDelete = {
                            prepareDelete(category)
                        }
                    )
                    
                    val subcategories = categories.filter { it.parentId == category.id }
                    if (subcategories.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(start = 32.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            subcategories.forEach { sub ->
                                CategoryItem(
                                    category = sub,
                                    onEdit = {
                                        editingCategory = sub
                                        showCategoryDialog = true
                                    },
                                    onDelete = {
                                        prepareDelete(sub)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showCategoryDialog) {
        CategoryDialog(
            category = editingCategory,
            onDismiss = { 
                showCategoryDialog = false
                editingCategory = null
            },
            onSave = { name, color, parentId ->
                if (editingCategory != null) {
                    viewModel.updateCategory(editingCategory!!.copy(
                        name = name, 
                        color = color, 
                        parentId = parentId
                    ))
                } else {
                    viewModel.addCategory(name, color, parentId)
                }
                showCategoryDialog = false
                editingCategory = null
            },
            parentCategories = categories
        )
    }
    
    if (showDeleteDialog != null) {
        DeleteCategoryDialog(
            category = showDeleteDialog!!,
            transactionCount = deleteTransactionCount,
            availableCategories = categories,
            onDismiss = { showDeleteDialog = null },
            onConfirmDelete = { reassignId ->
                viewModel.deleteCategory(showDeleteDialog!!.id, reassignId)
                showDeleteDialog = null
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(category.color).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // Should show icon based on name, for now existing logic or generic
                Icon(
                    imageVector = Icons.Default.Category, // TODO: Map icon string to Vector
                    contentDescription = null,
                    tint = Color(category.color)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate900
                )
//                if (category.parentId != null) {
//                    Text(
//                        text = "Subcategory", // Could resolve parent name if passed
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Slate500
//                    )
//                }
            }
            
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Slate400)
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Red400)
            }
        }
    }
}
