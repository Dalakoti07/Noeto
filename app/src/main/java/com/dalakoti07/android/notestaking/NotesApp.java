package com.dalakoti07.android.notestaking;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.dalakoti07.android.notestaking.di.components.AppComponent;
import com.dalakoti07.android.notestaking.di.components.DaggerAppComponent;
import com.dalakoti07.android.notestaking.di.modules.AppModule;
import com.dalakoti07.android.notestaking.di.modules.RoomModule;
import com.dalakoti07.android.notestaking.room.NotesDatabase;

public class NotesApp extends Application {
    private static NotesApp context;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        appComponent= DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this)).build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

    public static NotesApp get(Activity activity){
        return (NotesApp) activity.getApplication();
    }

    public static Context getAppContext(){
        return context;
    }
}
