package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["parentId", "sortOrder"]),
        Index(value = ["name"])
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val parentId: Long? = null,
    val isSystem: Boolean = false,
    val sortOrder: Int = 0,
    val requiresMember: Boolean = false, // True for Phone/WiFi under Telephone Bills
    val color: Int = 0xFF6366F1.toInt(), // Default Indigo color
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
