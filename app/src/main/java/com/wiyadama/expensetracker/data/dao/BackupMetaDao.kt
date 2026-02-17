package com.wiyadama.expensetracker.data.dao

import androidx.room.*
import com.wiyadama.expensetracker.data.entity.BackupMeta
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupMetaDao {
    @Query("SELECT * FROM backup_meta ORDER BY createdAt DESC")
    fun getAllBackups(): Flow<List<BackupMeta>>

    @Query("SELECT * FROM backup_meta WHERE type = :type ORDER BY createdAt DESC LIMIT :limit")
    fun getBackupsByType(type: String, limit: Int = 30): Flow<List<BackupMeta>>

    @Insert
    suspend fun insertBackupMeta(backupMeta: BackupMeta): Long

    @Query("DELETE FROM backup_meta WHERE createdAt < :beforeTimestamp")
    suspend fun deleteOldBackups(beforeTimestamp: Long)

    @Query("DELETE FROM backup_meta WHERE id = :id")
    suspend fun deleteBackupMeta(id: Long)
}
