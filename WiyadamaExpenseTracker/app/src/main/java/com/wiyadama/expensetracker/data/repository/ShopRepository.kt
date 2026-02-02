package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.ShopDao
import com.wiyadama.expensetracker.data.entity.Shop
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val shopDao: ShopDao
) {
    fun getAllShops(): Flow<List<Shop>> = shopDao.getAllShops()

    fun searchShops(query: String): Flow<List<Shop>> = shopDao.searchShops(query)

    suspend fun getShopById(id: Long): Shop? = shopDao.getShopById(id)

    suspend fun insertShop(shop: Shop): Long = shopDao.insertShop(shop)

    suspend fun updateShop(shop: Shop) = shopDao.updateShop(shop)

    suspend fun deleteShop(shop: Shop) = shopDao.deleteShop(shop)

    suspend fun updateLastUsed(id: Long) = shopDao.updateLastUsed(id)
}
