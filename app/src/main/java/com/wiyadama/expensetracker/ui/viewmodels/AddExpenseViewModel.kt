package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Category
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.entity.Transaction
import com.wiyadama.expensetracker.data.repository.CategoryRepository
import com.wiyadama.expensetracker.data.repository.MemberRepository
import com.wiyadama.expensetracker.data.repository.ShopRepository
import com.wiyadama.expensetracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val memberRepository: MemberRepository,
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops: StateFlow<List<Shop>> = _shops.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            launch {
                categoryRepository.getAllCategories().collect { categoryList ->
                    _categories.value = categoryList
                }
            }
            launch {
                memberRepository.getAllMembers().collect { memberList ->
                    _members.value = memberList
                }
            }
            launch {
                shopRepository.getAllShops().collect { shopList ->
                    _shops.value = shopList
                }
            }
        }
    }

    fun saveTransaction(
        amountCents: Int,
        categoryId: Long,
        subcategoryId: Long?,
        memberId: Long?,
        shopId: Long?,
        notes: String,
        date: Long
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val transaction = Transaction(
                dateTime = date,
                amountCents = amountCents,
                categoryId = categoryId,
                memberId = memberId,
                shopId = shopId,
                paymentMethod = "Cash",
                notes = notes
            )
            transactionRepository.insertTransaction(transaction)
            _isSaving.value = false
        }
    }

    fun updateTransaction(
        transactionId: Long,
        amountCents: Int,
        categoryId: Long,
        subcategoryId: Long?,
        memberId: Long?,
        shopId: Long?,
        notes: String,
        date: Long
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val transaction = Transaction(
                id = transactionId,
                dateTime = date,
                amountCents = amountCents,
                categoryId = categoryId,
                memberId = memberId,
                shopId = shopId,
                paymentMethod = "Cash",
                notes = notes
            )
            transactionRepository.updateTransaction(transaction)
            _isSaving.value = false
        }
    }

    fun addShop(name: String, address: String = "", imagePath: String? = null, onSuccess: (Shop) -> Unit) {
        viewModelScope.launch {
            val shop = Shop(
                name = name,
                address = address.ifBlank { null },
                imagePath = imagePath,
                createdAt = System.currentTimeMillis()
            )
            val shopId = shopRepository.insertShop(shop)
            onSuccess(shop.copy(id = shopId))
        }
    }
    
    fun addMember(name: String, color: Int, imagePath: String? = null, onSuccess: (Member) -> Unit) {
        viewModelScope.launch {
            val member = Member(
                name = name,
                color = color,
                imagePath = imagePath,
                createdAt = System.currentTimeMillis()
            )
            val memberId = memberRepository.insertMember(member)
            onSuccess(member.copy(id = memberId))
        }
    }
}
