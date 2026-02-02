package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.MemberDao
import com.wiyadama.expensetracker.data.entity.Member
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun getMemberById(id: Long): Member? = memberDao.getMemberById(id)

    fun getMemberByIdFlow(id: Long): Flow<Member?> = memberDao.getMemberByIdFlow(id)

    suspend fun insertMember(member: Member): Long = memberDao.insertMember(member)

    suspend fun updateMember(member: Member) = memberDao.updateMember(member)

    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)

    suspend fun getMemberCount(): Int = memberDao.getMemberCount()
}
