package com.wiyadama.expensetracker.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.ui.theme.*
import androidx.compose.ui.unit.dp

/**
 * Preview Components for HomeScreen
 * 
 * HOW TO USE:
 * 1. Open this file in Android Studio
 * 2. Click the "Split" or "Design" button at the top right of the editor
 * 3. You'll see live previews of all components
 * 4. Make changes to the UI and see them instantly!
 * 5. No need to rebuild or install APK
 */

// Preview: Category Card
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
fun PreviewCategoryCard() {
    WiyadamaExpenseTrackerTheme {
        CategoryCardItem(
            category = Category(
                id = 1,
                name = "Food & Dining",
                color = 0xFFF59E0B.toInt()
            ),
            totalExpense = 2500000, // Rs 25,000
            transactionCount = 42,
            onClick = {}
        )
    }
}

// Preview: Recent Transaction Item
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
fun PreviewRecentTransactionItem() {
    WiyadamaExpenseTrackerTheme {
        RecentTransactionItem(
            merchantName = "Keells Super",
            categoryName = "Grocery",
            memberName = "Mom",
            shopName = "Nugegoda",
            notes = "Weekly groceries",
            amount = 850000, // Rs 8,500
            date = System.currentTimeMillis()
        )
    }
}

// Preview: Category Card with different colors
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "Transport Category")
@Composable
fun PreviewTransportCard() {
    WiyadamaExpenseTrackerTheme {
        CategoryCardItem(
            category = Category(
                id = 2,
                name = "Transport",
                color = 0xFF3B82F6.toInt()
            ),
            totalExpense = 1200000, // Rs 12,000
            transactionCount = 15,
            onClick = {}
        )
    }
}

// Preview: Multiple transaction items
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "Transaction List")
@Composable
fun PreviewTransactionList() {
    WiyadamaExpenseTrackerTheme {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            RecentTransactionItem(
                merchantName = "Uber",
                categoryName = "Transport",
                memberName = null,
                shopName = null,
                notes = "Airport ride",
                amount = 250000,
                date = System.currentTimeMillis()
            )
            RecentTransactionItem(
                merchantName = "Pizza Hut",
                categoryName = "Food & Dining",
                memberName = "Dad",
                shopName = "Food City",
                notes = null,
                amount = 320000,
                date = System.currentTimeMillis() - 86400000
            )
            RecentTransactionItem(
                merchantName = "Colombo City Centre",
                categoryName = "Shopping",
                memberName = "Sister",
                shopName = null,
                notes = "New shoes",
                amount = 1500000,
                date = System.currentTimeMillis() - 172800000
            )
        }
    }
}

// Preview: Dark theme (if you want to test dark mode)
@Preview(showBackground = true, backgroundColor = 0xFF1E293B, name = "Dark Theme Card")
@Composable
fun PreviewCategoryCardDark() {
    // Note: You might need to create a dark theme variant
    WiyadamaExpenseTrackerTheme {
        CategoryCardItem(
            category = Category(
                id = 3,
                name = "Entertainment",
                color = 0xFFEC4899.toInt()
            ),
            totalExpense = 500000,
            transactionCount = 8,
            onClick = {}
        )
    }
}
