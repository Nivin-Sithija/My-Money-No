package com.wiyadama.expensetracker.ui.components

import android.net.Uri
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.ui.theme.*

@Composable
fun MemberDialog(
    member: Member? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, color: Int, imagePath: String?) -> Unit
) {
    val context = LocalContext.current
    var memberName by remember { mutableStateOf(member?.name ?: "") }
    // Convert Int color to hex String, default to Indigo if null
    val defaultColorHex = "#6366F1"
    val initialColorHex = member?.color?.let { String.format("#%06X", (0xFFFFFF and it)) } ?: defaultColorHex
    var selectedColor by remember { mutableStateOf(initialColorHex) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(member?.imagePath?.let { Uri.parse(it) }) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                // Permission already granted or not available
            }
        }
        selectedImageUri = uri
    }
    
    // Predefined colors for member selection
    val memberColors = listOf(
        "#6366F1" to "Indigo",
        "#8B5CF6" to "Purple",
        "#EC4899" to "Pink",
        "#EF4444" to "Red",
        "#F59E0B" to "Amber",
        "#10B981" to "Emerald",
        "#14B8A6" to "Teal",
        "#3B82F6" to "Blue",
        "#F97316" to "Orange",
        "#84CC16" to "Lime"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Indigo600,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = if (member == null) "Add Family Member" else "Edit Member",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
                
                // Image Picker
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Slate100)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Member photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Add photo",
                                tint = Slate400,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Text(
                        text = "Tap to ${if (selectedImageUri != null) "change" else "add"} photo",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
                
                // Member Name Input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Member Name *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )
                    OutlinedTextField(
                        value = memberName,
                        onValueChange = { memberName = it },
                        placeholder = { Text("e.g., John, Sarah", color = Slate400) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Indigo500,
                            unfocusedBorderColor = Slate200
                        ),
                        singleLine = true
                    )
                }
                
                // Color Picker
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Choose Color",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(100.dp)
                    ) {
                        items(memberColors.size) { index ->
                            val (colorHex, _) = memberColors[index]
                            val isSelected = selectedColor == colorHex
                            
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(colorHex)))
                                    .border(
                                        width = if (isSelected) 3.0.dp else 0.dp,
                                        color = if (isSelected) Indigo900 else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = colorHex }
                            )
                        }
                    }
                }
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Slate700
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    Button(
                        onClick = { 
                            if (memberName.isNotBlank()) {
                                // Convert hex string to Int color
                                val colorInt = android.graphics.Color.parseColor(selectedColor)
                                onConfirm(memberName.trim(), colorInt, selectedImageUri?.toString())
                            }
                        },
                        enabled = memberName.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo600,
                            disabledContainerColor = Slate200
                        )
                    ) {
                        Text(
                            text = if (member == null) "Add Member" else "Save Changes",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
