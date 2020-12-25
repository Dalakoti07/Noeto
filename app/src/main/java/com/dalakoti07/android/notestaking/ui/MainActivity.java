package com.dalakoti07.android.notestaking.ui;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.dalakoti07.android.notestaking.NotesApp;
import com.dalakoti07.android.notestaking.R;
import com.dalakoti07.android.notestaking.di.components.MainComponent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    public MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dagger injection
        mainComponent= NotesApp.get(this).getAppComponent().mainComponent().create(this);
        mainComponent.inject(this);

        super.onCreate(savedInstanceState);
        changeStatusBarColor(R.color.white);
        setContentView(R.layout.activity_main);
        account= GoogleSignIn.getLastSignedInAccount(this);
    }

    protected void changeStatusBarColor(@ColorRes int colorRes) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,colorRes));
        }
    }

    public boolean isUserSignedIn(){
        return account!=null;
    }
}