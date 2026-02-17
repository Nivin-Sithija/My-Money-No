package com.wiyadama.expensetracker.data.backup

import android.content.Context
import com.wiyadama.expensetracker.data.entity.*
import com.wiyadama.expensetracker.data.repository.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val memberRepository: MemberRepository,
    private val shopRepository: ShopRepository,
    private val transactionRepository: TransactionRepository,
    private val backupMetaRepository: BackupMetaRepository
) {

    suspend fun createBackup(): Result<File> = withContext(Dispatchers.IO) {
        try {
            val backupData = createBackupData()
            val backupFile = saveBackupToFile(backupData)
            
            // Record backup metadata
            val checksum = calculateChecksum(backupFile)
            backupMetaRepository.insertBackupMeta(
                type = "json",
                path = backupFile.absolutePath,
                checksum = checksum,
                size = backupFile.length(),
                appVersion = "1.0.0",
                schemaVersion = backupData.getInt("database_version")
            )
            
            Result.success(backupFile)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun restoreBackup(backupFile: File): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val backupJson = JSONObject(backupFile.readText())
            
            // Restore categories
            val categoriesArray = backupJson.getJSONArray("categories")
            for (i in 0 until categoriesArray.length()) {
                val categoryJson = categoriesArray.getJSONObject(i)
                val category = Category(
                    id = categoryJson.getLong("id"),
                    name = categoryJson.getString("name"),
                    parentId = if (categoryJson.isNull("parentId")) null else categoryJson.getLong("parentId"),
                    isSystem = categoryJson.getBoolean("isSystem"),
                    sortOrder = categoryJson.getInt("sortOrder"),
                    requiresMember = categoryJson.getBoolean("requiresMember"),
                    color = categoryJson.getInt("color"),
                    createdAt = categoryJson.getLong("createdAt"),
                    updatedAt = categoryJson.getLong("updatedAt")
                )
                categoryRepository.insertCategory(category)
            }
            
            // Restore members
            val membersArray = backupJson.getJSONArray("members")
            for (i in 0 until membersArray.length()) {
                val memberJson = membersArray.getJSONObject(i)
                val member = Member(
                    id = memberJson.getLong("id"),
                    name = memberJson.getString("name"),
                    createdAt = memberJson.getLong("createdAt"),
                    updatedAt = memberJson.getLong("updatedAt")
                )
                memberRepository.insertMember(member)
            }
            
            // Restore shops
            val shopsArray = backupJson.getJSONArray("shops")
            for (i in 0 until shopsArray.length()) {
                val shopJson = shopsArray.getJSONObject(i)
                val shop = Shop(
                    id = shopJson.getLong("id"),
                    name = shopJson.getString("name"),
                    address = if (shopJson.isNull("address")) null else shopJson.getString("address"),
                    createdAt = shopJson.getLong("createdAt"),
                    updatedAt = shopJson.getLong("updatedAt")
                )
                shopRepository.insertShop(shop)
            }
            
            // Restore transactions
            val transactionsArray = backupJson.getJSONArray("transactions")
            for (i in 0 until transactionsArray.length()) {
                val transactionJson = transactionsArray.getJSONObject(i)
                val transaction = Transaction(
                    id = transactionJson.getLong("id"),
                    amountCents = transactionJson.getInt("amountCents"),
                    categoryId = transactionJson.getLong("categoryId"),
                    memberId = if (transactionJson.isNull("memberId")) null else transactionJson.getLong("memberId"),
                    shopId = if (transactionJson.isNull("shopId")) null else transactionJson.getLong("shopId"),
                    merchantName = if (transactionJson.isNull("merchantName")) null else transactionJson.getString("merchantName"),
                    notes = if (transactionJson.isNull("notes")) null else transactionJson.getString("notes"),
                    dateTime = transactionJson.getLong("dateTime"),
                    paymentMethod = transactionJson.getString("paymentMethod"),
                    createdAt = transactionJson.getLong("createdAt"),
                    updatedAt = transactionJson.getLong("updatedAt"),
                    deletedAt = if (transactionJson.isNull("deletedAt")) null else transactionJson.getLong("deletedAt")
                )
                transactionRepository.insertTransaction(transaction)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun listBackups(): List<File> = withContext(Dispatchers.IO) {
        val backupDir = File(context.getExternalFilesDir(null), "backups")
        if (!backupDir.exists()) {
            return@withContext emptyList()
        }
        backupDir.listFiles { file -> file.extension == "json" }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    private suspend fun createBackupData(): JSONObject {
        val backup = JSONObject()
        backup.put("version", 1)
        backup.put("timestamp", System.currentTimeMillis())
        backup.put("database_version", 2)
        
        // Backup categories
        val categoriesArray = JSONArray()
        categoryRepository.getAllCategories().first().forEach { category ->
            val categoryJson = JSONObject().apply {
                put("id", category.id)
                put("name", category.name)
                put("parentId", category.parentId)
                put("isSystem", category.isSystem)
                put("sortOrder", category.sortOrder)
                put("requiresMember", category.requiresMember)
                put("color", category.color)
                put("createdAt", category.createdAt)
                put("updatedAt", category.updatedAt)
            }
            categoriesArray.put(categoryJson)
        }
        backup.put("categories", categoriesArray)
        
        // Backup members
        val membersArray = JSONArray()
        memberRepository.getAllMembers().first().forEach { member ->
            val memberJson = JSONObject().apply {
                put("id", member.id)
                put("name", member.name)
                put("createdAt", member.createdAt)
                put("updatedAt", member.updatedAt)
            }
            membersArray.put(memberJson)
        }
        backup.put("members", membersArray)
        
        // Backup shops
        val shopsArray = JSONArray()
        shopRepository.getAllShops().first().forEach { shop ->
            val shopJson = JSONObject().apply {
                put("id", shop.id)
                put("name", shop.name)
                put("address", shop.address)
                put("createdAt", shop.createdAt)
                put("updatedAt", shop.updatedAt)
            }
            shopsArray.put(shopJson)
        }
        backup.put("shops", shopsArray)
        
        // Backup transactions
        val transactionsArray = JSONArray()
        transactionRepository.getAllTransactions().first().forEach { transaction ->
            val transactionJson = JSONObject().apply {
                put("id", transaction.id)
                put("amountCents", transaction.amountCents)
                put("categoryId", transaction.categoryId)
                put("memberId", transaction.memberId)
                put("shopId", transaction.shopId)
                put("merchantName", transaction.merchantName)
                put("notes", transaction.notes)
                put("dateTime", transaction.dateTime)
                put("paymentMethod", transaction.paymentMethod)
                put("createdAt", transaction.createdAt)
                put("updatedAt", transaction.updatedAt)
                put("deletedAt", transaction.deletedAt)
            }
            transactionsArray.put(transactionJson)
        }
        backup.put("transactions", transactionsArray)
        
        return backup
    }

    private fun saveBackupToFile(backupData: JSONObject): File {
        val backupDir = File(context.getExternalFilesDir(null), "backups")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val fileName = "wiyadama_backup_${dateFormat.format(Date())}.json"
        val backupFile = File(backupDir, fileName)
        
        backupFile.writeText(backupData.toString(2))
        return backupFile
    }

    fun getBackupDirectory(): File {
        return File(context.getExternalFilesDir(null), "backups")
    }
    
    private fun calculateChecksum(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = file.readBytes()
        val hash = digest.digest(bytes)
        return hash.joinToString("") { "%02x".format(it) }
    }
}
