package com.abdullah.graduationproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdullah.graduationproject.LogInActivities.AdviserCvActivity;
import com.abdullah.graduationproject.R;

public class WorkerDetailsActivity extends AppCompatActivity {

    TextView experienceTextView, projectsTextView,
            skillsTextView, otherTextView;
    Button EditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        SetData();
    }

    private void SetData() {

        if(MainActivity.SaveSharedPreference.getPE(WorkerDetailsActivity.this).isEmpty()) {
            experienceTextView.setText("لا يوجد خبرات سابقة");
        }else {
            experienceTextView.setText(MainActivity.SaveSharedPreference.getPE(WorkerDetailsActivity.this));
        }

        if(MainActivity.SaveSharedPreference.getPP(WorkerDetailsActivity.this).isEmpty()) {
            projectsTextView.setText("لا يوجد مشاريع سابقة");
        }else {
            projectsTextView.setText(MainActivity.SaveSharedPreference.getPP(WorkerDetailsActivity.this));
        }

        if(MainActivity.SaveSharedPreference.getSkills(WorkerDetailsActivity.this).isEmpty()) {
            skillsTextView.setText("لا يوجد مهارات سابقة");
        }else {
            skillsTextView.setText(MainActivity.SaveSharedPreference.getSkills(WorkerDetailsActivity.this));
        }

        if(MainActivity.SaveSharedPreference.getAbout(WorkerDetailsActivity.this).isEmpty()) {
            otherTextView.setText("لا يوجد معلومات أخرى");
        }else {
            otherTextView.setText(MainActivity.SaveSharedPreference.getAbout(WorkerDetailsActivity.this));
        }

    }

    private void findView() {
        experienceTextView = findViewById(R.id.experienceTextView);
        projectsTextView = findViewById(R.id.projectsTextView);
        skillsTextView = findViewById(R.id.skillsTextView);
        otherTextView = findViewById(R.id.otherTextView);
        EditButton = findViewById(R.id.EditButton);
    }

    public void EditButtonClicked(View view) {
        Intent toAdviserCvActivity = new Intent(WorkerDetailsActivity.this, AdviserCvActivity.class);
        toAdviserCvActivity.putExtra("Role", "1");
        toAdviserCvActivity.putExtra("S", "E");
        startActivity(toAdviserCvActivity);
    }
}