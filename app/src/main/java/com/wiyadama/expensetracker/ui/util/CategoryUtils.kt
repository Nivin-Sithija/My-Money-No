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
        "Bank Card Payments", "Amex Card", "Combank Card" -> CategoryData(
            Icons.Default.CreditCard,
            listOf(Indigo400, Blue400),
            listOf(Indigo50, Slate50),
            Indigo500
        )
        "Medical Expenses" -> CategoryData(
            Icons.Default.LocalHospital,
            listOf(Red400, Rose400),
            listOf(Red50, Red100),
            Red500
        )
        "Transport", "Petrol", "Bus Fare" -> CategoryData(
            Icons.Default.DirectionsCar,
            CategoryColors.Transport,
            BackgroundColors.TransportBg,
            Blue500
        )
        "Bills", "Water", "Electricity", "Telecom" -> CategoryData(
            Icons.Default.Receipt,
            CategoryColors.Bills,
            BackgroundColors.BillsBg,
            Yellow500
        )
        "Wifi" -> CategoryData(
            Icons.Default.Wifi,
            listOf(Teal400, Emerald400),
            listOf(Teal50, Emerald50),
            Teal500
        )
        "Telephone" -> CategoryData(
            Icons.Default.Phone,
            listOf(Emerald400, Green500),
            listOf(Emerald50, Slate50),
            Green500
        )
        "Shopping" -> CategoryData(
            Icons.Default.ShoppingBag,
            CategoryColors.Shopping,
            BackgroundColors.ShoppingBg,
            Purple500
        )
        "Seafood and Farm Shops" -> CategoryData(
            Icons.Default.ShoppingCart,
            listOf(Cyan400, Blue400),
            listOf(Slate50, Slate100),
            Cyan400
        )
        "Sithija Boarding Fees" -> CategoryData(
            Icons.Default.Home,
            listOf(Orange400, Amber400),
            listOf(Color(0xFFFFF7ED), Color(0xFFFEF3C7)),
            Orange500
        )
        "Other" -> CategoryData(
            Icons.Default.MoreHoriz,
            listOf(Slate400, Slate500),
            listOf(Slate50, Slate100),
            Slate500
        )
        "Petty Cash" -> CategoryData(
            Icons.Default.AttachMoney,
            listOf(Emerald500, Teal500),
            listOf(Emerald50, Teal50),
            Emerald500
        )
        "Saloon" -> CategoryData(
            Icons.Default.Face,
            CategoryColors.Entertainment,
            BackgroundColors.EntertainmentBg,
            Pink500
        )
        "Sports", "Badminton", "Zhumba" -> CategoryData(
            Icons.Default.DirectionsRun,
            listOf(Amber400, Yellow500),
            listOf(Color(0xFFFEFCE8), Color(0xFFFEF3C7)),
            Amber500
        )
        "Food and Dining" -> CategoryData(
            Icons.Default.Restaurant,
            CategoryColors.FoodDining,
            BackgroundColors.FoodDiningBg,
            Orange500
        )
        "Entertainment" -> CategoryData(
            Icons.Default.MusicNote,
            listOf(Fuchsia400, Purple400),
            listOf(Purple50, Slate50),
            Fuchsia400
        )
        else -> CategoryData(
            Icons.Default.Category,
            listOf(Slate400, Slate500),
            listOf(Slate50, Slate100),
            Slate600
        )
    }
}
