package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incomes")
data class Income(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: Long,
    val amountCents: Int,
    val currency: String = "LKR",
    val categoryType: String, // "HOUSE_RENT", "IET_SALARY", "SOLAR"
    val propertyId: Long? = null, // For house rent - links to RentalProperty
    val notes: String? = null,
    val paymentMethod: String = "Bank Transfer",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
