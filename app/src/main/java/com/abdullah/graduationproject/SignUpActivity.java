package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    Context context = this;
    EditText FirstNameEditText, LastNameEditText, AgeEditText,
            PasswordEditText, DoubleCheckPasswordEditText;
    Spinner LocationSpinner, RoleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
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
        FirstNameEditText = findViewById(R.id.FirstNameEditText);
        LastNameEditText = findViewById(R.id.LastNameEditText);
        AgeEditText = findViewById(R.id.AgeEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        DoubleCheckPasswordEditText = findViewById(R.id.DoubleCheckPasswordEditText);
        LocationSpinner = findViewById(R.id.LocationSpinner);
        RoleSpinner = findViewById(R.id.RoleSpinner);
    }

    public void NextButtonButtonClicked(View view) {
        hideKeyboard(this);
        findView();
        if (CheckState()) {
            MainActivity.SaveSharedPreference.setFirstName(context, FirstNameEditText.getText().toString().trim());
            MainActivity.SaveSharedPreference.setLastName(context, LastNameEditText.getText().toString().trim());
            MainActivity.SaveSharedPreference.setLocation(context, LocationSpinner.getSelectedItem().toString().trim());
            MainActivity.SaveSharedPreference.setAge(context, AgeEditText.getText().toString().trim());
            if(RoleSpinner.getSelectedItem().toString().trim().equals("مستخدم")) {
                MainActivity.SaveSharedPreference.setRole(context, "1");
            }else if(RoleSpinner.getSelectedItem().toString().trim().equals("صاحب عمل")) {
                MainActivity.SaveSharedPreference.setRole(context, "2");
            }else if(RoleSpinner.getSelectedItem().toString().trim().equals("مهندس زراعي")) {
                MainActivity.SaveSharedPreference.setRole(context, "3");
            }else {
                MainActivity.SaveSharedPreference.setRole(context, "4");
            }
            MainActivity.SaveSharedPreference.setPassword(context, PasswordEditText.getText().toString().trim());
            if (RoleSpinner.getSelectedItem().toString().equals("مزارع عامل") ||
                    RoleSpinner.getSelectedItem().toString().equals("مهندس زراعي")) {
                Intent toAdviserCvActivity = new Intent(this, AdviserCvActivity.class);
                toAdviserCvActivity.putExtra("Role", RoleSpinner.getSelectedItem().toString());
                startActivity(toAdviserCvActivity);
            } else {
                Intent toPhoneNumberActivity = new Intent(this, PhoneNumberActivity.class);
                startActivity(toPhoneNumberActivity);
            }
        }
    }

    public void SaveButtonSignUpClicked(View view) {
        Toast.makeText(this, "Save!", Toast.LENGTH_SHORT).show();
    }

    public void CancelButtonSignUpClicked(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(SignUpActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(SignUpActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}