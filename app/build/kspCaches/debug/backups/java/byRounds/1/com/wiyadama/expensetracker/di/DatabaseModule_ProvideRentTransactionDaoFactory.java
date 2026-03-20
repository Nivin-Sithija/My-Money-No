package com.wiyadama.expensetracker.di;

import com.wiyadama.expensetracker.data.dao.RentTransactionDao;
import com.wiyadama.expensetracker.data.database.WiyadamaDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideRentTransactionDaoFactory implements Factory<RentTransactionDao> {
  private final Provider<WiyadamaDatabase> databaseProvider;

  public DatabaseModule_ProvideRentTransactionDaoFactory(
      Provider<WiyadamaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RentTransactionDao get() {
    return provideRentTransactionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideRentTransactionDaoFactory create(
      Provider<WiyadamaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideRentTransactionDaoFactory(databaseProvider);
  }

  public static RentTransactionDao provideRentTransactionDao(WiyadamaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRentTransactionDao(database));
  }
}
