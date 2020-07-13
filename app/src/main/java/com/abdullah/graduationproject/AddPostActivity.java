package com.abdullah.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }

    public void ConfirmAddPostButtonClicked(View view) {
        Intent toProfileActivity = new Intent(this, ProfileFragment.class);
        finish();
        Toast.makeText(this, "The post added successfully", Toast.LENGTH_SHORT).show();
        startActivity(toProfileActivity);
    }
}