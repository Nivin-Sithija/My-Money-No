package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.RentalPropertyDao
import com.wiyadama.expensetracker.data.entity.RentalProperty
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RentalPropertyRepository @Inject constructor(
    private val rentalPropertyDao: RentalPropertyDao
) {
    fun getAllProperties(): Flow<List<RentalProperty>> = rentalPropertyDao.getAllProperties()

    fun getPropertiesByType(type: String): Flow<List<RentalProperty>> =
        rentalPropertyDao.getPropertiesByType(type)

    suspend fun getPropertyById(id: Long): RentalProperty? =
        rentalPropertyDao.getPropertyById(id)

    suspend fun insertProperty(property: RentalProperty): Long =
        rentalPropertyDao.insertProperty(property)

    suspend fun updateProperty(property: RentalProperty) =
        rentalPropertyDao.updateProperty(property)

    suspend fun deleteProperty(property: RentalProperty) =
        rentalPropertyDao.deleteProperty(property)
}
