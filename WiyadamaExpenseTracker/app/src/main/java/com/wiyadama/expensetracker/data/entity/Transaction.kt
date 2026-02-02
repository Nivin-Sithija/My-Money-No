package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Shop::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["dateTime"]),
        Index(value = ["categoryId"]),
        Index(value = ["memberId"]),
        Index(value = ["shopId"]),
        Index(value = ["deletedAt"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: Long,
    val amountCents: Int, // Store amount in cents to avoid floating-point issues
    val currency: String = "LKR",
    val categoryId: Long,
    val memberId: Long? = null,
    val shopId: Long? = null,
    val merchantName: String? = null,
    val paymentMethod: String,
    val notes: String? = null,
    val deletedAt: Long? = null, // Soft delete timestamp
    val revision: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
