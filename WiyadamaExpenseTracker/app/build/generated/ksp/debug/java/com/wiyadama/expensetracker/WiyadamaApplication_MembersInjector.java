package com.wiyadama.expensetracker;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class WiyadamaApplication_MembersInjector implements MembersInjector<WiyadamaApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public WiyadamaApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<WiyadamaApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new WiyadamaApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(WiyadamaApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.wiyadama.expensetracker.WiyadamaApplication.workerFactory")
  public static void injectWorkerFactory(WiyadamaApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
