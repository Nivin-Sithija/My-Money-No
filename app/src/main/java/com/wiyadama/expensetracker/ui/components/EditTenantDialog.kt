package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.ui.theme.*

@Composable
fun EditTenantDialog(
    currentTenant: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var tenantName by remember { mutableStateOf(currentTenant ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Indigo600
                )
                Text(
                    text = "Edit Tenant",
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
                    text = "Update the current tenant name. This won't affect payment history.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
                
                OutlinedTextField(
                    value = tenantName,
                    onValueChange = { tenantName = it },
                    label = { Text("Tenant Name") },
                    placeholder = { Text("Enter tenant name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Indigo600
                        )
                    },
                    singleLine = true
                )
                
                if (currentTenant != null) {
                    TextButton(
                        onClick = {
                            tenantName = ""
                            onConfirm(null)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Red600
                        )
                    ) {
                        Text("Clear Tenant (Mark as Vacant)")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(if (tenantName.isBlank()) null else tenantName)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
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
