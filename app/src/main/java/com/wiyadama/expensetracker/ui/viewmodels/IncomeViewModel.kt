package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Income
import com.wiyadama.expensetracker.data.entity.RentalProperty
import com.wiyadama.expensetracker.data.repository.IncomeRepository
import com.wiyadama.expensetracker.data.repository.RentalPropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val rentalPropertyRepository: RentalPropertyRepository
) : ViewModel() {

    private val _allIncomes = MutableStateFlow<List<Income>>(emptyList())
    val allIncomes: StateFlow<List<Income>> = _allIncomes.asStateFlow()

    private val _rentalProperties = MutableStateFlow<List<RentalProperty>>(emptyList())
    val rentalProperties: StateFlow<List<RentalProperty>> = _rentalProperties.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            launch {
                incomeRepository.getAllIncomes().collect { incomes ->
                    _allIncomes.value = incomes
                }
            }
            launch {
                rentalPropertyRepository.getAllProperties().collect { properties ->
                    _rentalProperties.value = properties
                }
            }
        }
    }

    fun addIncome(
        amountCents: Int,
        categoryType: String,
        propertyId: Long?,
        notes: String,
        date: Long
    ) {
        viewModelScope.launch {
            val income = Income(
                dateTime = date,
                amountCents = amountCents,
                categoryType = categoryType,
                propertyId = propertyId,
                notes = notes
            )
            incomeRepository.insertIncome(income)
        }
    }

    fun updateIncome(income: Income) {
        viewModelScope.launch {
            incomeRepository.updateIncome(income)
        }
    }

    fun deleteIncome(income: Income) {
        viewModelScope.launch {
            incomeRepository.deleteIncome(income)
        }
    }

    fun addProperty(property: RentalProperty) {
        viewModelScope.launch {
            rentalPropertyRepository.insertProperty(property)
        }
    }

    fun updateProperty(property: RentalProperty) {
        viewModelScope.launch {
            rentalPropertyRepository.updateProperty(property)
        }
    }

    fun deleteProperty(property: RentalProperty) {
        viewModelScope.launch {
            rentalPropertyRepository.deleteProperty(property)
        }
    }

    fun updatePropertyPayment(propertyId: Long, paymentDate: Long, amountCents: Int) {
        viewModelScope.launch {
            val property = rentalPropertyRepository.getPropertyById(propertyId)
            property?.let {
                val updated = it.copy(
                    lastPaidDate = paymentDate,
                    updatedAt = System.currentTimeMillis()
                )
                rentalPropertyRepository.updateProperty(updated)
            }
        }
    }
}
