package com.abdullah.graduationproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.abdullah.graduationproject.R;

public class SplashScreen extends AppCompatActivity {

    static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMainActivity = new Intent(SplashScreen.this, MainActivity.class);
                MainActivity.SaveSharedPreference.setFragment(SplashScreen.this, "");
                startActivity(toMainActivity);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}