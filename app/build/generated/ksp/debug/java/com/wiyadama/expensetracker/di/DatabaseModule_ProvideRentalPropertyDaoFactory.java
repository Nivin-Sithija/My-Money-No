package com.wiyadama.expensetracker.di;

import com.wiyadama.expensetracker.data.dao.RentalPropertyDao;
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
public final class DatabaseModule_ProvideRentalPropertyDaoFactory implements Factory<RentalPropertyDao> {
  private final Provider<WiyadamaDatabase> databaseProvider;

  public DatabaseModule_ProvideRentalPropertyDaoFactory(
      Provider<WiyadamaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RentalPropertyDao get() {
    return provideRentalPropertyDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideRentalPropertyDaoFactory create(
      Provider<WiyadamaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideRentalPropertyDaoFactory(databaseProvider);
  }

  public static RentalPropertyDao provideRentalPropertyDao(WiyadamaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRentalPropertyDao(database));
  }
}
