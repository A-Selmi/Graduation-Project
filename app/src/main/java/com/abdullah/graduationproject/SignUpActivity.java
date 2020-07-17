package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Context context = this;
    EditText FirstNameEditText, LastNameEditText, AgeEditText,
            PasswordEditText, DoubleCheckPasswordEditText, PhoneNumberEditText;
    Spinner LocationSpinner, RoleSpinner;
    Button SignUpButton, SaveButtonSignUp, CancelButtonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        Clickable(true);
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
        if (PhoneNumberEditText.getText().toString().trim().length() == 0) {
            PhoneNumberEditText.setError("هذا الحقل مطلوب !");
            PhoneNumberEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        FirstNameEditText = findViewById(R.id.FirstNameEditText);
        LastNameEditText = findViewById(R.id.LastNameEditText);
        AgeEditText = findViewById(R.id.AgeEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        DoubleCheckPasswordEditText = findViewById(R.id.DoubleCheckPasswordEditText);
        PhoneNumberEditText = findViewById(R.id.PhoneNumberEditText);
        LocationSpinner = findViewById(R.id.LocationSpinner);
        RoleSpinner = findViewById(R.id.RoleSpinner);
        SignUpButton = findViewById(R.id.SignUpButton);
        SaveButtonSignUp = findViewById(R.id.SaveButtonSignUp);
        CancelButtonSignUp = findViewById(R.id.CancelButtonSignUp);
    }

    public void SignUpActivityButtonClicked(View view) {
        findView();
        if (CheckState()) {
            // verification phone Number
            if (RoleSpinner.getSelectedItem().toString().equals("مزارع عامل") ||
                    RoleSpinner.getSelectedItem().toString().equals("مهندس زراعي")) {
                Intent toAdviserCvActivity = new Intent(this, AdviserCvActivity.class);
                toAdviserCvActivity.putExtra("Role", RoleSpinner.getSelectedItem().toString());
                startActivity(toAdviserCvActivity);
            } else {
                // verification phone Number
                if (PasswordEditText.getText().toString().trim().equals(DoubleCheckPasswordEditText.getText().toString().trim())) {
                    if (Connected()) {
                        Clickable(false);
                        Map<String, Object> user = new HashMap<>();
                        user.put("First Name", FirstNameEditText.getText().toString().trim());
                        user.put("Last Name", LastNameEditText.getText().toString().trim());
                        user.put("Location ", LocationSpinner.getSelectedItem().toString().trim());
                        user.put("Age", AgeEditText.getText().toString().trim());
                        user.put("Password", PasswordEditText.getText().toString().trim());
                        user.put("Role", RoleSpinner.getSelectedItem().toString().trim());

                        db.collection("Users").document(PhoneNumberEditText.getText().toString().trim())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        MainActivity.SaveSharedPreference.setLogIn(context, "true");
                                        MainActivity.SaveSharedPreference.setUserName(context, FirstNameEditText.getText().toString().trim() + " " +
                                                LastNameEditText.getText().toString().trim());
                                        MainActivity.SaveSharedPreference.setLocation(context, LocationSpinner.getSelectedItem().toString().trim());
                                        MainActivity.SaveSharedPreference.setAge(context, AgeEditText.getText().toString().trim());
                                        MainActivity.SaveSharedPreference.setPhoneNumber(context, PhoneNumberEditText.getText().toString().trim());
                                        MainActivity.SaveSharedPreference.setRole(context, RoleSpinner.getSelectedItem().toString().trim());
                                        Clickable(true);
                                        finish();
                                        LoginActivity.getInstance().finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, R.string.FailToSignUpMessage, Toast.LENGTH_SHORT).show();
                                        Clickable(true);
                                    }
                                });
                    } else {
                        Toast.makeText(context, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    PasswordEditText.setError("يرجى التحقق من كلمة المرور !");
                    PasswordEditText.requestFocus();
                    DoubleCheckPasswordEditText.setError("يرجى التحقق من كلمة المرور !");
                    DoubleCheckPasswordEditText.requestFocus();
                }
            }
        }
    }

    public void SaveButtonSignUpClicked(View view) {
        Toast.makeText(this, "Save!", Toast.LENGTH_SHORT).show();
    }

    public void CancelButtonSignUpClicked(View view) {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
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