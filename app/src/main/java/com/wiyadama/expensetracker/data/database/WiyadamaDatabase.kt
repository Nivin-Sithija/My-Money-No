package com.wiyadama.expensetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wiyadama.expensetracker.data.dao.*
import com.wiyadama.expensetracker.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Member::class,
        Category::class,
        Shop::class,
        Transaction::class,
        BackupMeta::class
    ],
    version = 2,
    exportSchema = true
)
abstract class WiyadamaDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun categoryDao(): CategoryDao
    abstract fun shopDao(): ShopDao
    abstract fun transactionDao(): TransactionDao
    abstract fun backupMetaDao(): BackupMetaDao

    companion object {
        @Volatile
        private var INSTANCE: WiyadamaDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add color column to categories table with default Indigo500 color
                db.execSQL("ALTER TABLE categories ADD COLUMN color INTEGER NOT NULL DEFAULT ${0xFF6366F1.toInt()}")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): WiyadamaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WiyadamaDatabase::class.java,
                    "wiyadama_expense_tracker.db"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(SeedDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    seedDatabase(database)
                }
            }
        }

        suspend fun seedDatabase(db: WiyadamaDatabase) {
            val categoryDao = db.categoryDao()
            val now = System.currentTimeMillis()

            // Seed root categories
            val billsId = categoryDao.insertCategory(
                Category(
                    name = "Bills & Utilities",
                    isSystem = true,
                    sortOrder = 1,
                    createdAt = now,
                    updatedAt = now
                )
            )

            val telephoneId = categoryDao.insertCategory(
                Category(
                    name = "Telephone Bills",
                    isSystem = true,
                    sortOrder = 2,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Food & Dining",
                    isSystem = true,
                    sortOrder = 3,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Transport",
                    isSystem = true,
                    sortOrder = 4,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Shopping",
                    isSystem = true,
                    sortOrder = 5,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Grocery",
                    isSystem = true,
                    sortOrder = 6,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Entertainment",
                    isSystem = true,
                    sortOrder = 7,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Travel",
                    isSystem = true,
                    sortOrder = 8,
                    createdAt = now,
                    updatedAt = now
                )
            )

            // Seed Bills & Utilities subcategories
            categoryDao.insertCategory(
                Category(
                    name = "Water",
                    parentId = billsId,
                    isSystem = true,
                    sortOrder = 1,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Electricity",
                    parentId = billsId,
                    isSystem = true,
                    sortOrder = 2,
                    createdAt = now,
                    updatedAt = now
                )
            )

            // Seed Telephone Bills subcategories
            categoryDao.insertCategory(
                Category(
                    name = "TV",
                    parentId = telephoneId,
                    isSystem = true,
                    sortOrder = 1,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Phone",
                    parentId = telephoneId,
                    isSystem = true,
                    requiresMember = true, // Member required
                    sortOrder = 2,
                    createdAt = now,
                    updatedAt = now
                )
            )

            categoryDao.insertCategory(
                Category(
                    name = "Wi-Fi",
                    parentId = telephoneId,
                    isSystem = true,
                    requiresMember = true, // Member required
                    sortOrder = 3,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }
}
