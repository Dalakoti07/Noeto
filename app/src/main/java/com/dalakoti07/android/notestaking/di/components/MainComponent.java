package com.dalakoti07.android.notestaking.di.components;


import android.content.Context;

import com.dalakoti07.android.notestaking.di.ActivityScope;
import com.dalakoti07.android.notestaking.di.modules.MainViewModelModule;
import com.dalakoti07.android.notestaking.ui.MainActivity;
import com.dalakoti07.android.notestaking.ui.fragments.EditNoteFragment;
import com.dalakoti07.android.notestaking.ui.fragments.HomeFragment;
import com.dalakoti07.android.notestaking.ui.fragments.LogInFragment;
import com.dalakoti07.android.notestaking.ui.fragments.ProfileFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {MainViewModelModule.class})
public interface MainComponent {

    @Subcomponent.Factory
    interface Factory{
        MainComponent create(@BindsInstance Context context);
    }

    void inject(MainActivity activity);
    void inject(HomeFragment fragment);
    void inject(EditNoteFragment fragment);
    void inject(ProfileFragment fragment);
    void inject(LogInFragment fragment);
}
