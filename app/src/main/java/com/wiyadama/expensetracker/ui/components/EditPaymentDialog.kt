package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.data.entity.RentTransaction
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditPaymentDialog(
    transaction: RentTransaction,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var paidAmount by remember { mutableStateOf((transaction.paidAmount / 100.0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Edit Payment",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(transaction.dueDate)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Slate50),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Expected Amount",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(transaction.expectedAmount, "LKR"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                    }
                }
                
                OutlinedTextField(
                    value = paidAmount,
                    onValueChange = { paidAmount = it },
                    label = { Text("Paid Amount (LKR)") },
                    placeholder = { Text("Enter amount") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = Emerald600
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = paidAmount.toDoubleOrNull() == null || paidAmount.toDoubleOrNull()!! <= 0
                )
                
                if (paidAmount.toDoubleOrNull() != null && paidAmount.toDoubleOrNull()!! > 0) {
                    val newAmount = (paidAmount.toDouble() * 100).toInt()
                    Text(
                        text = "Updated amount: ${CurrencyFormatter.formatWithSymbol(newAmount, "LKR")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Emerald600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = (paidAmount.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                    if (amount > 0) {
                        onConfirm(amount)
                    }
                },
                enabled = paidAmount.toDoubleOrNull() != null && paidAmount.toDoubleOrNull()!! > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate600)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
