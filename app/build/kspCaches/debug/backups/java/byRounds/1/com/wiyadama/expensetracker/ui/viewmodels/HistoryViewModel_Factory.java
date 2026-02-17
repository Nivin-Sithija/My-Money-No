package com.wiyadama.expensetracker.ui.viewmodels;

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
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  public HistoryViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(transactionRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider) {
    return new HistoryViewModel_Factory(transactionRepositoryProvider);
  }

  public static HistoryViewModel newInstance(TransactionRepository transactionRepository) {
    return new HistoryViewModel(transactionRepository);
  }
}
