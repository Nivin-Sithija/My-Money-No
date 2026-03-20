package com.wiyadama.expensetracker.di;

import com.wiyadama.expensetracker.data.dao.IncomeDao;
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
public final class DatabaseModule_ProvideIncomeDaoFactory implements Factory<IncomeDao> {
  private final Provider<WiyadamaDatabase> databaseProvider;

  public DatabaseModule_ProvideIncomeDaoFactory(Provider<WiyadamaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public IncomeDao get() {
    return provideIncomeDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideIncomeDaoFactory create(
      Provider<WiyadamaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideIncomeDaoFactory(databaseProvider);
  }

  public static IncomeDao provideIncomeDao(WiyadamaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideIncomeDao(database));
  }
}
