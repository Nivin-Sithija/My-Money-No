package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.wiyadama.expensetracker.ui.theme.*

@Composable
fun PropertySettingsDialog(
    propertyName: String,
    onDismiss: () -> Unit,
    onEditProperty: () -> Unit,
    onEditTenant: () -> Unit,
    onEditRent: () -> Unit,
    onEditImage: () -> Unit,
    onDeleteProperty: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Property Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = propertyName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate600,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Menu Items
                SettingsMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Property Details",
                    subtitle = "Update name, type, and notes",
                    iconColor = Indigo600,
                    onClick = {
                        onEditProperty()
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsMenuItem(
                    icon = Icons.Default.Person,
                    title = "Edit Tenant",
                    subtitle = "Change current tenant name",
                    iconColor = Purple600,
                    onClick = {
                        onEditTenant()
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsMenuItem(
                    icon = Icons.Default.AttachMoney,
                    title = "Edit Monthly Rent",
                    subtitle = "Update rent amount",
                    iconColor = Emerald600,
                    onClick = {
                        onEditRent()
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsMenuItem(
                    icon = Icons.Default.Image,
                    title = "Edit Property Image",
                    subtitle = "Add or change property photo",
                    iconColor = Teal600,
                    onClick = {
                        onEditImage()
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = Slate100)

                Spacer(modifier = Modifier.height(12.dp))

                SettingsMenuItem(
                    icon = Icons.Default.Delete,
                    title = "Delete Property",
                    subtitle = "Remove property and all records",
                    iconColor = Red600,
                    onClick = {
                        onDeleteProperty()
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Slate500
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(20.dp)
        )
    }
}
