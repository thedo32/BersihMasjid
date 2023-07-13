package com.app.ikafeunand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ikafeunand.databinding.ActivitySplashBinding;


public class Splash extends AppCompatActivity {
    ActivitySplashBinding binding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences( "kitasinau",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (preferences.getBoolean("autologin",false)) {
                    startActivity(new Intent(Splash.this, Dashboard.class));
                }else{*/
                    startActivity(new Intent(Splash.this, SignInActivity.class));
               /* }*/
            }
        },5000);
    }
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
