package com.wiyadama.expensetracker.data.repository;

import com.wiyadama.expensetracker.data.dao.RentalPropertyDao;
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
public final class RentalPropertyRepository_Factory implements Factory<RentalPropertyRepository> {
  private final Provider<RentalPropertyDao> rentalPropertyDaoProvider;

  public RentalPropertyRepository_Factory(Provider<RentalPropertyDao> rentalPropertyDaoProvider) {
    this.rentalPropertyDaoProvider = rentalPropertyDaoProvider;
  }

  @Override
  public RentalPropertyRepository get() {
    return newInstance(rentalPropertyDaoProvider.get());
  }

  public static RentalPropertyRepository_Factory create(
      Provider<RentalPropertyDao> rentalPropertyDaoProvider) {
    return new RentalPropertyRepository_Factory(rentalPropertyDaoProvider);
  }

  public static RentalPropertyRepository newInstance(RentalPropertyDao rentalPropertyDao) {
    return new RentalPropertyRepository(rentalPropertyDao);
  }
}
