package com.abdullah.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void SignUpButtonClicked(View view) {
        Intent toSignUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(toSignUpActivity);
    }

    public void SignInButtonClicked(View view) {
        Toast.makeText(this, "Check the Email and the Password", Toast.LENGTH_SHORT).show();
    }
}