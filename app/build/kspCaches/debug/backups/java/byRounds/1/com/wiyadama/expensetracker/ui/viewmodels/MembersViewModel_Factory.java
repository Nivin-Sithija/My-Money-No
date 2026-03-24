package com.wiyadama.expensetracker.ui.viewmodels;

import com.wiyadama.expensetracker.data.repository.MemberRepository;
import com.wiyadama.expensetracker.data.repository.ShopRepository;
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
public final class MembersViewModel_Factory implements Factory<MembersViewModel> {
  private final Provider<MemberRepository> memberRepositoryProvider;

  private final Provider<ShopRepository> shopRepositoryProvider;

  public MembersViewModel_Factory(Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider) {
    this.memberRepositoryProvider = memberRepositoryProvider;
    this.shopRepositoryProvider = shopRepositoryProvider;
  }

  @Override
  public MembersViewModel get() {
    return newInstance(memberRepositoryProvider.get(), shopRepositoryProvider.get());
  }

  public static MembersViewModel_Factory create(Provider<MemberRepository> memberRepositoryProvider,
      Provider<ShopRepository> shopRepositoryProvider) {
    return new MembersViewModel_Factory(memberRepositoryProvider, shopRepositoryProvider);
  }

  public static MembersViewModel newInstance(MemberRepository memberRepository,
      ShopRepository shopRepository) {
    return new MembersViewModel(memberRepository, shopRepository);
  }
}
