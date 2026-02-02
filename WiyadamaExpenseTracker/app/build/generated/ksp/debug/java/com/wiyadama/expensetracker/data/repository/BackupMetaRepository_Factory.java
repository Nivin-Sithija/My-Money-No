package com.wiyadama.expensetracker.data.repository;

import com.wiyadama.expensetracker.data.dao.BackupMetaDao;
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
public final class BackupMetaRepository_Factory implements Factory<BackupMetaRepository> {
  private final Provider<BackupMetaDao> backupMetaDaoProvider;

  public BackupMetaRepository_Factory(Provider<BackupMetaDao> backupMetaDaoProvider) {
    this.backupMetaDaoProvider = backupMetaDaoProvider;
  }

  @Override
  public BackupMetaRepository get() {
    return newInstance(backupMetaDaoProvider.get());
  }

  public static BackupMetaRepository_Factory create(Provider<BackupMetaDao> backupMetaDaoProvider) {
    return new BackupMetaRepository_Factory(backupMetaDaoProvider);
  }

  public static BackupMetaRepository newInstance(BackupMetaDao backupMetaDao) {
    return new BackupMetaRepository(backupMetaDao);
  }
}
