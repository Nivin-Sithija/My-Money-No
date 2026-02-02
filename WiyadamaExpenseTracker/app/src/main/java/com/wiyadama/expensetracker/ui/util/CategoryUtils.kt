package com.wiyadama.expensetracker.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wiyadama.expensetracker.ui.theme.*

data class CategoryData(
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val bgColors: List<Color>,
    val iconColor: Color
)

fun getCategoryData(name: String): CategoryData {
    return when (name) {
        "Food & Dining", "Grocery" -> CategoryData(
            Icons.Default.Restaurant,
            CategoryColors.FoodDining,
            BackgroundColors.FoodDiningBg,
            Orange400
        )
        "Transport" -> CategoryData(
            Icons.Default.DirectionsCar,
            CategoryColors.Transport,
            BackgroundColors.TransportBg,
            Blue400
        )
        "Shopping" -> CategoryData(
            Icons.Default.ShoppingBag,
            CategoryColors.Shopping,
            BackgroundColors.ShoppingBg,
            Purple400
        )
        "Bills & Utilities", "Telephone Bills", "Electricity", "Water" -> CategoryData(
            Icons.Default.Bolt,
            CategoryColors.Bills,
            BackgroundColors.BillsBg,
            Yellow500
        )
        "Entertainment" -> CategoryData(
            Icons.Default.MusicNote,
            CategoryColors.Entertainment,
            BackgroundColors.EntertainmentBg,
            Pink400
        )
        "Travel" -> CategoryData(
            Icons.Default.Flight,
            CategoryColors.Travel,
            BackgroundColors.TravelBg,
            Teal400
        )
        else -> CategoryData(
            Icons.Default.Category,
            listOf(Slate400, Slate500),
            listOf(Slate50, Slate100),
            Slate600
        )
    }
}
