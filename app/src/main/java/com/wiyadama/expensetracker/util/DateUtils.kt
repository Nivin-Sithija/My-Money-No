package com.wiyadama.expensetracker.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private fun getDataFormatter() = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    private fun getTimeFormatter() = SimpleDateFormat("h:mm a", Locale.getDefault())
    private fun getDateTimeFormatter() = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())
    private fun getDayFormatter() = SimpleDateFormat("EEE", Locale.getDefault())
    private fun getCompactDateFormatter() = SimpleDateFormat("MMM dd", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return getDataFormatter().format(Date(timestamp))
    }

    fun formatCompactDate(timestamp: Long): String {
        return getCompactDateFormatter().format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return getTimeFormatter().format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        return getDateTimeFormatter().format(Date(timestamp))
    }

    fun formatDay(timestamp: Long): String {
        return getDayFormatter().format(Date(timestamp))
    }

    fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getEndOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    fun getStartOfWeek(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return getStartOfDay(calendar.timeInMillis)
    }

    fun getStartOfMonth(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return getStartOfDay(calendar.timeInMillis)
    }

    fun getDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return getStartOfDay(calendar.timeInMillis)
    }

    fun isToday(timestamp: Long): Boolean {
        val today = getStartOfDay(System.currentTimeMillis())
        val target = getStartOfDay(timestamp)
        return today == target
    }

    fun isFuture(timestamp: Long): Boolean {
        return timestamp > System.currentTimeMillis()
    }
}
