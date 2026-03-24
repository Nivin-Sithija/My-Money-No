package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.ui.theme.*
import com.wiyadama.expensetracker.util.CurrencyFormatter

@Composable
fun EditRentAmountDialog(
    currentRent: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var rentAmount by remember { mutableStateOf((currentRent / 100.0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null,
                    tint = Emerald600
                )
                Text(
                    text = "Edit Monthly Rent",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Update the monthly rent amount. This will apply to future rent transactions only.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Slate50),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Current Rent",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = CurrencyFormatter.formatWithSymbol(currentRent, "LKR"),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )
                    }
                }
                
                OutlinedTextField(
                    value = rentAmount,
                    onValueChange = { rentAmount = it },
                    label = { Text("New Rent Amount (LKR)") },
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
                    isError = rentAmount.toDoubleOrNull() == null || rentAmount.toDoubleOrNull()!! <= 0
                )
                
                if (rentAmount.toDoubleOrNull() != null && rentAmount.toDoubleOrNull()!! > 0) {
                    val newAmount = (rentAmount.toDouble() * 100).toInt()
                    Text(
                        text = "New rent: ${CurrencyFormatter.formatWithSymbol(newAmount, "LKR")}/month",
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
                    val amount = (rentAmount.toDoubleOrNull()?.times(100))?.toInt() ?: 0
                    if (amount > 0) {
                        onConfirm(amount)
                    }
                },
                enabled = rentAmount.toDoubleOrNull() != null && rentAmount.toDoubleOrNull()!! > 0,
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
