package com.wiyadama.expensetracker.di

import android.content.Context
import com.wiyadama.expensetracker.data.dao.*
import com.wiyadama.expensetracker.data.database.WiyadamaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): WiyadamaDatabase {
        return WiyadamaDatabase.getDatabase(context, scope)
    }

    @Provides
    fun provideMemberDao(database: WiyadamaDatabase): MemberDao {
        return database.memberDao()
    }

    @Provides
    fun provideCategoryDao(database: WiyadamaDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideShopDao(database: WiyadamaDatabase): ShopDao {
        return database.shopDao()
    }

    @Provides
    fun provideTransactionDao(database: WiyadamaDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideBackupMetaDao(database: WiyadamaDatabase): BackupMetaDao {
        return database.backupMetaDao()
    }
}
