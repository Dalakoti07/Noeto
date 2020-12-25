package com.dalakoti07.android.notestaking.di.modules;

import androidx.lifecycle.ViewModelProvider;

import com.dalakoti07.android.notestaking.viewModels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);
}
