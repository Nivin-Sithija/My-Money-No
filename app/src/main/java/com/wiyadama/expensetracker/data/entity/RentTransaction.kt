package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rent_transactions",
    foreignKeys = [
        ForeignKey(
            entity = RentalProperty::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["propertyId"]), Index(value = ["dueDate"]), Index(value = ["status"])]
)
data class RentTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val dueDate: Long, // When the rent is due (e.g., 1st of each month)
    val expectedAmount: Int, // Expected rent amount in cents
    val paidAmount: Int = 0, // Amount actually paid in cents (can be partial)
    val status: RentPaymentStatus = RentPaymentStatus.UNPAID,
    val paidDate: Long? = null, // When payment was received
    val notes: String? = null,
    val paymentMethod: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class RentPaymentStatus {
    UNPAID,      // No payment received
    PARTIAL,     // Partial payment received
    PAID,        // Full payment received
    OVERDUE      // Payment is overdue
}
