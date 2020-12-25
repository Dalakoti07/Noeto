package com.dalakoti07.android.notestaking.di.components;

import com.dalakoti07.android.notestaking.di.modules.AppModule;
import com.dalakoti07.android.notestaking.di.modules.AppSubComponents;
import com.dalakoti07.android.notestaking.di.modules.RoomModule;
import com.dalakoti07.android.notestaking.di.modules.ViewModelFactoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {},modules = {AppModule.class, RoomModule.class, AppSubComponents.class, ViewModelFactoryModule.class})
public interface AppComponent {

    MainComponent.Factory mainComponent();
}
