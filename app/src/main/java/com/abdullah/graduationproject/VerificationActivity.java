package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Uri pdfUri;

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
        Toast.makeText(context, "جاري إرسال الرمز", Toast.LENGTH_SHORT).show();
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        VerificayioncodeEditText = findViewById(R.id.VerificayioncodeEditText);
        CheckPhoneNumberButton = findViewById(R.id.CheckPhoneNumberButton);
        progressBarVerification = findViewById(R.id.progressBarVerification);
        phoneNumber = "+962" + getIntent().getStringExtra("phoneNumber");
        context = this;
        if (MainActivity.SaveSharedPreference.getRole(context).equals("3")) {
            pdfUri = Uri.parse(getIntent().getStringExtra("uri"));
        }
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void CheckPhoneNumberButtonClicked(View view) {
        hideKeyboard(this);
        if (VerificayioncodeEditText.getText().toString().trim().length() == 0) {
            VerificayioncodeEditText.setError("هذا الحقل مطلوب !");
            VerificayioncodeEditText.requestFocus();
        } else if (VerificayioncodeEditText.getText().toString().trim().length() < 6) {
            VerificayioncodeEditText.setError("الرمز غير صالح ");
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
            UploadThePDFFile();
            user = new HashMap<>();
            user.put("First Name", MainActivity.SaveSharedPreference.getFirstName(context));
            user.put("Last Name", MainActivity.SaveSharedPreference.getLastName(context));
            user.put("Location", MainActivity.SaveSharedPreference.getLocation(context));
            user.put("Age", MainActivity.SaveSharedPreference.getAge(context));
            user.put("Role", MainActivity.SaveSharedPreference.getRole(context));
            user.put("Password", MainActivity.SaveSharedPreference.getPassword(context));

            if (MainActivity.SaveSharedPreference.getRole(context).equals("4")) {
                user.put("PE", MainActivity.SaveSharedPreference.getPE(context));
                user.put("PP", MainActivity.SaveSharedPreference.getPP(context));
                user.put("Skills", MainActivity.SaveSharedPreference.getSkills(context));
                user.put("About", MainActivity.SaveSharedPreference.getAbout(context));
            } else if (MainActivity.SaveSharedPreference.getRole(context).equals("3")) {
                try {
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
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
                3,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks
        );
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                3,
                TimeUnit.SECONDS,
                this,
                callbacks,
                token);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            resendingToken = forceResendingToken;
        }

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
            Toast.makeText(VerificationActivity.this, "حدث خطأ, يرجى المحاولة لاحقًا", Toast.LENGTH_SHORT).show();
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

    public void ResendVerificationCodeClicked(View view) {
        resendVerificationCode(phoneNumber, resendingToken);
        Toast.makeText(context, "جاري إعادة إرسال الرمز", Toast.LENGTH_SHORT).show();
    }

    public void UploadThePDFFile() {
        if (pdfUri != null) {
            uploudFile(pdfUri);
        } else {
            Toast.makeText(this, "اختار الملف ", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploudFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("تحميل الملف ");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();
        storageReference.child("Uploads").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                DatabaseReference reference = database.getReference();
                reference.child(fileName).setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "تم تحميل الملف", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "حدث خطأ أثناء تحميل الملف", Toast.LENGTH_SHORT).show();
                Intent backMainActivity = new Intent(VerificationActivity.this, MainActivity.class);
                backMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(backMainActivity);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }
}