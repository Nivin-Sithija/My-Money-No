package com.wiyadama.expensetracker.di;

import com.wiyadama.expensetracker.data.dao.ShopDao;
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
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvideShopDaoFactory implements Factory<ShopDao> {
  private final Provider<WiyadamaDatabase> databaseProvider;

  public DatabaseModule_ProvideShopDaoFactory(Provider<WiyadamaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ShopDao get() {
    return provideShopDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideShopDaoFactory create(
      Provider<WiyadamaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideShopDaoFactory(databaseProvider);
  }

  public static ShopDao provideShopDao(WiyadamaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideShopDao(database));
  }
}
