package com.abdullah.graduationproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.abdullah.graduationproject.Fragments.ProfileFragment;
import com.abdullah.graduationproject.R;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }


    public void ReviewButtonClicked(View view) {
        Intent toProfileActivity = new Intent(this, ProfileFragment.class);
        finish();
        Toast.makeText(this, "The post added successfully", Toast.LENGTH_SHORT).show();
        startActivity(toProfileActivity);
    }
}