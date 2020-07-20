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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText EmailEditText, PasswordEditText;
    FirebaseFirestore db;
    ProgressBar progressBarLogin;
    String phoneNumber;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findview();
        Clickable(true);
        progressBarLogin.setVisibility(View.GONE);
    }

    private void findview() {
        db = FirebaseFirestore.getInstance();
        EmailEditText = findViewById(R.id.EmailEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        context = this;
    }

    public void SignUpButtonClicked(View view) {
        hideKeyboard(this);
        Intent toSignUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(toSignUpActivity);
    }

    public void SignInButtonClicked(View view) {
        hideKeyboard(this);
        if (CheckState()) {
            if (Connected()) {
                Clickable(false);
                progressBarLogin.setVisibility(View.VISIBLE);
                phoneNumber = "+962" + EmailEditText.getText().toString().trim().substring(1);
                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getId().equals(phoneNumber) && document.getData().get("Password").equals(PasswordEditText.getText().toString().trim())) {
                                            MainActivity.SaveSharedPreference.setLogIn(context, "true");
                                            MainActivity.SaveSharedPreference.setFirstName(context, document.getData().get("First Name").toString());
                                            MainActivity.SaveSharedPreference.setLastName(context, document.getData().get("Last Name").toString());
                                            MainActivity.SaveSharedPreference.setLocation(context, document.getData().get("Location").toString());
                                            MainActivity.SaveSharedPreference.setAge(context, document.getData().get("Age").toString());
                                            MainActivity.SaveSharedPreference.setRole(context, document.getData().get("Role").toString());
                                            MainActivity.SaveSharedPreference.setPassword(context, document.getData().get("Password").toString());
                                            if (document.getData().get("Role").toString().equals("4")) {
                                                MainActivity.SaveSharedPreference.setPE(context, document.getData().get("PE").toString());
                                                MainActivity.SaveSharedPreference.setPP(context, document.getData().get("PP").toString());
                                                MainActivity.SaveSharedPreference.setSkills(context, document.getData().get("Skills").toString());
                                                MainActivity.SaveSharedPreference.setAbout(context, document.getData().get("About").toString());
                                            }
                                            Clickable(true);
                                            progressBarLogin.setVisibility(View.GONE);
                                            finish();
                                            return;
                                        }
                                    }
                                    Clickable(true);
                                    progressBarLogin.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "هذا المستخدم غير موجود", Toast.LENGTH_SHORT).show();
                                } else {
                                    Clickable(true);
                                    progressBarLogin.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "حدث خطأ خلال تسجيل الدخول", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean CheckState() {
        if (EmailEditText.getText().toString().trim().length() == 0) {
            EmailEditText.setError("هذا الحقل مطلوب !");
            EmailEditText.requestFocus();
            return false;
        }
        if (EmailEditText.getText().toString().trim().length() != 10) {
            EmailEditText.setError("الرقم غير صحيح !");
            EmailEditText.requestFocus();
            return false;
        }
        if (EmailEditText.getText().toString().trim().charAt(0) != '0' ||
                EmailEditText.getText().toString().trim().charAt(1) != '7' ||
                (EmailEditText.getText().toString().trim().charAt(2) != '7' &&
                        EmailEditText.getText().toString().trim().charAt(2) != '8' &&
                        EmailEditText.getText().toString().trim().charAt(2) != '9')) {
            EmailEditText.setError("الرقم غير صحيح !");
            EmailEditText.requestFocus();
            return false;
        }
        if (PasswordEditText.getText().toString().trim().length() == 0) {
            PasswordEditText.setError("هذا الحقل مطلوب !");
            PasswordEditText.requestFocus();
            return false;
        }
        return true;
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

    public static void hideKeyboard(LoginActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}