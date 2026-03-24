package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.entity.Shop
import com.wiyadama.expensetracker.data.repository.MemberRepository
import com.wiyadama.expensetracker.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops: StateFlow<List<Shop>> = _shops.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMembers()
        loadShops()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            _isLoading.value = true
            memberRepository.getAllMembers().collect { memberList ->
                _members.value = memberList
                _isLoading.value = false
            }
        }
    }

    private fun loadShops() {
        viewModelScope.launch {
            shopRepository.getAllShops().collect { shopList ->
                _shops.value = shopList
            }
        }
    }

    fun addMember(name: String, color: Int? = 0xFF6366F1.toInt(), imagePath: String? = null) {
        viewModelScope.launch {
            val member = Member(
                name = name,
                color = color,
                imagePath = imagePath,
                createdAt = System.currentTimeMillis()
            )
            memberRepository.insertMember(member)
        }
    }

    fun updateMember(member: Member) {
        viewModelScope.launch {
            memberRepository.updateMember(member)
        }
    }

    fun deleteMember(memberId: Long) {
        viewModelScope.launch {
            val member = _members.value.find { it.id == memberId }
            member?.let { memberRepository.deleteMember(it) }
        }
    }

    // Shop functions
    fun addShop(name: String, address: String, imagePath: String? = null) {
        viewModelScope.launch {
            val shop = Shop(
                name = name,
                address = address,
                imagePath = imagePath,
                createdAt = System.currentTimeMillis()
            )
            shopRepository.insertShop(shop)
        }
    }

    fun updateShop(shop: Shop) {
        viewModelScope.launch {
            shopRepository.updateShop(shop)
        }
    }

    fun deleteShop(shopId: Long) {
        viewModelScope.launch {
            val shop = _shops.value.find { it.id == shopId }
            shop?.let { shopRepository.deleteShop(it) }
        }
    }
}
