package com.wiyadama.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "backup_meta")
data class BackupMeta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val type: String, // "json" or "sqlite"
    val path: String,
    val checksum: String, // SHA-256
    val size: Long,
    val appVersion: String,
    val schemaVersion: Int
)
