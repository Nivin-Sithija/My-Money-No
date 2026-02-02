package com.wiyadama.expensetracker.ui.theme

import androidx.compose.ui.graphics.Color

// Primary colors matching TSX design
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Gradient colors from TSX (matching the color palette)
val Indigo900 = Color(0xFF312E81)
val Indigo600 = Color(0xFF4F46E5)
val Indigo500 = Color(0xFF6366F1)
val Indigo400 = Color(0xFF818CF8)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo50 = Color(0xFFEEF2FF)

val Purple900 = Color(0xFF581C87)
val Purple600 = Color(0xFF9333EA)
val Purple500 = Color(0xFF8B5CF6)
val Purple400 = Color(0xFFA78BFA)
val Purple50 = Color(0xFFFAF5FF)

val Pink600 = Color(0xFFDB2777)
val Pink500 = Color(0xFFEC4899)
val Pink400 = Color(0xFFF472B6)
val Pink50 = Color(0xFFFDF2F8)

val Orange500 = Color(0xFFF97316)
val Orange400 = Color(0xFFFB923C)
val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Yellow500 = Color(0xFFEAB308)

val Blue500 = Color(0xFF3B82F6)
val Blue400 = Color(0xFF60A5FA)
val Cyan400 = Color(0xFF22D3EE)

val Teal900 = Color(0xFF134E4A)
val Teal600 = Color(0xFF0D9488)
val Teal500 = Color(0xFF14B8A6)
val Teal400 = Color(0xFF2DD4BF)
val Teal50 = Color(0xFFF0FDFA)

val Fuchsia400 = Color(0xFFE879F9)
val Rose400 = Color(0xFFFB7185)
val Red900 = Color(0xFF7F1D1D)
val Red700 = Color(0xFFB91C1C)
val Red600 = Color(0xFFDC2626)
val Red500 = Color(0xFFEF4444)
val Red400 = Color(0xFFF87171)
val Red100 = Color(0xFFFEE2E2)
val Red50 = Color(0xFFFEF2F2)

val Emerald700 = Color(0xFF047857)
val Emerald600 = Color(0xFF059669)
val Emerald500 = Color(0xFF10B981)
val Emerald400 = Color(0xFF34D399)
val Emerald50 = Color(0xFFECFDF5)
val Green500 = Color(0xFF22C55E)

// Slate colors for backgrounds and text
val Slate50 = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

// Category-specific gradient colors
object CategoryColors {
    val FoodDining = listOf(Orange400, Amber400, Yellow500)
    val Transport = listOf(Blue400, Cyan400, Teal500)
    val Shopping = listOf(Purple400, Fuchsia400, Pink500)
    val Bills = listOf(Yellow500, Amber400, Orange400)
    val Entertainment = listOf(Pink400, Rose400, Red500)
    val Travel = listOf(Teal400, Emerald400, Green500)
}

// Background gradients
object BackgroundColors {
    val FoodDiningBg = listOf(Color(0xFFFFF7ED), Color(0xFFFEF3C7))
    val TransportBg = listOf(Color(0xFFEFF6FF), Color(0xFFE0F2FE))
    val ShoppingBg = listOf(Color(0xFFFAF5FF), Color(0xFFFCE7F3))
    val BillsBg = listOf(Color(0xFFFEFCE8), Color(0xFFFEF3C7))
    val EntertainmentBg = listOf(Color(0xFFFDF2F8), Color(0xFFFCE7F3))
    val TravelBg = listOf(Color(0xFFF0FDFA), Color(0xFFCCFBF1))
}
