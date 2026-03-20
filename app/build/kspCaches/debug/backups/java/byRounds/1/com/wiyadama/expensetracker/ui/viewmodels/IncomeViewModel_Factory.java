package com.wiyadama.expensetracker.ui.viewmodels;

import com.wiyadama.expensetracker.data.repository.IncomeRepository;
import com.wiyadama.expensetracker.data.repository.RentalPropertyRepository;
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
public final class IncomeViewModel_Factory implements Factory<IncomeViewModel> {
  private final Provider<IncomeRepository> incomeRepositoryProvider;

  private final Provider<RentalPropertyRepository> rentalPropertyRepositoryProvider;

  public IncomeViewModel_Factory(Provider<IncomeRepository> incomeRepositoryProvider,
      Provider<RentalPropertyRepository> rentalPropertyRepositoryProvider) {
    this.incomeRepositoryProvider = incomeRepositoryProvider;
    this.rentalPropertyRepositoryProvider = rentalPropertyRepositoryProvider;
  }

  @Override
  public IncomeViewModel get() {
    return newInstance(incomeRepositoryProvider.get(), rentalPropertyRepositoryProvider.get());
  }

  public static IncomeViewModel_Factory create(Provider<IncomeRepository> incomeRepositoryProvider,
      Provider<RentalPropertyRepository> rentalPropertyRepositoryProvider) {
    return new IncomeViewModel_Factory(incomeRepositoryProvider, rentalPropertyRepositoryProvider);
  }

  public static IncomeViewModel newInstance(IncomeRepository incomeRepository,
      RentalPropertyRepository rentalPropertyRepository) {
    return new IncomeViewModel(incomeRepository, rentalPropertyRepository);
  }
}
