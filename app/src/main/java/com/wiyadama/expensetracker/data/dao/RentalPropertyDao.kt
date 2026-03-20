package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.RentalProperty
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalPropertyDao {
    @Query("SELECT * FROM rental_properties ORDER BY type, name")
    fun getAllProperties(): Flow<List<RentalProperty>>

    @Query("SELECT * FROM rental_properties WHERE type = :type ORDER BY name")
    fun getPropertiesByType(type: String): Flow<List<RentalProperty>>

    @Query("SELECT * FROM rental_properties WHERE id = :id")
    suspend fun getPropertyById(id: Long): RentalProperty?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: RentalProperty): Long

    @Update
    suspend fun updateProperty(property: RentalProperty)

    @Delete
    suspend fun deleteProperty(property: RentalProperty)
}
