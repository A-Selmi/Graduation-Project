package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class AdviserCvActivity extends AppCompatActivity {

    ScrollView CVScrollView;
    TextView UploadTextView;
    ScrollView WorkerDetailsScrollView;
    EditText ExperienceEditText, ProjectsEditText, SkillsEditText, OtherEditText;
    String Role;
    Context context;
    Uri pdfUri;
    boolean True;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adviser_cv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        if (Role.equals("1")) {
            CVScrollView.setVisibility(View.GONE);
            WorkerDetailsScrollView.setVisibility(View.VISIBLE);
        } else {
            CVScrollView.setVisibility(View.VISIBLE);
            WorkerDetailsScrollView.setVisibility(View.GONE);
        }
    }

    public void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, metrics);
    }

    private void findView() {
        CVScrollView = findViewById(R.id.CVScrollView);
        UploadTextView = findViewById(R.id.UploadTextView);
        WorkerDetailsScrollView = findViewById(R.id.WorkerDetailsScrollView);
        ExperienceEditText = findViewById(R.id.ExperienceEditText);
        ProjectsEditText = findViewById(R.id.ProjectsEditText);
        SkillsEditText = findViewById(R.id.SkillsEditText);
        OtherEditText = findViewById(R.id.OtherEditText);
        Role = getIntent().getStringExtra("Role").toString();
        context = this;
    }

    public void UploadButtonClicked(View view) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            ActivityCompat.requestPermissions(AdviserCvActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
            True = true;
//            UploadThePDFFile();
        } else {
            True = false;
            Toast.makeText(AdviserCvActivity.this, "الرجاء اعطاء الصلاحية", Toast.LENGTH_LONG).show();
        }
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //to fetch files
        startActivityForResult(intent, 86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            UploadTextView.setText(data.getData().getLastPathSegment());
            True = true;
//            UploadThePDFFile();
        } else {
            True = false;
            Toast.makeText(AdviserCvActivity.this, "الرجاء اختيار الملف ", Toast.LENGTH_LONG).show();
        }
    }

    public void UploadDoneButtonClicked(View view) {
        if(True) {
            Intent toPhoneNumberActivity = new Intent(AdviserCvActivity.this, PhoneNumberActivity.class);
            toPhoneNumberActivity.putExtra("uri", pdfUri.toString());
            startActivity(toPhoneNumberActivity);
        }
    }

    public void WorkerDetailsButtonClicked(View view) {
        hideKeyboard(this);
        if (ExperienceEditText.getText().toString().trim().isEmpty()) {
            MainActivity.SaveSharedPreference.setPE(context, "");
        } else {
            MainActivity.SaveSharedPreference.setPE(context, ExperienceEditText.getText().toString().trim());
        }
        if (ProjectsEditText.getText().toString().trim().isEmpty()) {
            MainActivity.SaveSharedPreference.setPP(context, "");
        } else {
            MainActivity.SaveSharedPreference.setPP(context, ProjectsEditText.getText().toString().trim());
        }
        if (SkillsEditText.getText().toString().trim().isEmpty()) {
            MainActivity.SaveSharedPreference.setSkills(context, "");
        } else {
            MainActivity.SaveSharedPreference.setSkills(context, SkillsEditText.getText().toString().trim());
        }
        if (OtherEditText.getText().toString().trim().isEmpty()) {
            MainActivity.SaveSharedPreference.setAbout(context, "");
        } else {
            MainActivity.SaveSharedPreference.setAbout(context, OtherEditText.getText().toString().trim());
        }
        Intent toPhoneNumberActivity = new Intent(AdviserCvActivity.this, PhoneNumberActivity.class);
        startActivity(toPhoneNumberActivity);
    }

    public static void hideKeyboard(AdviserCvActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AdviserCvActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}