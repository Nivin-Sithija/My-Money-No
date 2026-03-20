package com.wiyadama.expensetracker.data.repository;

import com.wiyadama.expensetracker.data.dao.RentTransactionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RentTransactionRepository_Factory implements Factory<RentTransactionRepository> {
  private final Provider<RentTransactionDao> rentTransactionDaoProvider;

  public RentTransactionRepository_Factory(
      Provider<RentTransactionDao> rentTransactionDaoProvider) {
    this.rentTransactionDaoProvider = rentTransactionDaoProvider;
  }

  @Override
  public RentTransactionRepository get() {
    return newInstance(rentTransactionDaoProvider.get());
  }

  public static RentTransactionRepository_Factory create(
      Provider<RentTransactionDao> rentTransactionDaoProvider) {
    return new RentTransactionRepository_Factory(rentTransactionDaoProvider);
  }

  public static RentTransactionRepository newInstance(RentTransactionDao rentTransactionDao) {
    return new RentTransactionRepository(rentTransactionDao);
  }
}
