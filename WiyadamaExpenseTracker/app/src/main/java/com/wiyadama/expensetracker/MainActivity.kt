package com.wiyadama.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wiyadama.expensetracker.ui.screens.*
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel
import com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel
import com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val membersViewModel: MembersViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val addExpenseViewModel: AddExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            WiyadamaExpenseTrackerTheme {
                MainApp(
                    homeViewModel = homeViewModel,
                    membersViewModel = membersViewModel,
                    historyViewModel = historyViewModel,
                    addExpenseViewModel = addExpenseViewModel
                )
            }
        }
    }
}

@Composable
fun MainApp(
    homeViewModel: HomeViewModel,
    membersViewModel: MembersViewModel,
    historyViewModel: HistoryViewModel,
    addExpenseViewModel: AddExpenseViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddExpense by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var editingTransaction by remember { mutableStateOf<com.wiyadama.expensetracker.data.entity.Transaction?>(null) }

    val categories by addExpenseViewModel.categories.collectAsStateWithLifecycle()
    val members by addExpenseViewModel.members.collectAsStateWithLifecycle()
    val shops by addExpenseViewModel.shops.collectAsStateWithLifecycle()
    val transactions by historyViewModel.transactions.collectAsStateWithLifecycle()
    val activeFilters by historyViewModel.activeFilters.collectAsStateWithLifecycle()
    val categoriesWithStats by homeViewModel.categoriesWithStats.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddExpense = true },
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .size(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 12.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Indigo500, Purple500, Pink500)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Expense",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Slate50,
                                    Color.White,
                                    Color(0xFFF5F3FF)
                                )
                            )
                        )
                        .padding(paddingValues)
                ) {
                    when (selectedTab) {
                        0 -> HomeScreen(
                            onCategoryClick = { categoryId ->
                                selectedCategoryId = categoryId
                            }
                        )
                        1 -> AnalyticsScreen()
                        2 -> {
                            var showAddMemberDialog by remember { mutableStateOf(false) }
                            
                            MembersScreen(
                                members = members,
                                onAddMember = { showAddMemberDialog = true },
                                onMemberClick = { /* TODO: Navigate to member detail */ }
                            )
                            
                            if (showAddMemberDialog) {
                                com.wiyadama.expensetracker.ui.components.AddMemberDialog(
                                    onDismiss = { showAddMemberDialog = false },
                                    onConfirm = { name, color ->
                                        membersViewModel.addMember(name, color)
                                        showAddMemberDialog = false
                                    }
                                )
                            }
                        }
                        3 -> HistoryScreen(
                            transactions = transactions,
                            onTransactionClick = { /* TODO: Navigate to transaction detail */ },
                            onFilterClick = { showFilterDialog = true },
                            onEditTransaction = { transaction ->
                                editingTransaction = transaction
                                showAddExpense = true
                            },
                            onDeleteTransaction = { transaction ->
                                historyViewModel.deleteTransaction(transaction)
                            }
                        )
                    }
                }
            }
        }
    
    // Filter Dialog
    if (showFilterDialog) {
        com.wiyadama.expensetracker.ui.components.FilterDialog(
            currentFilters = activeFilters,
            categories = categories,
            members = members,
            shops = shops,
            onDismiss = { showFilterDialog = false },
            onApply = { filters ->
                historyViewModel.applyFilters(filters)
                showFilterDialog = false
            }
        )
    }

    if (showAddExpense) {
        AddExpenseScreen(
            onDismiss = {
                showAddExpense = false
                editingTransaction = null
            },
            onSave = { amount, categoryId, subcategoryId, memberId, shopId, notes, date ->
                if (editingTransaction != null) {
                    // Update existing transaction
                    addExpenseViewModel.updateTransaction(
                        transactionId = editingTransaction!!.id,
                        amountCents = amount,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        memberId = memberId,
                        shopId = shopId,
                        notes = notes,
                        date = date
                    )
                } else {
                    // Create new transaction
                    addExpenseViewModel.saveTransaction(
                        amountCents = amount,
                        categoryId = categoryId,
                        subcategoryId = subcategoryId,
                        memberId = memberId,
                        shopId = shopId,
                        notes = notes,
                        date = date
                    )
                }
                showAddExpense = false
                editingTransaction = null
            },
            onAddShop = { name, address, onSuccess ->
                addExpenseViewModel.addShop(name, address, onSuccess)
            },
            categories = categories,
            members = members,
            shops = shops,
            editingTransaction = editingTransaction
        )
    }
    
    // Category Detail Screen
    selectedCategoryId?.let { categoryId ->
        CategoryDetailScreen(
            categoryId = categoryId,
            onBack = { selectedCategoryId = null }
        )
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.6f),
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Home", style = MaterialTheme.typography.labelMedium) },
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo500,
                    selectedTextColor = Indigo500,
                    indicatorColor = Color(0xFFEEF2FF),
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                )
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.BarChart,
                        contentDescription = "Analytics",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Analytics", style = MaterialTheme.typography.labelMedium) },
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Teal500,
                    selectedTextColor = Teal500,
                    indicatorColor = Color(0xFFF0FDFA),
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                )
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.People,
                        contentDescription = "Members",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Members", style = MaterialTheme.typography.labelMedium) },
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple500,
                    selectedTextColor = Purple500,
                    indicatorColor = Color(0xFFFAF5FF),
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                )
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.History,
                        contentDescription = "History",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("History", style = MaterialTheme.typography.labelMedium) },
                selected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Slate700,
                    selectedTextColor = Slate700,
                    indicatorColor = Slate100,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                )
            )
        }
    }
}
