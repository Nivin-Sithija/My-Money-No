package com.wiyadama.expensetracker.ui.screens;

import com.wiyadama.expensetracker.data.backup.BackupManager;
import com.wiyadama.expensetracker.data.repository.CategoryRepository;
import com.wiyadama.expensetracker.data.repository.TransactionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<BackupManager> backupManagerProvider;

  public HomeViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<BackupManager> backupManagerProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.backupManagerProvider = backupManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), categoryRepositoryProvider.get(), backupManagerProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<BackupManager> backupManagerProvider) {
    return new HomeViewModel_Factory(transactionRepositoryProvider, categoryRepositoryProvider, backupManagerProvider);
  }

  public static HomeViewModel newInstance(TransactionRepository transactionRepository,
      CategoryRepository categoryRepository, BackupManager backupManager) {
    return new HomeViewModel(transactionRepository, categoryRepository, backupManager);
  }
}
