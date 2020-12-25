package com.dalakoti07.android.notestaking.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application applicationInstance;

    public AppModule(Application application) {
        applicationInstance = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return applicationInstance;
    }
}
