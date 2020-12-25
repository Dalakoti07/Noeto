package com.dalakoti07.android.notestaking.di.modules;

import androidx.lifecycle.ViewModel;

import com.dalakoti07.android.notestaking.viewModels.EditNoteViewModel;
import com.dalakoti07.android.notestaking.viewModels.HomeFragmentViewModel;
import com.dalakoti07.android.notestaking.viewModels.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel.class)
    public abstract ViewModel bindHomeFragmentViewModel(HomeFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditNoteViewModel.class)
    public abstract ViewModel bindEditNotesFragmentViewModel(EditNoteViewModel viewModel);
}
