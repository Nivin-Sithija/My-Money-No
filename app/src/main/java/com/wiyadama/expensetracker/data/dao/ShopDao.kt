package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.Shop
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops ORDER BY lastUsedAt DESC, name ASC")
    fun getAllShops(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE name LIKE '%' || :query || '%' ORDER BY lastUsedAt DESC LIMIT 10")
    fun searchShops(query: String): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE id = :id")
    suspend fun getShopById(id: Long): Shop?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: Shop): Long

    @Update
    suspend fun updateShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)

    @Query("UPDATE shops SET lastUsedAt = :timestamp WHERE id = :id")
    suspend fun updateLastUsed(id: Long, timestamp: Long = System.currentTimeMillis())
}
