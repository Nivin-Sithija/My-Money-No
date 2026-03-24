package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter

@Composable
fun RentPaymentDialog(
    propertyName: String,
    expectedAmount: Int,
    currentPaidAmount: Int,
    currentStatus: RentPaymentStatus,
    onDismiss: () -> Unit,
    onMarkPaid: () -> Unit,
    onPartialPayment: (Int) -> Unit,
    onMarkUnpaid: () -> Unit
) {
    var showPartialInput by remember { mutableStateOf(false) }
    var partialAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Payment for $propertyName",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Expected: ${CurrencyFormatter.formatWithSymbol(expectedAmount, "LKR")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
                if (currentPaidAmount > 0) {
                    Text(
                        text = "Paid: ${CurrencyFormatter.formatWithSymbol(currentPaidAmount, "LKR")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Emerald600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (showPartialInput) {
                    OutlinedTextField(
                        value = partialAmount,
                        onValueChange = { partialAmount = it },
                        label = { Text("Amount (LKR)") },
                        placeholder = { Text("Enter amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Emerald600
                            )
                        }
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showPartialInput = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                val amount = (partialAmount.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                                if (amount > 0) {
                                    onPartialPayment(amount)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = partialAmount.toDoubleOrNull() != null && partialAmount.toDoubleOrNull()!! > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
                        ) {
                            Text("Record")
                        }
                    }
                } else {
                    // Quick action buttons
                    Button(
                        onClick = onMarkPaid,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Mark as Fully Paid")
                    }
                    
                    OutlinedButton(
                        onClick = { showPartialInput = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Record Partial Payment")
                    }
                    
                    if (currentStatus != RentPaymentStatus.UNPAID) {
                        OutlinedButton(
                            onClick = onMarkUnpaid,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Red600
                            )
                        ) {
                            Text("Mark as Unpaid")
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!showPartialInput) {
                TextButton(onClick = onDismiss) {
                    Text("Close", color = Slate600)
                }
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
