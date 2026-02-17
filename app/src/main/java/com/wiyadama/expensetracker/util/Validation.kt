package com.wiyadama.expensetracker.util

object Validation {
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateAmount(cents: Int?): ValidationResult {
        return when {
            cents == null -> ValidationResult(false, "Amount is required")
            cents <= 0 -> ValidationResult(false, "Amount must be greater than zero")
            else -> ValidationResult(true)
        }
    }

    fun validateCategory(categoryId: Long?): ValidationResult {
        return when {
            categoryId == null || categoryId <= 0 -> ValidationResult(false, "Category is required")
            else -> ValidationResult(true)
        }
    }

    fun validateMemberRequired(memberId: Long?, requiresMember: Boolean): ValidationResult {
        return when {
            requiresMember && (memberId == null || memberId <= 0) ->
                ValidationResult(false, "Member is required for this category")
            else -> ValidationResult(true)
        }
    }

    fun validateDateTime(timestamp: Long, allowFuture: Boolean = false): ValidationResult {
        return when {
            !allowFuture && timestamp > System.currentTimeMillis() ->
                ValidationResult(false, "Future dates are not allowed")
            else -> ValidationResult(true)
        }
    }

    fun validateName(name: String?, fieldName: String = "Name"): ValidationResult {
        return when {
            name.isNullOrBlank() -> ValidationResult(false, "$fieldName is required")
            name.length < 2 -> ValidationResult(false, "$fieldName must be at least 2 characters")
            else -> ValidationResult(true)
        }
    }

    fun validatePaymentMethod(method: String?): ValidationResult {
        return when {
            method.isNullOrBlank() -> ValidationResult(false, "Payment method is required")
            else -> ValidationResult(true)
        }
    }
}
