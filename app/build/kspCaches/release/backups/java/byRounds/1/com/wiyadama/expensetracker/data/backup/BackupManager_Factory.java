package com.wiyadama.expensetracker.data.backup;

import android.content.Context;
import com.wiyadama.expensetracker.data.repository.BackupMetaRepository;
import com.wiyadama.expensetracker.data.repository.CategoryRepository;
import com.wiyadama.expensetracker.data.repository.MemberRepository;
import com.wiyadama.expensetracker.data.repository.ShopRepository;
import com.wiyadama.expensetracker.data.repository.TransactionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class BackupManager_Factory implements Factory<BackupManager> {
  private final Provider<Context> contextProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<MemberRepository> memberRepositoryProvider;

  private final Provider<ShopRepository> shopRepositoryProvider;

  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<BackupMetaRepository> backupMetaRepositoryProvider;

  public BackupManager_Factory(Provider<Context> contextProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<BackupMetaRepository> backupMetaRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.memberRepositoryProvider = memberRepositoryProvider;
    this.shopRepositoryProvider = shopRepositoryProvider;
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.backupMetaRepositoryProvider = backupMetaRepositoryProvider;
  }

  @Override
  public BackupManager get() {
    return newInstance(contextProvider.get(), categoryRepositoryProvider.get(), memberRepositoryProvider.get(), shopRepositoryProvider.get(), transactionRepositoryProvider.get(), backupMetaRepositoryProvider.get());
  }

  public static BackupManager_Factory create(Provider<Context> contextProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider,
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<BackupMetaRepository> backupMetaRepositoryProvider) {
    return new BackupManager_Factory(contextProvider, categoryRepositoryProvider, memberRepositoryProvider, shopRepositoryProvider, transactionRepositoryProvider, backupMetaRepositoryProvider);
  }

  public static BackupManager newInstance(Context context, CategoryRepository categoryRepository,
      MemberRepository memberRepository, ShopRepository shopRepository,
      TransactionRepository transactionRepository, BackupMetaRepository backupMetaRepository) {
    return new BackupManager(context, categoryRepository, memberRepository, shopRepository, transactionRepository, backupMetaRepository);
  }
}
