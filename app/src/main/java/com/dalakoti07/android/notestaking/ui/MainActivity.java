package com.dalakoti07.android.notestaking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dalakoti07.android.notestaking.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account= GoogleSignIn.getLastSignedInAccount(this);
    }

    public boolean isUserSignedIn(){
        return account!=null;
    }
}