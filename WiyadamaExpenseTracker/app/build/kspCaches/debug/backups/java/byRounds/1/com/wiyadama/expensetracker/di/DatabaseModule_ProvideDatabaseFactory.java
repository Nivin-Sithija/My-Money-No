package com.wiyadama.expensetracker.di;

import android.content.Context;
import com.wiyadama.expensetracker.data.database.WiyadamaDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideDatabaseFactory implements Factory<WiyadamaDatabase> {
  private final Provider<Context> contextProvider;

  private final Provider<CoroutineScope> scopeProvider;

  public DatabaseModule_ProvideDatabaseFactory(Provider<Context> contextProvider,
      Provider<CoroutineScope> scopeProvider) {
    this.contextProvider = contextProvider;
    this.scopeProvider = scopeProvider;
  }

  @Override
  public WiyadamaDatabase get() {
    return provideDatabase(contextProvider.get(), scopeProvider.get());
  }

  public static DatabaseModule_ProvideDatabaseFactory create(Provider<Context> contextProvider,
      Provider<CoroutineScope> scopeProvider) {
    return new DatabaseModule_ProvideDatabaseFactory(contextProvider, scopeProvider);
  }

  public static WiyadamaDatabase provideDatabase(Context context, CoroutineScope scope) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDatabase(context, scope));
  }
}
