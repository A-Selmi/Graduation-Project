package com.abdullah.graduationproject.LogInActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Activity.MainActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    Context context = this;
    EditText FirstNameEditText, LastNameEditText, AgeEditText,
            PasswordEditText, DoubleCheckPasswordEditText;
    Spinner LocationSpinner, RoleSpinner;
    Button NextButton, SaveButtonSignUp, CancelButtonSignUp;
    ProgressBar progressBarSignUpActivity;
    FirebaseFirestore db;
    Map<String, Object> user;
    TextView RoleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        progressBarSignUpActivity.setVisibility(View.GONE);
        setAppLocale("ar");
        if (MainActivity.SaveSharedPreference.getLogIn(this).equals("true")) {
            NextButton.setVisibility(View.GONE);
            SaveButtonSignUp.setVisibility(View.VISIBLE);
            CancelButtonSignUp.setVisibility(View.VISIBLE);
            RoleSpinner.setVisibility(View.GONE);
            RoleTextView.setVisibility(View.GONE);
            setData();
        } else {
            NextButton.setVisibility(View.VISIBLE);
            SaveButtonSignUp.setVisibility(View.GONE);
            CancelButtonSignUp.setVisibility(View.GONE);
            RoleSpinner.setVisibility(View.VISIBLE);
            RoleTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setData() {
        FirstNameEditText.setText(MainActivity.SaveSharedPreference.getFirstName(this));
        LastNameEditText.setText(MainActivity.SaveSharedPreference.getLastName(this));
        AgeEditText.setText(MainActivity.SaveSharedPreference.getAge(this));
        PasswordEditText.setText(MainActivity.SaveSharedPreference.getPassword(this));
        DoubleCheckPasswordEditText.setText(MainActivity.SaveSharedPreference.getPassword(this));
        if (MainActivity.SaveSharedPreference.getLocation(this).equals("عمان")) {
            LocationSpinner.setSelection(0);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("السلط")) {
            LocationSpinner.setSelection(1);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("إربد")) {
            LocationSpinner.setSelection(2);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("جرش")) {
            LocationSpinner.setSelection(3);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("عجلون")) {
            LocationSpinner.setSelection(4);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("الزرقاء")) {
            LocationSpinner.setSelection(5);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("المفرق")) {
            LocationSpinner.setSelection(6);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("مأدبا")) {
            LocationSpinner.setSelection(7);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("الكرك")) {
            LocationSpinner.setSelection(8);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("الطفيلة")) {
            LocationSpinner.setSelection(9);
        } else if (MainActivity.SaveSharedPreference.getLocation(this).equals("معان")) {
            LocationSpinner.setSelection(10);
        } else {
            LocationSpinner.setSelection(11);
        }
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

    private boolean CheckState() {
        if (FirstNameEditText.getText().toString().trim().length() == 0) {
            FirstNameEditText.setError("هذا الحقل مطلوب !");
            FirstNameEditText.requestFocus();
            return false;
        }
        if (LastNameEditText.getText().toString().trim().length() == 0) {
            LastNameEditText.setError("هذا الحقل مطلوب !");
            LastNameEditText.requestFocus();
            return false;
        }
        if (AgeEditText.getText().toString().trim().length() == 0) {
            AgeEditText.setError("هذا الحقل مطلوب !");
            AgeEditText.requestFocus();
            return false;
        }
        if (PasswordEditText.getText().toString().trim().length() == 0) {
            PasswordEditText.setError("هذا الحقل مطلوب !");
            PasswordEditText.requestFocus();
            return false;
        }
        if (DoubleCheckPasswordEditText.getText().toString().trim().length() == 0) {
            DoubleCheckPasswordEditText.setError("هذا الحقل مطلوب !");
            DoubleCheckPasswordEditText.requestFocus();
            return false;
        }
        if (!PasswordEditText.getText().toString().trim().equals(DoubleCheckPasswordEditText.getText().toString().trim())) {
            PasswordEditText.setError("يرجى التحقق من كلمة المرور !");
            PasswordEditText.requestFocus();
            DoubleCheckPasswordEditText.setError("يرجى التحقق من كلمة المرور !");
            DoubleCheckPasswordEditText.requestFocus();
            return false;
        }
        return true;
    }

    public void findView() {
        db = FirebaseFirestore.getInstance();
        FirstNameEditText = findViewById(R.id.FirstNameEditText);
        LastNameEditText = findViewById(R.id.LastNameEditText);
        AgeEditText = findViewById(R.id.AgeEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        DoubleCheckPasswordEditText = findViewById(R.id.DoubleCheckPasswordEditText);
        LocationSpinner = findViewById(R.id.LocationSpinner);
        RoleSpinner = findViewById(R.id.RoleSpinner);
        RoleTextView = findViewById(R.id.RoleTextView);
        NextButton = findViewById(R.id.NextButton);
        SaveButtonSignUp = findViewById(R.id.SaveButtonSignUp);
        CancelButtonSignUp = findViewById(R.id.CancelButtonSignUp);
        progressBarSignUpActivity = findViewById(R.id.progressBarSignUpActivity);
    }

    public void NextButtonButtonClicked(View view) {
        hideKeyboard(this);
        findView();
        if (CheckState()) {
            MainActivity.SaveSharedPreference.setFirstName(context, FirstNameEditText.getText().toString().trim());
            MainActivity.SaveSharedPreference.setLastName(context, LastNameEditText.getText().toString().trim());
            MainActivity.SaveSharedPreference.setLocation(context, LocationSpinner.getSelectedItem().toString().trim());
            MainActivity.SaveSharedPreference.setAge(context, AgeEditText.getText().toString().trim());
            if (RoleSpinner.getSelectedItem().toString().trim().equals("مستخدم")) {
                MainActivity.SaveSharedPreference.setRole(context, "1");
            } else if (RoleSpinner.getSelectedItem().toString().trim().equals("صاحب عمل")) {
                MainActivity.SaveSharedPreference.setRole(context, "2");
            } else if (RoleSpinner.getSelectedItem().toString().trim().equals("مهندس زراعي")) {
                MainActivity.SaveSharedPreference.setRole(context, "3");
            } else {
                MainActivity.SaveSharedPreference.setRole(context, "4");
            }
            MainActivity.SaveSharedPreference.setPassword(context, PasswordEditText.getText().toString().trim());
            if (RoleSpinner.getSelectedItem().toString().equals("مزارع عامل")) {
                Intent toAdviserCvActivity = new Intent(this, AdviserCvActivity.class);
                toAdviserCvActivity.putExtra("Role", "1");
                toAdviserCvActivity.putExtra("S", "");
                startActivity(toAdviserCvActivity);
            } else if (RoleSpinner.getSelectedItem().toString().equals("مهندس زراعي")) {
                Intent toAdviserCvActivity = new Intent(this, AdviserCvActivity.class);
                toAdviserCvActivity.putExtra("Role", "2");
                toAdviserCvActivity.putExtra("S", "");
                startActivity(toAdviserCvActivity);
            } else {
                Intent toPhoneNumberActivity = new Intent(this, PhoneNumberActivity.class);
                startActivity(toPhoneNumberActivity);
            }
        }
    }

    public void SaveButtonSignUpClicked(View view) {
        hideKeyboard(this);
        if (Check()) {
            if (CheckState()) {
                AlertDialog dialog = SaveDialog();
                dialog.show();
            }
        } else {
            MainActivity.SaveSharedPreference.setFragment(SignUpActivity.this, "1");
            finish();
        }
    }

    private boolean Check() {
        if (!FirstNameEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getFirstName(this))) {
            return true;
        } else if (!LastNameEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getLastName(this))) {
            return true;
        } else if (!AgeEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getAge(this))) {
            return true;
        } else if (!PasswordEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getPassword(this))) {
            return true;
        } else if (!DoubleCheckPasswordEditText.getText().toString().trim().equals(MainActivity.SaveSharedPreference.getPassword(this))) {
            return true;
        } else if (!LocationSpinner.getSelectedItem().toString().equals(MainActivity.SaveSharedPreference.getLocation(this))) {
            return true;
        }
        return false;
    }

    public void CancelButtonSignUpClicked(View view) {
        hideKeyboard(this);
        if (Check()) {
            AlertDialog dialog = CancelDialog();
            dialog.show();
        } else {
            MainActivity.SaveSharedPreference.setFragment(SignUpActivity.this, "1");
            finish();
        }
    }

    public static void hideKeyboard(SignUpActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(SignUpActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getIntent().getStringExtra("state").equals("Edit")) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                MainActivity.SaveSharedPreference.setFragment(this, "1");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private AlertDialog CancelDialog() {
        final AlertDialog LogoutDialog = new AlertDialog.Builder(this)
                .setTitle("إلغاء")
                .setMessage("هل تريد إلغاء التغييرات ؟")
                .setIcon(R.drawable.ic_cancel)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        MainActivity.SaveSharedPreference.setFragment(SignUpActivity.this, "1");
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
            progressBarSignUpActivity.setVisibility(View.VISIBLE);
            Clickable(false);
            user = new HashMap<>();
            db.collection("Users")
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                    .update("First Name", FirstNameEditText.getText().toString().trim(),
                            "Last Name", LastNameEditText.getText().toString().trim(),
                            "Location", LocationSpinner.getSelectedItem().toString().trim(),
                            "Age", AgeEditText.getText().toString().trim(),
                            "Password", PasswordEditText.getText().toString().trim())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Clickable(true);
                                            ChangeSaveSharedPreference();
                                            Clickable(true);
                                            progressBarSignUpActivity.setVisibility(View.GONE);
                                            MainActivity.SaveSharedPreference.setFragment(SignUpActivity.this, "1");
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Clickable(true);
                                            progressBarSignUpActivity.setVisibility(View.GONE);
                                            Toast.makeText(SignUpActivity.this, R.string.FailToSignUpMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    });
        }else {
            Toast.makeText(context, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void ChangeSaveSharedPreference() {
        MainActivity.SaveSharedPreference.setFirstName(context, FirstNameEditText.getText().toString().trim());
        MainActivity.SaveSharedPreference.setLastName(context, LastNameEditText.getText().toString().trim());
        MainActivity.SaveSharedPreference.setLocation(context, LocationSpinner.getSelectedItem().toString().trim());
        MainActivity.SaveSharedPreference.setAge(context, AgeEditText.getText().toString().trim());
        if (RoleSpinner.getSelectedItem().toString().trim().equals("مستخدم")) {
            MainActivity.SaveSharedPreference.setRole(context, "1");
        } else if (RoleSpinner.getSelectedItem().toString().trim().equals("صاحب عمل")) {
            MainActivity.SaveSharedPreference.setRole(context, "2");
        } else if (RoleSpinner.getSelectedItem().toString().trim().equals("مهندس زراعي")) {
            MainActivity.SaveSharedPreference.setRole(context, "3");
        } else {
            MainActivity.SaveSharedPreference.setRole(context, "4");
        }
        MainActivity.SaveSharedPreference.setPassword(context, PasswordEditText.getText().toString().trim());
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