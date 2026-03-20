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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
    private val incomeViewModel: com.wiyadama.expensetracker.ui.viewmodels.IncomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyMoneyTheme {
                MainApp(
                    homeViewModel = homeViewModel,
                    membersViewModel = membersViewModel,
                    historyViewModel = historyViewModel,
                    addExpenseViewModel = addExpenseViewModel,
                    incomeViewModel = incomeViewModel
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
    addExpenseViewModel: AddExpenseViewModel,
    incomeViewModel: com.wiyadama.expensetracker.ui.viewmodels.IncomeViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddExpense by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var categoryBackStack by remember { mutableStateOf<List<Long>>(emptyList()) }
    var editingTransaction by remember { mutableStateOf<com.wiyadama.expensetracker.data.entity.Transaction?>(null) }
    var showMonthlyExpenses by remember { mutableStateOf(false) }

    val categories by addExpenseViewModel.categories.collectAsStateWithLifecycle()
    val members by addExpenseViewModel.members.collectAsStateWithLifecycle()
    val shops by addExpenseViewModel.shops.collectAsStateWithLifecycle()
    val transactions by historyViewModel.transactions.collectAsStateWithLifecycle()
    val activeFilters by historyViewModel.activeFilters.collectAsStateWithLifecycle()
    val categoriesWithStats by homeViewModel.categoriesWithStats.collectAsStateWithLifecycle()

    var showCategoryManagement by remember { mutableStateOf(false) }

    if (showCategoryManagement) {
        CategoryManagementScreen(
            onBack = { showCategoryManagement = false }
        )
    } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                },
                floatingActionButton = {
                    // Only show FAB on tabs other than Income (tab 1)
                    if (selectedTab != 1) {
                        FloatingActionButton(
                            onClick = { showAddExpense = true },
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(56.dp),
                            shape = RoundedCornerShape(16.dp),
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
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (selectedTab) {
                        0 -> {
                            // Category Detail Screen Logic inside Home Tab
                            if (selectedCategoryId != null) {
                                CategoryDetailScreen(
                                    categoryId = selectedCategoryId!!,
                                    onBack = { 
                                        if (categoryBackStack.size > 1) {
                                            val newStack = categoryBackStack.dropLast(1)
                                            categoryBackStack = newStack
                                            selectedCategoryId = newStack.last()
                                        } else {
                                            selectedCategoryId = null
                                            categoryBackStack = emptyList()
                                        }
                                    },
                                    onSubcategoryClick = { subId ->
                                        categoryBackStack = categoryBackStack + subId
                                        selectedCategoryId = subId
                                    }
                                )
                            } else if (showMonthlyExpenses) {
                                MonthlyExpensesScreen(
                                    transactions = transactions,
                                    onBack = { showMonthlyExpenses = false }
                                )
                            } else {
                                HomeScreen(
                                    onCategoryClick = { categoryId ->
                                        selectedCategoryId = categoryId
                                        categoryBackStack = listOf(categoryId)
                                    },
                                    onSeeAllCategoriesClick = {
                                        showCategoryManagement = true
                                    },
                                    onTotalExpensesClick = {
                                        showMonthlyExpenses = true
                                    }
                                )
                            }
                        }
                        1 -> IncomeScreen(viewModel = incomeViewModel)
                        2 -> AnalyticsScreen()
                        3 -> {
                            var showMemberDialog by remember { mutableStateOf(false) }
                            var showShopDialog by remember { mutableStateOf(false) }
                            var editingMember by remember { mutableStateOf<com.wiyadama.expensetracker.data.entity.Member?>(null) }
                            var viewingMember by remember { mutableStateOf<com.wiyadama.expensetracker.data.entity.Member?>(null) }
                            var viewingShop by remember { mutableStateOf<com.wiyadama.expensetracker.data.entity.Shop?>(null) }
                            
                            if (viewingMember != null) {
                                val memberTransactions = transactions.filter { it.memberId == viewingMember!!.id && it.deletedAt == null }
                                val totalExpenses = memberTransactions.sumOf { it.amountCents }
                                val txCount = memberTransactions.size
                                
                                MemberDetailScreen(
                                    member = viewingMember!!,
                                    transactions = memberTransactions,
                                    categories = categories,
                                    totalExpenses = totalExpenses,
                                    transactionCount = txCount,
                                    onBack = { viewingMember = null },
                                    onEdit = { 
                                        editingMember = viewingMember
                                        showMemberDialog = true 
                                    },
                                    onDelete = {
                                        membersViewModel.deleteMember(viewingMember!!.id)
                                        viewingMember = null
                                    }
                                )
                            } else if (viewingShop != null) {
                                val shopTransactions = transactions.filter { it.shopId == viewingShop!!.id && it.deletedAt == null }
                                val totalExpenses = shopTransactions.sumOf { it.amountCents }
                                val txCount = shopTransactions.size
                                
                                ShopDetailScreen(
                                    shop = viewingShop!!,
                                    transactions = shopTransactions,
                                    categories = categories,
                                    totalExpenses = totalExpenses,
                                    transactionCount = txCount,
                                    onBack = { viewingShop = null },
                                    onEdit = {
                                        // TODO: Implement Shop Edit
                                    },
                                    onDelete = {
                                        // TODO: Implement Shop Delete
                                        viewingShop = null
                                    }
                                )
                            } else {
                                MembersScreen(
                                    members = members,
                                    shops = shops,
                                    transactions = transactions,
                                    categories = categories.map { it },
                                    onAddMember = { 
                                        editingMember = null
                                        showMemberDialog = true 
                                    },
                                    onAddShop = { showShopDialog = true },
                                    onMemberClick = { member -> viewingMember = member },
                                    onShopClick = { shop -> viewingShop = shop }
                                )
                            }
                            
                            if (showMemberDialog) {
                                com.wiyadama.expensetracker.ui.components.MemberDialog(
                                    member = editingMember,
                                    onDismiss = { 
                                        showMemberDialog = false
                                        editingMember = null
                                    },
                                    onConfirm = { name, color, imagePath ->
                                        if (editingMember != null) {
                                            membersViewModel.updateMember(editingMember!!.copy(name = name, color = color, imagePath = imagePath))
                                        } else {
                                            membersViewModel.addMember(name, color, imagePath)
                                        }
                                        showMemberDialog = false
                                        editingMember = null
                                    }
                                )
                            }
                            
                            if (showShopDialog) {
                                com.wiyadama.expensetracker.ui.components.AddShopDialog(
                                    onDismiss = { showShopDialog = false },
                                    onConfirm = { name, address, imagePath ->
                                        addExpenseViewModel.addShop(name, address, imagePath) { _ ->
                                            showShopDialog = false
                                        }
                                    }
                                )
                            }
                        }
                        4 -> HistoryScreen(
                            transactions = transactions,
                            categories = categories.map { it },
                            members = members,
                            shops = shops,
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
            onAddShop = { name, address, imagePath, onSuccess ->
                addExpenseViewModel.addShop(name, address, imagePath, onSuccess)
            },
            onAddMember = { name, color, imagePath, onSuccess ->
                addExpenseViewModel.addMember(name, color, imagePath, onSuccess)
            },
            categories = categories,
            members = members,
            shops = shops,
            editingTransaction = editingTransaction
        )
    }
    
}



@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Indigo600,
                    selectedTextColor = Indigo600,
                    indicatorColor = Indigo50,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                )
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.AttachMoney,
                        contentDescription = "Income",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Income", style = MaterialTheme.typography.labelSmall) },
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Emerald600,
                    selectedTextColor = Emerald600,
                    indicatorColor = Emerald50,
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
                label = { Text("Analytics", style = MaterialTheme.typography.labelSmall) },
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Teal600,
                    selectedTextColor = Teal600,
                    indicatorColor = Teal50,
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
                label = { Text("Members", style = MaterialTheme.typography.labelSmall) },
                selected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple600,
                    selectedTextColor = Purple600,
                    indicatorColor = Purple50,
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
                label = { Text("History", style = MaterialTheme.typography.labelSmall) },
                selected = selectedTab == 4,
                onClick = { onTabSelected(4) },
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
