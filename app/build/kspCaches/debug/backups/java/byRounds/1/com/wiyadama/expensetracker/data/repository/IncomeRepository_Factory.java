package com.wiyadama.expensetracker.data.repository;

import com.wiyadama.expensetracker.data.dao.IncomeDao;
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
public final class IncomeRepository_Factory implements Factory<IncomeRepository> {
  private final Provider<IncomeDao> incomeDaoProvider;

  public IncomeRepository_Factory(Provider<IncomeDao> incomeDaoProvider) {
    this.incomeDaoProvider = incomeDaoProvider;
  }

  @Override
  public IncomeRepository get() {
    return newInstance(incomeDaoProvider.get());
  }

  public static IncomeRepository_Factory create(Provider<IncomeDao> incomeDaoProvider) {
    return new IncomeRepository_Factory(incomeDaoProvider);
  }

  public static IncomeRepository newInstance(IncomeDao incomeDao) {
    return new IncomeRepository(incomeDao);
  }
}
