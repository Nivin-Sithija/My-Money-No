package com.wiyadama.expensetracker.ui.viewmodels;

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
public final class CategoryDetailViewModel_Factory implements Factory<CategoryDetailViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public CategoryDetailViewModel_Factory(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public CategoryDetailViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static CategoryDetailViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new CategoryDetailViewModel_Factory(transactionRepositoryProvider, categoryRepositoryProvider);
  }

  public static CategoryDetailViewModel newInstance(TransactionRepository transactionRepository,
      CategoryRepository categoryRepository) {
    return new CategoryDetailViewModel(transactionRepository, categoryRepository);
  }
}
