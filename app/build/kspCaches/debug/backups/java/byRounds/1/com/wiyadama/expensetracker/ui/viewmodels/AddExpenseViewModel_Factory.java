package com.wiyadama.expensetracker.ui.viewmodels;

import com.wiyadama.expensetracker.data.repository.CategoryRepository;
import com.wiyadama.expensetracker.data.repository.MemberRepository;
import com.wiyadama.expensetracker.data.repository.ShopRepository;
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
public final class AddExpenseViewModel_Factory implements Factory<AddExpenseViewModel> {
  private final Provider<TransactionRepository> transactionRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  private final Provider<MemberRepository> memberRepositoryProvider;

  private final Provider<ShopRepository> shopRepositoryProvider;

  public AddExpenseViewModel_Factory(Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider) {
    this.transactionRepositoryProvider = transactionRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
    this.memberRepositoryProvider = memberRepositoryProvider;
    this.shopRepositoryProvider = shopRepositoryProvider;
  }

  @Override
  public AddExpenseViewModel get() {
    return newInstance(transactionRepositoryProvider.get(), categoryRepositoryProvider.get(), memberRepositoryProvider.get(), shopRepositoryProvider.get());
  }

  public static AddExpenseViewModel_Factory create(
      Provider<TransactionRepository> transactionRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider,
      Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider) {
    return new AddExpenseViewModel_Factory(transactionRepositoryProvider, categoryRepositoryProvider, memberRepositoryProvider, shopRepositoryProvider);
  }

  public static AddExpenseViewModel newInstance(TransactionRepository transactionRepository,
      CategoryRepository categoryRepository, MemberRepository memberRepository,
      ShopRepository shopRepository) {
    return new AddExpenseViewModel(transactionRepository, categoryRepository, memberRepository, shopRepository);
  }
}
