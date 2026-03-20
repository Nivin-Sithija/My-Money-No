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
        BackupMeta::class,
        Income::class,
        RentalProperty::class
    ],
    version = 3,
    exportSchema = true
)
abstract class WiyadamaDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun categoryDao(): CategoryDao
    abstract fun shopDao(): ShopDao
    abstract fun transactionDao(): TransactionDao
    abstract fun backupMetaDao(): BackupMetaDao
    abstract fun incomeDao(): IncomeDao
    abstract fun rentalPropertyDao(): RentalPropertyDao

    companion object {
        @Volatile
        private var INSTANCE: WiyadamaDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add color column to categories table with default Indigo500 color
                db.execSQL("ALTER TABLE categories ADD COLUMN color INTEGER NOT NULL DEFAULT ${0xFF6366F1.toInt()}")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create rental_properties table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS rental_properties (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        type TEXT NOT NULL,
                        currentTenant TEXT,
                        monthlyRent INTEGER NOT NULL DEFAULT 0,
                        lastPaidDate INTEGER,
                        advancePayment INTEGER NOT NULL DEFAULT 0,
                        notes TEXT,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())

                // Create incomes table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS incomes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        dateTime INTEGER NOT NULL,
                        amountCents INTEGER NOT NULL,
                        currency TEXT NOT NULL DEFAULT 'LKR',
                        categoryType TEXT NOT NULL,
                        propertyId INTEGER,
                        notes TEXT,
                        paymentMethod TEXT NOT NULL DEFAULT 'Bank Transfer',
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): WiyadamaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WiyadamaDatabase::class.java,
                    "my_money_no.db"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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
            val transactionDao = db.transactionDao()
            val now = System.currentTimeMillis()
            val insertedIds = mutableListOf<Long>()

            fun saveId(id: Long): Long {
                insertedIds.add(id)
                return id
            }

            // 1. Bank Card Payments
            val bankCardsId = saveId(categoryDao.insertCategory(
                Category(name = "Bank Card Payments", isSystem = true, sortOrder = 1, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Amex Card", parentId = bankCardsId, isSystem = true, sortOrder = 1, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Combank Card", parentId = bankCardsId, isSystem = true, sortOrder = 2, createdAt = now, updatedAt = now)
            ))

            // 2. Medical Expenses
            saveId(categoryDao.insertCategory(
                Category(name = "Medical Expenses", isSystem = true, sortOrder = 2, createdAt = now, updatedAt = now)
            ))

            // 3. Transport
            val transportId = saveId(categoryDao.insertCategory(
                Category(name = "Transport", isSystem = true, sortOrder = 3, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Petrol", parentId = transportId, isSystem = true, sortOrder = 1, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Bus Fare", parentId = transportId, isSystem = true, sortOrder = 2, createdAt = now, updatedAt = now)
            ))

            // 4. Bills
            val billsId = saveId(categoryDao.insertCategory(
                Category(name = "Bills", isSystem = true, sortOrder = 4, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Water", parentId = billsId, isSystem = true, sortOrder = 1, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Electricity", parentId = billsId, isSystem = true, sortOrder = 2, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Telecom", parentId = billsId, isSystem = true, sortOrder = 3, createdAt = now, updatedAt = now)
            ))

            // 5. Wifi
            saveId(categoryDao.insertCategory(
                Category(name = "Wifi", isSystem = true, sortOrder = 5, createdAt = now, updatedAt = now)
            ))

            // 6. Telephone
            saveId(categoryDao.insertCategory(
                Category(name = "Telephone", isSystem = true, sortOrder = 6, createdAt = now, updatedAt = now)
            ))

            // 7. Shopping
            saveId(categoryDao.insertCategory(
                Category(name = "Shopping", isSystem = true, sortOrder = 7, createdAt = now, updatedAt = now)
            ))

            // 8. Seafood and Farm Shops
            saveId(categoryDao.insertCategory(
                Category(name = "Seafood and Farm Shops", isSystem = true, sortOrder = 8, createdAt = now, updatedAt = now)
            ))

            // 9. Sithija Boarding Fees
            saveId(categoryDao.insertCategory(
                Category(name = "Sithija Boarding Fees", isSystem = true, sortOrder = 9, createdAt = now, updatedAt = now)
            ))

            // 10. Other
            saveId(categoryDao.insertCategory(
                Category(name = "Other", isSystem = true, sortOrder = 10, createdAt = now, updatedAt = now)
            ))

            // 11. Petty Cash
            saveId(categoryDao.insertCategory(
                Category(name = "Petty Cash", isSystem = true, sortOrder = 11, createdAt = now, updatedAt = now)
            ))

            // 12. Saloon
            saveId(categoryDao.insertCategory(
                Category(name = "Saloon", isSystem = true, sortOrder = 12, createdAt = now, updatedAt = now)
            ))

            // 13. Sports
            val sportsId = saveId(categoryDao.insertCategory(
                Category(name = "Sports", isSystem = true, sortOrder = 13, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Badminton", parentId = sportsId, isSystem = true, sortOrder = 1, createdAt = now, updatedAt = now)
            ))
            saveId(categoryDao.insertCategory(
                Category(name = "Zhumba", parentId = sportsId, isSystem = true, sortOrder = 2, createdAt = now, updatedAt = now)
            ))

            // 14. Food and Dining
            saveId(categoryDao.insertCategory(
                Category(name = "Food and Dining", isSystem = true, sortOrder = 14, createdAt = now, updatedAt = now)
            ))

            // 15. Entertainment
            saveId(categoryDao.insertCategory(
                Category(name = "Entertainment", isSystem = true, sortOrder = 15, createdAt = now, updatedAt = now)
            ))
        }
    }
}
