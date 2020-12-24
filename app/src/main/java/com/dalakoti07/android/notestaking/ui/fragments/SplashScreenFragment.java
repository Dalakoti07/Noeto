package com.dalakoti07.android.notestaking.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.dalakoti07.android.notestaking.R;
import com.dalakoti07.android.notestaking.databinding.FragmentSplashScreenBinding;

public class SplashScreenFragment extends Fragment {
    private FragmentSplashScreenBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentSplashScreenBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    //todo sometime its not working
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= NavHostFragment.findNavController(this);
        new Handler().postDelayed(() -> {
            navController.navigate(R.id.action_splashScreenFragment_to_logInFragment);
        },500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}
