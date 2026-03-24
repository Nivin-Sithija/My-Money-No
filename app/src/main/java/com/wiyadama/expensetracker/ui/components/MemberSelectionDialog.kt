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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.ui.theme.*

@Composable
fun MemberSelectionDialog(
    members: List<Member>,
    selectedMember: Member?,
    onMemberSelected: (Member?) -> Unit,
    onAddNewMember: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Member",
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
                            onMemberSelected(null)
                            onDismiss()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedMember == null) Indigo50 else Color.White
                    ),
                    border = if (selectedMember == null) BorderStroke(2.dp, Indigo600) else null
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
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Slate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "None",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selectedMember == null) FontWeight.SemiBold else FontWeight.Normal,
                                color = Slate700
                            )
                        }
                        if (selectedMember == null) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = Indigo600,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Existing members
                if (members.isNotEmpty()) {
                    Text(
                        text = "Existing Members",
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
                        items(members) { member ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onMemberSelected(member)
                                        onDismiss()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedMember != null && selectedMember.id == member.id) Indigo50 else Color.White
                                ),
                                border = if (selectedMember != null && selectedMember.id == member.id) BorderStroke(2.dp, Indigo600) else null
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
                                                .background(Color(member.color ?: 0xFF6366F1.toInt())),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = member.name.first().uppercase(),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = member.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (selectedMember != null && selectedMember.id == member.id) FontWeight.SemiBold else FontWeight.Normal,
                                            color = Slate900
                                        )
                                    }
                                    if (selectedMember != null && selectedMember.id == member.id) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = Indigo600,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Add new member button
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onAddNewMember()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Indigo600)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Indigo600,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Member",
                        color = Indigo600,
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
