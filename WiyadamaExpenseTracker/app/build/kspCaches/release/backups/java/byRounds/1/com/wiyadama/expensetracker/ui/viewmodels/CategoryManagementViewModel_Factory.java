package com.wiyadama.expensetracker.ui.viewmodels;

import com.wiyadama.expensetracker.data.repository.CategoryRepository;
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
public final class CategoryManagementViewModel_Factory implements Factory<CategoryManagementViewModel> {
  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public CategoryManagementViewModel_Factory(
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public CategoryManagementViewModel get() {
    return newInstance(categoryRepositoryProvider.get());
  }

  public static CategoryManagementViewModel_Factory create(
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new CategoryManagementViewModel_Factory(categoryRepositoryProvider);
  }

  public static CategoryManagementViewModel newInstance(CategoryRepository categoryRepository) {
    return new CategoryManagementViewModel(categoryRepository);
  }
}
