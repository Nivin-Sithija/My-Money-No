package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental_properties")
data class RentalProperty(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g., "Shop 1", "House 1"
    val type: String, // "SHOP" or "HOUSE"
    val currentTenant: String? = null,
    val monthlyRent: Int = 0, // in cents
    val lastPaidDate: Long? = null,
    val advancePayment: Int = 0, // in cents
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
