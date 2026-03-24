package com.wiyadama.expensetracker.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.wiyadama.expensetracker.data.entity.RentalProperty
import com.wiyadama.expensetracker.data.entity.RentTransaction
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter
import com.wiyadama.expensetracker.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PropertyDetailScreen(
    property: RentalProperty,
    allTransactions: List<RentTransaction>,
    viewModel: com.wiyadama.expensetracker.ui.viewmodels.IncomeViewModel,
    onBack: () -> Unit,
    onPropertyUpdated: (RentalProperty) -> Unit,
    onDeleteProperty: () -> Unit,
    onRecordPayment: (RentTransaction?) -> Unit,
    onEditPayment: (RentTransaction, Int) -> Unit,
    onDeletePayment: (RentTransaction) -> Unit
) {
    var currentProperty by remember { mutableStateOf(property) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showEditPaymentDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var showEditPropertyDialog by remember { mutableStateOf(false) }
    var showEditTenantDialog by remember { mutableStateOf(false) }
    var showEditRentDialog by remember { mutableStateOf(false) }
    var currentMonthTransaction by remember { mutableStateOf<RentTransaction?>(null) }
    var editingTransaction by remember { mutableStateOf<RentTransaction?>(null) }
    var deletingTransaction by remember { mutableStateOf<RentTransaction?>(null) }
    
    // Update current property when prop changes
    LaunchedEffect(property) {
        currentProperty = property
    }
    
    BackHandler(onBack = onBack)
    
    val paidTransactions = allTransactions.filter { it.status == RentPaymentStatus.PAID }
    val partialTransactions = allTransactions.filter { it.status == RentPaymentStatus.PARTIAL }
    val unpaidTransactions = allTransactions.filter { it.status == RentPaymentStatus.UNPAID || it.status == RentPaymentStatus.OVERDUE }
    
    val totalCollected = paidTransactions.sumOf { it.paidAmount } + partialTransactions.sumOf { it.paidAmount }
    val totalDue = unpaidTransactions.sumOf { it.expectedAmount } + partialTransactions.sumOf { it.expectedAmount - it.paidAmount }
    val missedPayments = unpaidTransactions.size
    
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Payment History", "Missed Payments")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Indigo600, Purple600)
                        )
                    )
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Property Image/Icon
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
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
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = property.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                if (property.currentTenant != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color.White.copy(alpha = 0.9f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = property.currentTenant,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${CurrencyFormatter.formatWithSymbol(property.monthlyRent, "LKR")}/month",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        
                        IconButton(
                            onClick = { showSettingsDialog = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Collected
                StatCard(
                    title = "Total Collected",
                    value = CurrencyFormatter.formatWithSymbol(totalCollected, "LKR"),
                    icon = Icons.Default.CheckCircle,
                    iconColor = Emerald600,
                    modifier = Modifier.weight(1f)
                )
                
                // Total Due
                StatCard(
                    title = "Total Due",
                    value = CurrencyFormatter.formatWithSymbol(totalDue, "LKR"),
                    icon = Icons.Default.Warning,
                    iconColor = Red600,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Paid Payments
                StatCard(
                    title = "Paid",
                    value = "${paidTransactions.size}",
                    icon = Icons.Default.Done,
                    iconColor = Emerald600,
                    modifier = Modifier.weight(1f)
                )
                
                // Missed Payments
                StatCard(
                    title = "Missed",
                    value = "$missedPayments",
                    icon = Icons.Default.Close,
                    iconColor = Red600,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Actions
        item {
            Button(
                onClick = {
                    // Find current month transaction
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val currentMonthStart = calendar.timeInMillis
                    calendar.add(Calendar.MONTH, 1)
                    val nextMonthStart = calendar.timeInMillis
                    
                    currentMonthTransaction = allTransactions.find { tx ->
                        tx.dueDate >= currentMonthStart && tx.dueDate < nextMonthStart
                    }
                    showPaymentDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Record Payment")
            }
        }

        // Tabs
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
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
                                containerColor = if (isSelected) Indigo600 else Slate50,
                                contentColor = if (isSelected) Color.White else Slate700
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (isSelected) 4.dp else 0.dp
                            )
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Overview - Recent transactions
                item {
                    Text(
                        text = "Recent Activity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }
                
                val recentTransactions = allTransactions.sortedByDescending { it.dueDate }.take(5)
                if (recentTransactions.isEmpty()) {
                    item {
                        EmptyState(message = "No transactions yet")
                    }
                } else {
                    items(recentTransactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            onEdit = { tx ->
                                editingTransaction = tx
                                showEditPaymentDialog = true
                            },
                            onDelete = { tx ->
                                deletingTransaction = tx
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
            }
            
            1 -> {
                // Payment History - All paid and partial
                item {
                    Text(
                        text = "All Payments (${paidTransactions.size + partialTransactions.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }
                
                val paymentHistory = (paidTransactions + partialTransactions).sortedByDescending { it.paidDate }
                if (paymentHistory.isEmpty()) {
                    item {
                        EmptyState(message = "No payment history")
                    }
                } else {
                    items(paymentHistory) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            onEdit = { tx ->
                                editingTransaction = tx
                                showEditPaymentDialog = true
                            },
                            onDelete = { tx ->
                                deletingTransaction = tx
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
            }
            
            2 -> {
                // Missed Payments
                item {
                    Text(
                        text = "Unpaid Rent ($missedPayments)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }
                
                if (unpaidTransactions.isEmpty()) {
                    item {
                        EmptyState(message = "No missed payments! 🎉")
                    }
                } else {
                    items(unpaidTransactions.sortedBy { it.dueDate }) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                }
            }
        }
    }
    
    if (showSettingsDialog) {
        com.wiyadama.expensetracker.ui.components.PropertySettingsDialog(
            propertyName = currentProperty.name,
            onDismiss = { showSettingsDialog = false },
            onEditProperty = { 
                showEditPropertyDialog = true
                showSettingsDialog = false
            },
            onEditTenant = { 
                showEditTenantDialog = true
                showSettingsDialog = false
            },
            onEditRent = { 
                showEditRentDialog = true
                showSettingsDialog = false
            },
            onEditImage = { 
                showImagePickerDialog = true
                showSettingsDialog = false
            },
            onDeleteProperty = onDeleteProperty
        )
    }
    
    if (showImagePickerDialog) {
        com.wiyadama.expensetracker.ui.components.ImagePickerDialog(
            currentImagePath = currentProperty.imagePath,
            onDismiss = { showImagePickerDialog = false },
            onImageSelected = { imagePath ->
                val updatedProperty = currentProperty.copy(imagePath = imagePath)
                viewModel.updateProperty(updatedProperty)
                currentProperty = updatedProperty
                onPropertyUpdated(updatedProperty)
                showImagePickerDialog = false
            }
        )
    }
    
    if (showEditPropertyDialog) {
        AddPropertyDialog(
            property = currentProperty,
            onDismiss = { showEditPropertyDialog = false },
            onConfirm = { updatedProperty ->
                viewModel.updateProperty(updatedProperty)
                currentProperty = updatedProperty
                onPropertyUpdated(updatedProperty)
                showEditPropertyDialog = false
            }
        )
    }
    
    if (showEditTenantDialog) {
        com.wiyadama.expensetracker.ui.components.EditTenantDialog(
            currentTenant = currentProperty.currentTenant,
            onDismiss = { showEditTenantDialog = false },
            onConfirm = { newTenant ->
                val updatedProperty = currentProperty.copy(currentTenant = newTenant)
                viewModel.updateProperty(updatedProperty)
                currentProperty = updatedProperty
                onPropertyUpdated(updatedProperty)
                showEditTenantDialog = false
            }
        )
    }
    
    if (showEditRentDialog) {
        com.wiyadama.expensetracker.ui.components.EditRentAmountDialog(
            currentRent = currentProperty.monthlyRent,
            onDismiss = { showEditRentDialog = false },
            onConfirm = { newRent ->
                val updatedProperty = currentProperty.copy(monthlyRent = newRent)
                viewModel.updateProperty(updatedProperty)
                currentProperty = updatedProperty
                onPropertyUpdated(updatedProperty)
                showEditRentDialog = false
            }
        )
    }

    if (showPaymentDialog) {
        com.wiyadama.expensetracker.ui.components.RentPaymentDialog(
            propertyName = property.name,
            expectedAmount = property.monthlyRent,
            currentPaidAmount = currentMonthTransaction?.paidAmount ?: 0,
            currentStatus = currentMonthTransaction?.status ?: RentPaymentStatus.UNPAID,
            onDismiss = {
                showPaymentDialog = false
                currentMonthTransaction = null
            },
            onMarkPaid = {
                currentMonthTransaction?.let { tx ->
                    onRecordPayment(tx)
                }
                showPaymentDialog = false
                currentMonthTransaction = null
            },
            onPartialPayment = { amount ->
                currentMonthTransaction?.let { tx ->
                    onEditPayment(tx, amount)
                }
                showPaymentDialog = false
                currentMonthTransaction = null
            },
            onMarkUnpaid = {
                currentMonthTransaction?.let { tx ->
                    onEditPayment(tx, 0)
                }
                showPaymentDialog = false
                currentMonthTransaction = null
            }
        )
    }

    if (showEditPaymentDialog && editingTransaction != null) {
        com.wiyadama.expensetracker.ui.components.EditPaymentDialog(
            transaction = editingTransaction!!,
            onDismiss = {
                showEditPaymentDialog = false
                editingTransaction = null
            },
            onConfirm = { newAmount ->
                onEditPayment(editingTransaction!!, newAmount)
                showEditPaymentDialog = false
                editingTransaction = null
            }
        )
    }

    if (showDeleteConfirmDialog && deletingTransaction != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                deletingTransaction = null
            },
            title = {
                Text(
                    text = "Delete Payment?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to delete this payment record? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeletePayment(deletingTransaction!!)
                        showDeleteConfirmDialog = false
                        deletingTransaction = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red600)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmDialog = false
                        deletingTransaction = null
                    }
                ) {
                    Text("Cancel", color = Slate600)
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Slate500,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Slate900,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TransactionCard(
    transaction: RentTransaction,
    onEdit: ((RentTransaction) -> Unit)? = null,
    onDelete: ((RentTransaction) -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Status Icon
                val (statusIcon, statusColor) = when (transaction.status) {
                    RentPaymentStatus.PAID -> Icons.Default.CheckCircle to Emerald600
                    RentPaymentStatus.PARTIAL -> Icons.Default.Schedule to Color(0xFFB45309)
                    RentPaymentStatus.OVERDUE -> Icons.Default.Error to Red600
                    RentPaymentStatus.UNPAID -> Icons.Default.Circle to Slate400
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Column {
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(transaction.dueDate)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                    
                    val statusText = when (transaction.status) {
                        RentPaymentStatus.PAID -> "Paid on ${transaction.paidDate?.let { DateUtils.formatDate(it) } ?: ""}"
                        RentPaymentStatus.PARTIAL -> "${CurrencyFormatter.formatWithSymbol(transaction.paidAmount, "LKR")} of ${CurrencyFormatter.formatWithSymbol(transaction.expectedAmount, "LKR")}"
                        RentPaymentStatus.OVERDUE -> "Overdue since ${DateUtils.formatDate(transaction.dueDate)}"
                        RentPaymentStatus.UNPAID -> "Due ${DateUtils.formatDate(transaction.dueDate)}"
                    }
                    
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (transaction.status) {
                            RentPaymentStatus.PAID -> CurrencyFormatter.formatWithSymbol(transaction.paidAmount, "LKR")
                            RentPaymentStatus.PARTIAL -> CurrencyFormatter.formatWithSymbol(transaction.paidAmount, "LKR")
                            else -> CurrencyFormatter.formatWithSymbol(transaction.expectedAmount, "LKR")
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = when (transaction.status) {
                            RentPaymentStatus.PAID -> Emerald600
                            RentPaymentStatus.PARTIAL -> Color(0xFFB45309)
                            else -> Red600
                        }
                    )
                    
                    if (onEdit != null && onDelete != null && (transaction.status == RentPaymentStatus.PAID || transaction.status == RentPaymentStatus.PARTIAL)) {
                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options",
                                    tint = Slate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit Payment") },
                                    onClick = {
                                        onEdit(transaction)
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, contentDescription = null, tint = Indigo600)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete Payment") },
                                    onClick = {
                                        onDelete(transaction)
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Red600)
                                    }
                                )
                            }
                        }
                    }
                }
                
                val (statusText, statusBgColor, statusTextColor) = when (transaction.status) {
                    RentPaymentStatus.PAID -> Triple("Paid", Emerald50, Emerald700)
                    RentPaymentStatus.PARTIAL -> Triple("Partial", Color(0xFFFEF3C7), Color(0xFFB45309))
                    RentPaymentStatus.OVERDUE -> Triple("Overdue", Red100, Red700)
                    RentPaymentStatus.UNPAID -> Triple("Unpaid", Slate100, Slate700)
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
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
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
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = Slate300,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = Slate500
            )
        }
    }
}
