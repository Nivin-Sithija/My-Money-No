package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Income
import com.wiyadama.expensetracker.data.entity.RentalProperty
import com.wiyadama.expensetracker.data.entity.RentTransaction
import com.wiyadama.expensetracker.data.entity.RentPaymentStatus
import com.wiyadama.expensetracker.data.repository.IncomeRepository
import com.wiyadama.expensetracker.data.repository.RentalPropertyRepository
import com.wiyadama.expensetracker.data.repository.RentTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class PropertyWithTransactions(
    val property: RentalProperty,
    val currentMonthTransaction: RentTransaction?,
    val allTransactions: List<RentTransaction>
)

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val rentalPropertyRepository: RentalPropertyRepository,
    private val rentTransactionRepository: RentTransactionRepository
) : ViewModel() {

    private val _allIncomes = MutableStateFlow<List<Income>>(emptyList())
    val allIncomes: StateFlow<List<Income>> = _allIncomes.asStateFlow()

    private val _rentalProperties = MutableStateFlow<List<RentalProperty>>(emptyList())
    val rentalProperties: StateFlow<List<RentalProperty>> = _rentalProperties.asStateFlow()

    private val _allRentTransactions = MutableStateFlow<List<RentTransaction>>(emptyList())
    val allRentTransactions: StateFlow<List<RentTransaction>> = _allRentTransactions.asStateFlow()

    val propertiesWithTransactions: StateFlow<List<PropertyWithTransactions>> = combine(
        _rentalProperties,
        _allRentTransactions
    ) { properties, transactions ->
        val currentMonth = getCurrentMonthStart()
        properties.map { property ->
            val propertyTransactions = transactions.filter { it.propertyId == property.id }
            val currentTransaction = propertyTransactions.find { 
                it.dueDate >= currentMonth && it.dueDate < getNextMonthStart()
            }
            PropertyWithTransactions(
                property = property,
                currentMonthTransaction = currentTransaction,
                allTransactions = propertyTransactions
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
            launch {
                rentTransactionRepository.getAllTransactions().collect { transactions ->
                    _allRentTransactions.value = transactions
                }
            }
        }
    }

    // Generate rent transaction for current month if it doesn't exist
    fun generateCurrentMonthTransaction(property: RentalProperty) {
        viewModelScope.launch {
            val currentMonth = getCurrentMonthStart()
            val transactions = rentTransactionRepository.getTransactionsByProperty(property.id).first()
            val exists = transactions.any { 
                it.dueDate >= currentMonth && it.dueDate < getNextMonthStart()
            }
            
            if (!exists) {
                val transaction = RentTransaction(
                    propertyId = property.id,
                    dueDate = currentMonth,
                    expectedAmount = property.monthlyRent,
                    paidAmount = 0,
                    status = RentPaymentStatus.UNPAID
                )
                rentTransactionRepository.insert(transaction)
            }
        }
    }

    // Record full payment
    fun recordFullPayment(transactionId: Long, paymentDate: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            val transaction = rentTransactionRepository.getTransactionById(transactionId)
            transaction?.let {
                rentTransactionRepository.updatePaymentStatus(
                    id = transactionId,
                    status = RentPaymentStatus.PAID,
                    paidAmount = it.expectedAmount,
                    paidDate = paymentDate
                )
            }
        }
    }

    // Record partial payment
    fun recordPartialPayment(transactionId: Long, amountCents: Int, paymentDate: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            rentTransactionRepository.recordPayment(transactionId, amountCents, paymentDate)
        }
    }

    // Toggle payment status (for quick mark as paid/unpaid)
    fun togglePaymentStatus(transactionId: Long) {
        viewModelScope.launch {
            val transaction = rentTransactionRepository.getTransactionById(transactionId)
            transaction?.let {
                val newStatus = when (it.status) {
                    RentPaymentStatus.UNPAID, RentPaymentStatus.PARTIAL -> RentPaymentStatus.PAID
                    RentPaymentStatus.PAID -> RentPaymentStatus.UNPAID
                    RentPaymentStatus.OVERDUE -> RentPaymentStatus.PAID
                }
                val newAmount = if (newStatus == RentPaymentStatus.PAID) it.expectedAmount else 0
                val newDate = if (newStatus == RentPaymentStatus.PAID) System.currentTimeMillis() else null
                
                rentTransactionRepository.updatePaymentStatus(
                    id = transactionId,
                    status = newStatus,
                    paidAmount = newAmount,
                    paidDate = newDate
                )
            }
        }
    }

    // Update transaction
    fun updateTransaction(transaction: RentTransaction) {
        viewModelScope.launch {
            rentTransactionRepository.update(transaction)
        }
    }

    // Delete transaction
    fun deleteTransaction(transaction: RentTransaction) {
        viewModelScope.launch {
            rentTransactionRepository.delete(transaction)
        }
    }

    // Non-rent income functions (IET Salary, Solar)
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

    // Property management
    fun addProperty(property: RentalProperty) {
        viewModelScope.launch {
            val propertyId = rentalPropertyRepository.insertProperty(property)
            // Generate current month transaction for new property
            val transaction = RentTransaction(
                propertyId = propertyId,
                dueDate = getCurrentMonthStart(),
                expectedAmount = property.monthlyRent,
                paidAmount = 0,
                status = RentPaymentStatus.UNPAID
            )
            rentTransactionRepository.insert(transaction)
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

    private fun getCurrentMonthStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getNextMonthStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.MONTH, 1)
        return calendar.timeInMillis
    }
}
