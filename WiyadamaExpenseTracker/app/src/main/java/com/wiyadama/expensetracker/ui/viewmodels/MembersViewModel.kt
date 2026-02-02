package com.wiyadama.expensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiyadama.expensetracker.data.entity.Member
import com.wiyadama.expensetracker.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMembers()
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

    fun addMember(name: String, color: String = "#6366F1") {
        viewModelScope.launch {
            val member = Member(
                name = name,
                color = color,
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
}
