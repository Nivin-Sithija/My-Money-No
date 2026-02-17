package com.wiyadama.expensetracker;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.wiyadama.expensetracker.data.backup.BackupManager;
import com.wiyadama.expensetracker.data.backup.BackupWorker;
import com.wiyadama.expensetracker.data.backup.BackupWorker_AssistedFactory;
import com.wiyadama.expensetracker.data.dao.BackupMetaDao;
import com.wiyadama.expensetracker.data.dao.CategoryDao;
import com.wiyadama.expensetracker.data.dao.MemberDao;
import com.wiyadama.expensetracker.data.dao.ShopDao;
import com.wiyadama.expensetracker.data.dao.TransactionDao;
import com.wiyadama.expensetracker.data.database.WiyadamaDatabase;
import com.wiyadama.expensetracker.data.repository.BackupMetaRepository;
import com.wiyadama.expensetracker.data.repository.CategoryRepository;
import com.wiyadama.expensetracker.data.repository.MemberRepository;
import com.wiyadama.expensetracker.data.repository.ShopRepository;
import com.wiyadama.expensetracker.data.repository.TransactionRepository;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideApplicationScopeFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideBackupMetaDaoFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideCategoryDaoFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideDatabaseFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideMemberDaoFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideShopDaoFactory;
import com.wiyadama.expensetracker.di.DatabaseModule_ProvideTransactionDaoFactory;
import com.wiyadama.expensetracker.ui.screens.HomeViewModel;
import com.wiyadama.expensetracker.ui.screens.HomeViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.CategoryDetailViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.CategoryDetailViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel_HiltModules;
import com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel;
import com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

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
public final class DaggerWiyadamaApplication_HiltComponents_SingletonC {
  private DaggerWiyadamaApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public WiyadamaApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements WiyadamaApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements WiyadamaApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements WiyadamaApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements WiyadamaApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements WiyadamaApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements WiyadamaApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements WiyadamaApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public WiyadamaApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends WiyadamaApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends WiyadamaApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends WiyadamaApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends WiyadamaApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel, AddExpenseViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel, AnalyticsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel, CategoryDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel, CategoryManagementViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_screens_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel, MembersViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel = "com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel = "com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel = "com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel = "com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel = "com.wiyadama.expensetracker.ui.viewmodels.CategoryDetailViewModel";

      static String com_wiyadama_expensetracker_ui_screens_HomeViewModel = "com.wiyadama.expensetracker.ui.screens.HomeViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel = "com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel";

      @KeepFieldType
      AddExpenseViewModel com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel2;

      @KeepFieldType
      CategoryManagementViewModel com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel2;

      @KeepFieldType
      HistoryViewModel com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel2;

      @KeepFieldType
      CategoryDetailViewModel com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel2;

      @KeepFieldType
      HomeViewModel com_wiyadama_expensetracker_ui_screens_HomeViewModel2;

      @KeepFieldType
      MembersViewModel com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel2;
    }
  }

  private static final class ViewModelCImpl extends WiyadamaApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AddExpenseViewModel> addExpenseViewModelProvider;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<CategoryDetailViewModel> categoryDetailViewModelProvider;

    private Provider<CategoryManagementViewModel> categoryManagementViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MembersViewModel> membersViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.addExpenseViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.categoryDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.categoryManagementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.membersViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel, ((Provider) addExpenseViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel, ((Provider) analyticsViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel, ((Provider) categoryDetailViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel, ((Provider) categoryManagementViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_screens_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel, ((Provider) membersViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel = "com.wiyadama.expensetracker.ui.viewmodels.CategoryDetailViewModel";

      static String com_wiyadama_expensetracker_ui_screens_HomeViewModel = "com.wiyadama.expensetracker.ui.screens.HomeViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel = "com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel = "com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel = "com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel = "com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel";

      static String com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel = "com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel";

      @KeepFieldType
      CategoryDetailViewModel com_wiyadama_expensetracker_ui_viewmodels_CategoryDetailViewModel2;

      @KeepFieldType
      HomeViewModel com_wiyadama_expensetracker_ui_screens_HomeViewModel2;

      @KeepFieldType
      HistoryViewModel com_wiyadama_expensetracker_ui_viewmodels_HistoryViewModel2;

      @KeepFieldType
      AddExpenseViewModel com_wiyadama_expensetracker_ui_viewmodels_AddExpenseViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_wiyadama_expensetracker_ui_viewmodels_AnalyticsViewModel2;

      @KeepFieldType
      CategoryManagementViewModel com_wiyadama_expensetracker_ui_viewmodels_CategoryManagementViewModel2;

      @KeepFieldType
      MembersViewModel com_wiyadama_expensetracker_ui_viewmodels_MembersViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.wiyadama.expensetracker.ui.viewmodels.AddExpenseViewModel 
          return (T) new AddExpenseViewModel(singletonCImpl.transactionRepositoryProvider.get(), singletonCImpl.categoryRepositoryProvider.get(), singletonCImpl.memberRepositoryProvider.get(), singletonCImpl.shopRepositoryProvider.get());

          case 1: // com.wiyadama.expensetracker.ui.viewmodels.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(singletonCImpl.transactionRepositoryProvider.get(), singletonCImpl.categoryRepositoryProvider.get());

          case 2: // com.wiyadama.expensetracker.ui.viewmodels.CategoryDetailViewModel 
          return (T) new CategoryDetailViewModel(singletonCImpl.transactionRepositoryProvider.get(), singletonCImpl.categoryRepositoryProvider.get());

          case 3: // com.wiyadama.expensetracker.ui.viewmodels.CategoryManagementViewModel 
          return (T) new CategoryManagementViewModel(singletonCImpl.categoryRepositoryProvider.get());

          case 4: // com.wiyadama.expensetracker.ui.viewmodels.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.transactionRepositoryProvider.get());

          case 5: // com.wiyadama.expensetracker.ui.screens.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.transactionRepositoryProvider.get(), singletonCImpl.categoryRepositoryProvider.get(), singletonCImpl.memberRepositoryProvider.get(), singletonCImpl.shopRepositoryProvider.get(), singletonCImpl.backupManagerProvider.get());

          case 6: // com.wiyadama.expensetracker.ui.viewmodels.MembersViewModel 
          return (T) new MembersViewModel(singletonCImpl.memberRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends WiyadamaApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends WiyadamaApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends WiyadamaApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<CoroutineScope> provideApplicationScopeProvider;

    private Provider<WiyadamaDatabase> provideDatabaseProvider;

    private Provider<CategoryRepository> categoryRepositoryProvider;

    private Provider<MemberRepository> memberRepositoryProvider;

    private Provider<ShopRepository> shopRepositoryProvider;

    private Provider<TransactionRepository> transactionRepositoryProvider;

    private Provider<BackupMetaRepository> backupMetaRepositoryProvider;

    private Provider<BackupManager> backupManagerProvider;

    private Provider<BackupWorker_AssistedFactory> backupWorker_AssistedFactoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private CategoryDao categoryDao() {
      return DatabaseModule_ProvideCategoryDaoFactory.provideCategoryDao(provideDatabaseProvider.get());
    }

    private MemberDao memberDao() {
      return DatabaseModule_ProvideMemberDaoFactory.provideMemberDao(provideDatabaseProvider.get());
    }

    private ShopDao shopDao() {
      return DatabaseModule_ProvideShopDaoFactory.provideShopDao(provideDatabaseProvider.get());
    }

    private TransactionDao transactionDao() {
      return DatabaseModule_ProvideTransactionDaoFactory.provideTransactionDao(provideDatabaseProvider.get());
    }

    private BackupMetaDao backupMetaDao() {
      return DatabaseModule_ProvideBackupMetaDaoFactory.provideBackupMetaDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.wiyadama.expensetracker.data.backup.BackupWorker", ((Provider) backupWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideApplicationScopeProvider = DoubleCheck.provider(new SwitchingProvider<CoroutineScope>(singletonCImpl, 4));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<WiyadamaDatabase>(singletonCImpl, 3));
      this.categoryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CategoryRepository>(singletonCImpl, 2));
      this.memberRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MemberRepository>(singletonCImpl, 5));
      this.shopRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ShopRepository>(singletonCImpl, 6));
      this.transactionRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TransactionRepository>(singletonCImpl, 7));
      this.backupMetaRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BackupMetaRepository>(singletonCImpl, 8));
      this.backupManagerProvider = DoubleCheck.provider(new SwitchingProvider<BackupManager>(singletonCImpl, 1));
      this.backupWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<BackupWorker_AssistedFactory>(singletonCImpl, 0));
    }

    @Override
    public void injectWiyadamaApplication(WiyadamaApplication wiyadamaApplication) {
      injectWiyadamaApplication2(wiyadamaApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private WiyadamaApplication injectWiyadamaApplication2(WiyadamaApplication instance) {
      WiyadamaApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.wiyadama.expensetracker.data.backup.BackupWorker_AssistedFactory 
          return (T) new BackupWorker_AssistedFactory() {
            @Override
            public BackupWorker create(Context context, WorkerParameters params) {
              return new BackupWorker(context, params, singletonCImpl.backupManagerProvider.get());
            }
          };

          case 1: // com.wiyadama.expensetracker.data.backup.BackupManager 
          return (T) new BackupManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.categoryRepositoryProvider.get(), singletonCImpl.memberRepositoryProvider.get(), singletonCImpl.shopRepositoryProvider.get(), singletonCImpl.transactionRepositoryProvider.get(), singletonCImpl.backupMetaRepositoryProvider.get());

          case 2: // com.wiyadama.expensetracker.data.repository.CategoryRepository 
          return (T) new CategoryRepository(singletonCImpl.categoryDao());

          case 3: // com.wiyadama.expensetracker.data.database.WiyadamaDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideApplicationScopeProvider.get());

          case 4: // kotlinx.coroutines.CoroutineScope 
          return (T) DatabaseModule_ProvideApplicationScopeFactory.provideApplicationScope();

          case 5: // com.wiyadama.expensetracker.data.repository.MemberRepository 
          return (T) new MemberRepository(singletonCImpl.memberDao());

          case 6: // com.wiyadama.expensetracker.data.repository.ShopRepository 
          return (T) new ShopRepository(singletonCImpl.shopDao());

          case 7: // com.wiyadama.expensetracker.data.repository.TransactionRepository 
          return (T) new TransactionRepository(singletonCImpl.transactionDao());

          case 8: // com.wiyadama.expensetracker.data.repository.BackupMetaRepository 
          return (T) new BackupMetaRepository(singletonCImpl.backupMetaDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
