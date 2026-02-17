package com.wiyadama.expensetracker.ui.viewmodels;

import com.wiyadama.expensetracker.data.repository.MemberRepository;
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

  public MembersViewModel_Factory(Provider<MemberRepository> memberRepositoryProvider) {
    this.memberRepositoryProvider = memberRepositoryProvider;
  }

  @Override
  public MembersViewModel get() {
    return newInstance(memberRepositoryProvider.get());
  }

  public static MembersViewModel_Factory create(
      Provider<MemberRepository> memberRepositoryProvider) {
    return new MembersViewModel_Factory(memberRepositoryProvider);
  }

  public static MembersViewModel newInstance(MemberRepository memberRepository) {
    return new MembersViewModel(memberRepository);
  }
}
