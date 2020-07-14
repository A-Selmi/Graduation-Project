package com.abdullah.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdviserCvActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adviser_cv);
    }

    public void UploadButtonClicked(View view) {
        Toast.makeText(this, "Upload Document", Toast.LENGTH_SHORT).show();
    }

    public void UploadDoneButtonClicked(View view) {
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }
}