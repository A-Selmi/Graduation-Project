package com.abdullah.graduationproject.LogInActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdviserCvActivity extends AppCompatActivity {

    ScrollView CVScrollView;
    TextView UploadTextView;
    ScrollView WorkerDetailsScrollView;
    EditText ExperienceEditText, ProjectsEditText, SkillsEditText, OtherEditText;
    String Role;
    Context context;
    Uri pdfUri;
    boolean True;
    Button WorkerDetailsButton, SaveButtonWorkerDetails, CancelWorkerDetails;
    FirebaseFirestore db;
    Map<String, Object> user;
    ProgressBar progressBarWorkerAdviserCVActivity;
    String EditOrNot;

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
        progressBarWorkerAdviserCVActivity.setVisibility(View.GONE);
        if (Role.equals("1")) {
            CVScrollView.setVisibility(View.GONE);
            WorkerDetailsScrollView.setVisibility(View.VISIBLE);
            if (EditOrNot.equals("E")) {
                WorkerDetailsButton.setVisibility(View.GONE);
                SaveButtonWorkerDetails.setVisibility(View.VISIBLE);
                CancelWorkerDetails.setVisibility(View.VISIBLE);
                SetData();
            } else {
                WorkerDetailsButton.setVisibility(View.VISIBLE);
                SaveButtonWorkerDetails.setVisibility(View.GONE);
                CancelWorkerDetails.setVisibility(View.GONE);
            }
        } else {
            CVScrollView.setVisibility(View.VISIBLE);
            WorkerDetailsScrollView.setVisibility(View.GONE);
        }
    }

    private void SetData() {
        ExperienceEditText.setText(MainActivity.SaveSharedPreference.getPE(context));
        ProjectsEditText.setText(MainActivity.SaveSharedPreference.getPP(context));
        SkillsEditText.setText(MainActivity.SaveSharedPreference.getSkills(context));
        OtherEditText.setText(MainActivity.SaveSharedPreference.getAbout(context));
    }

    public void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, metrics);
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        CVScrollView = findViewById(R.id.CVScrollView);
        UploadTextView = findViewById(R.id.UploadTextView);
        WorkerDetailsScrollView = findViewById(R.id.WorkerDetailsScrollView);
        ExperienceEditText = findViewById(R.id.ExperienceEditText);
        ProjectsEditText = findViewById(R.id.ProjectsEditText);
        SkillsEditText = findViewById(R.id.SkillsEditText);
        OtherEditText = findViewById(R.id.OtherEditText);
        Role = getIntent().getStringExtra("Role");
        context = this;
        WorkerDetailsButton = findViewById(R.id.WorkerDetailsButton);
        SaveButtonWorkerDetails = findViewById(R.id.SaveButtonWorkerDetails);
        CancelWorkerDetails = findViewById(R.id.CancelWorkerDetails);
        progressBarWorkerAdviserCVActivity = findViewById(R.id.progressBarWorkerAdviserCVActivity);
        EditOrNot = getIntent().getStringExtra("S");
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
        } else {
            True = false;
            Toast.makeText(AdviserCvActivity.this, "الرجاء اختيار الملف ", Toast.LENGTH_LONG).show();
        }
    }

    public void UploadDoneButtonClicked(View view) {
        if (True) {
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

    public void SaveButtonWorkerDetailsClicked(View view) {
        hideKeyboard(this);
        if (Check()) {
            AlertDialog dialog = SaveDialog();
            dialog.show();
        } else {
            finish();
        }
    }

    private boolean Check() {
        if (!ExperienceEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getPE(this))) {
            return true;
        } else if (!ProjectsEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getPP(this))) {
            return true;
        } else if (!SkillsEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getSkills(this))) {
            return true;
        } else if (!OtherEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getAbout(this))) {
            return true;
        }
        return false;
    }

    public void CancelWorkerDetailsClicked(View view) {
        hideKeyboard(this);
        if (Check()) {
            AlertDialog dialog = CancelDialog();
            dialog.show();
        } else {
            finish();
        }
    }

    private AlertDialog CancelDialog() {
        final AlertDialog LogoutDialog = new AlertDialog.Builder(this)
                .setTitle("إلغاء")
                .setMessage("هل تريد إلغاء التغييرات ؟")
                .setIcon(R.drawable.ic_cancel)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        LogoutDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                LogoutDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Red));
            }
        });

        return LogoutDialog;
    }

    private AlertDialog SaveDialog() {
        final AlertDialog LogoutDialog = new AlertDialog.Builder(this)
                .setTitle("حفظ")
                .setMessage("هل تريد حفظ التغييرات ؟")
                .setIcon(R.drawable.ic_check)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        UploadToFireBase();
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        LogoutDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                LogoutDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        return LogoutDialog;
    }

    private void UploadToFireBase() {
        if (Connected()) {
            progressBarWorkerAdviserCVActivity.setVisibility(View.VISIBLE);
            Clickable(false);
            user = new HashMap<>();
            user.put("PE", MainActivity.SaveSharedPreference.getPE(context));
            user.put("PP", MainActivity.SaveSharedPreference.getPP(context));
            user.put("Skills", MainActivity.SaveSharedPreference.getSkills(context));
            user.put("About", MainActivity.SaveSharedPreference.getAbout(context));

            db.collection("Users")
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                    .update("PE", ExperienceEditText.getText().toString().trim(),
                            "PP", ProjectsEditText.getText().toString().trim(),
                            "Skills", SkillsEditText.getText().toString().trim(),
                            "About", OtherEditText.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            MainActivity.SaveSharedPreference.setPhoneNumber(context, MainActivity.SaveSharedPreference.getPhoneNumber(AdviserCvActivity.this));
                            ChangeSaveSharedPreference();
                            Clickable(true);
                            progressBarWorkerAdviserCVActivity.setVisibility(View.GONE);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Clickable(true);
                    progressBarWorkerAdviserCVActivity.setVisibility(View.GONE);
                    Toast.makeText(AdviserCvActivity.this, R.string.FailToSignUpMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void ChangeSaveSharedPreference() {
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
    }

    public void Clickable(boolean b) {
        if (b) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

}