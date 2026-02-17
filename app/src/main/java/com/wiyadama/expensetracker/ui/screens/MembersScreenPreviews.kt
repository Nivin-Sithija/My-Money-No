package com.wiyadama.expensetracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.ui.theme.*

/**
 * Preview Components for MembersScreen
 * 
 * HOW TO USE IN ANDROID STUDIO:
 * 1. Open this file
 * 2. Click "Split" or "Design" button (top right)
 * 3. See instant previews
 * 4. Edit UI → See changes immediately!
 */

// Preview: Member Detail Screen
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, heightDp = 800)
@Composable
fun PreviewMemberDetailScreen() {
    WiyadamaExpenseTrackerTheme {
        MemberDetailScreen(
            member = Member(
                id = 1,
                name = "Sarah Perera",
                color = 0xFFEC4899.toInt(),
                phone = "+94771234567"
            ),
            totalExpenses = 5400000, // Rs 54,000
            transactionCount = 23,
            transactions = listOf(
                Transaction(
                    id = 1,
                    dateTime = System.currentTimeMillis(),
                    amountCents = 150000,
                    categoryId = 1,
                    memberId = 1,
                    shopId = 1,
                    paymentMethod = "Cash",
                    merchantName = "Weekly Grocery",
                    notes = "Weekly Grocery"
                )
            ),
            categories = listOf(
                Category(id = 1, name = "Food & Dining", color = 0xFFEF4444.toInt())
            ),
            onBack = {},
            onEdit = {},
            onDelete = {}
        )
    }
}

// Preview: Full Members Screen
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, heightDp = 800)
@Composable
fun PreviewMembersScreen() {
    WiyadamaExpenseTrackerTheme {
        MembersScreen(
            members = listOf(
                Member(id = 1, name = "John", color = 0xFF6366F1.toInt()),
                Member(id = 2, name = "Sarah", color = 0xFFEC4899.toInt()),
                Member(id = 3, name = "Mike", color = 0xFF10B981.toInt()),
                Member(id = 4, name = "Lisa", color = 0xFFF59E0B.toInt())
            ),
            shops = listOf(
                Shop(id = 1, name = "Food City", address = "Colombo 07"),
                Shop(id = 2, name = "Keells Super", address = "Nugegoda")
            ),
            transactions = listOf(
                Transaction(
                    id = 1,
                    dateTime = System.currentTimeMillis(),
                    amountCents = 150000,
                    categoryId = 1,
                    memberId = 1,
                    shopId = 1,
                    paymentMethod = "Cash",
                    merchantName = "Weekly Grocery"
                )
            ),
            categories = listOf(
                Category(id = 1, name = "Food & Dining", color = 0xFFEF4444.toInt())
            ),
            onAddMember = {},
            onAddShop = {},
            onMemberClick = {},
            onShopClick = {}
        )
    }
}

// Preview: Shop Detail Screen
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, heightDp = 800)
@Composable
fun PreviewShopDetailScreen() {
    WiyadamaExpenseTrackerTheme {
        ShopDetailScreen(
            shop = Shop(
                id = 1,
                name = "Keells Super",
                address = "Nugegoda"
            ),
            totalExpenses = 2500000,
            transactionCount = 12,
            transactions = listOf(
                Transaction(
                    id = 1,
                    dateTime = System.currentTimeMillis(),
                    amountCents = 250000,
                    categoryId = 1,
                    memberId = 1,
                    shopId = 1,
                    paymentMethod = "Card",
                    merchantName = "Snacks",
                    notes = "Snacks"
                )
            ),
            categories = listOf(
                Category(id = 1, name = "Food & Dining", color = 0xFFEF4444.toInt())
            ),
            onBack = {},
            onEdit = {},
            onDelete = {}
        )
    }
}
