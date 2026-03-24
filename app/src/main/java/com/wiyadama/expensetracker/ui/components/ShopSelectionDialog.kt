package com.wiyadama.expensetracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.ui.theme.*

@Composable
fun ShopSelectionDialog(
    shops: List<Shop>,
    selectedShop: Shop?,
    onShopSelected: (Shop?) -> Unit,
    onAddNewShop: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Shop",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // None option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onShopSelected(null)
                            onDismiss()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedShop == null) Emerald50 else Color.White
                    ),
                    border = if (selectedShop == null) BorderStroke(2.dp, Emerald600) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Slate200),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = Slate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "None",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selectedShop == null) FontWeight.SemiBold else FontWeight.Normal,
                                color = Slate700
                            )
                        }
                        if (selectedShop == null) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = Emerald600,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Existing shops
                if (shops.isNotEmpty()) {
                    Text(
                        text = "Existing Shops",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(shops) { shop ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onShopSelected(shop)
                                        onDismiss()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedShop != null && selectedShop.id == shop.id) Emerald50 else Color.White
                                ),
                                border = if (selectedShop != null && selectedShop.id == shop.id) BorderStroke(2.dp, Emerald600) else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Emerald600),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Store,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = shop.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (selectedShop != null && selectedShop.id == shop.id) FontWeight.SemiBold else FontWeight.Normal,
                                                color = Slate900
                                            )
                                            if (!shop.address.isNullOrEmpty()) {
                                                Text(
                                                    text = shop.address ?: "",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Slate500
                                                )
                                            }
                                        }
                                    }
                                    if (selectedShop != null && selectedShop.id == shop.id) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = Emerald600,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Add new shop button
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onAddNewShop()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Emerald600)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Emerald600,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Shop",
                        color = Emerald600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate600)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
