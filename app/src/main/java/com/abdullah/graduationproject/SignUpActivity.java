package com.abdullah.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void SignUpActivityButtonClicked(View view) {
        Toast.makeText(this, "Check the Information to Sign Up", Toast.LENGTH_SHORT).show();
    }
}