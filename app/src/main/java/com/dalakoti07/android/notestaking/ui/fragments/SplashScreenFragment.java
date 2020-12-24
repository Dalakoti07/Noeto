package com.dalakoti07.android.notestaking.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.dalakoti07.android.notestaking.ui.MainActivity;

import java.lang.ref.WeakReference;

public class SplashScreenFragment extends Fragment {
    private FragmentSplashScreenBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentSplashScreenBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    //todo sometime its not working may be orientation change
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= NavHostFragment.findNavController(this);
        new Handler().postDelayed(() -> {
            if(((MainActivity)getActivity()).isUserSignedIn())
                navController.navigate(R.id.action_splashScreenFragment_to_homeFragment);
            else
                navController.navigate(R.id.action_splashScreenFragment_to_logInFragment);
        },500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}
