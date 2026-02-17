package com.wiyadama.expensetracker.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val lkrFormatter = NumberFormat.getCurrencyInstance(Locale("en", "LK"))
    
    /**
     * Format cents to LKR currency string
     * @param cents Amount in cents (e.g., 250000 = Rs 2,500.00)
     */
    fun formatLKR(cents: Int): String {
        val amount = cents / 100.0
        return lkrFormatter.format(amount)
    }

    /**
     * Format cents to LKR with custom symbol
     * @param cents Amount in cents
     * @param symbol Currency symbol (default "Rs")
     */
    fun formatWithSymbol(cents: Int, symbol: String = "Rs"): String {
        val amount = cents / 100.0
        return "$symbol ${String.format(Locale.US, "%,.2f", amount)}"
    }

    /**
     * Parse user input to cents
     * @param input User input string (e.g., "2500.50")
     * @return Amount in cents or null if invalid
     */
    fun parseToCents(input: String): Int? {
        return try {
            val cleaned = input.replace(",", "").trim()
            val amount = cleaned.toDoubleOrNull() ?: return null
            if (amount < 0) return null
            (amount * 100).toInt()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Format cents for input display (without symbol)
     * @param cents Amount in cents
     */
    fun formatForInput(cents: Int): String {
        val amount = cents / 100.0
        return String.format(Locale.US, "%.2f", amount)
    }
}
