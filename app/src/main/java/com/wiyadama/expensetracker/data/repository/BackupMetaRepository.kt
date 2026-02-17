package com.wiyadama.expensetracker.data.repository

import com.wiyadama.expensetracker.data.dao.BackupMetaDao
import com.wiyadama.expensetracker.data.entity.BackupMeta
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupMetaRepository @Inject constructor(
    private val backupMetaDao: BackupMetaDao
) {
    
    fun getAllBackups(): Flow<List<BackupMeta>> {
        return backupMetaDao.getAllBackups()
    }
    
    suspend fun insertBackupMeta(
        type: String,
        path: String,
        checksum: String,
        size: Long,
        appVersion: String,
        schemaVersion: Int
    ): Long {
        val backupMeta = BackupMeta(
            type = type,
            path = path,
            checksum = checksum,
            size = size,
            appVersion = appVersion,
            schemaVersion = schemaVersion
        )
        return backupMetaDao.insertBackupMeta(backupMeta)
    }
    
    suspend fun deleteBackupMeta(id: Long) {
        backupMetaDao.deleteBackupMeta(id)
    }
}
