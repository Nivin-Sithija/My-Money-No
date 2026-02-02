package com.wiyadama.expensetracker.di;

import com.wiyadama.expensetracker.data.dao.BackupMetaDao;
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
public final class DatabaseModule_ProvideBackupMetaDaoFactory implements Factory<BackupMetaDao> {
  private final Provider<WiyadamaDatabase> databaseProvider;

  public DatabaseModule_ProvideBackupMetaDaoFactory(Provider<WiyadamaDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupMetaDao get() {
    return provideBackupMetaDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBackupMetaDaoFactory create(
      Provider<WiyadamaDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBackupMetaDaoFactory(databaseProvider);
  }

  public static BackupMetaDao provideBackupMetaDao(WiyadamaDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBackupMetaDao(database));
  }
}
