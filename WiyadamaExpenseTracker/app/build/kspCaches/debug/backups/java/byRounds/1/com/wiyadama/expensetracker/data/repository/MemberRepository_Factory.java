package com.wiyadama.expensetracker.data.repository;

import com.wiyadama.expensetracker.data.dao.MemberDao;
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
    "KotlinInternalInJava"
})
public final class MemberRepository_Factory implements Factory<MemberRepository> {
  private final Provider<MemberDao> memberDaoProvider;

  public MemberRepository_Factory(Provider<MemberDao> memberDaoProvider) {
    this.memberDaoProvider = memberDaoProvider;
  }

  @Override
  public MemberRepository get() {
    return newInstance(memberDaoProvider.get());
  }

  public static MemberRepository_Factory create(Provider<MemberDao> memberDaoProvider) {
    return new MemberRepository_Factory(memberDaoProvider);
  }

  public static MemberRepository newInstance(MemberDao memberDao) {
    return new MemberRepository(memberDao);
  }
}
