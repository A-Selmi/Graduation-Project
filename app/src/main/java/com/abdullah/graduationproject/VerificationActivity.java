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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    EditText VerificayioncodeEditText;
    Button CheckPhoneNumberButton;
    ProgressBar progressBarVerification;
    String phoneNumber;
    String code;
    Context context;
    FirebaseFirestore db;
    Map<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        progressBarVerification.setVisibility(View.GONE);
        sendVerificationCode(phoneNumber);
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        VerificayioncodeEditText = findViewById(R.id.VerificayioncodeEditText);
        CheckPhoneNumberButton = findViewById(R.id.CheckPhoneNumberButton);
        progressBarVerification = findViewById(R.id.progressBarVerification);
        phoneNumber = "+962" + getIntent().getStringExtra("phoneNumber");
        context = this;
    }

    public void CheckPhoneNumberButtonClicked(View view) {
        hideKeyboard(this);
        if (VerificayioncodeEditText.getText().toString().trim().length() == 0) {
            VerificayioncodeEditText.setError("هذا الحقل مطلوب !");
            VerificayioncodeEditText.requestFocus();
        } else {
            verifyCode(VerificayioncodeEditText.getText().toString().trim());
        }
    }

    private void verifyCode(String verificationcode) {
        if (verificationcode.equals(code)) {
            UploadToDateBase();
        } else {
            Toast.makeText(this, "رمز التحقق خاطئ", Toast.LENGTH_SHORT).show();
            progressBarVerification.setVisibility(View.GONE);
            Clickable(true);
        }

    }

    private void UploadToDateBase() {
        if (Connected()) {
            progressBarVerification.setVisibility(View.VISIBLE);
            Clickable(false);
            user = new HashMap<>();
            user.put("First Name", MainActivity.SaveSharedPreference.getFirstName(context));
            user.put("Last Name", MainActivity.SaveSharedPreference.getLastName(context));
            user.put("Location ", MainActivity.SaveSharedPreference.getLocation(context));
            user.put("Age", MainActivity.SaveSharedPreference.getAge(context));
            user.put("Role", MainActivity.SaveSharedPreference.getRole(context));
            user.put("Password", MainActivity.SaveSharedPreference.getPassword(context));

            if (MainActivity.SaveSharedPreference.getRole(context).equals("4")) {
                user.put("PE ", MainActivity.SaveSharedPreference.getPE(context));
                user.put("PP", MainActivity.SaveSharedPreference.getPP(context));
                user.put("Skills", MainActivity.SaveSharedPreference.getSkills(context));
                user.put("About", MainActivity.SaveSharedPreference.getAbout(context));
            } else if (MainActivity.SaveSharedPreference.getRole(context).equals("3")) {
                // TODO Upload CV
            }

            db.collection("Users").document(phoneNumber)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            MainActivity.SaveSharedPreference.setLogIn(context, "true");
                            MainActivity.SaveSharedPreference.setPhoneNumber(context, phoneNumber);
                            Clickable(true);
                            progressBarVerification.setVisibility(View.GONE);
                            Intent backMainActivity = new Intent(VerificationActivity.this, MainActivity.class);
                            backMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(backMainActivity);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MainActivity.SaveSharedPreference.setLogIn(context, "false");
                            Clickable(true);
                            progressBarVerification.setVisibility(View.GONE);
                            Toast.makeText(VerificationActivity.this, R.string.FailToSignUpMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
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

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                5,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                VerificayioncodeEditText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
        }
    };

    public static void hideKeyboard(VerificationActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(VerificationActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}