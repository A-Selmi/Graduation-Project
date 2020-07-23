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
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class PhoneNumberActivity extends AppCompatActivity {

    EditText VerificayioncodeEditText;
    Button PhoneNumberVerificationButton;
    FirebaseFirestore db;
    Context context;
    ProgressBar progressBarPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        findView();
        progressBarPhoneNumber.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        progressBarPhoneNumber.setVisibility(View.GONE);
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

    public void PhoneNumberVerificationButtonClicked(View view) {
        findView();
        hideKeyboard(this);
        if (CheckState()) {
            Clickable(false);
            progressBarPhoneNumber.setVisibility(View.VISIBLE);
            CheckUser();
        }
    }

    private void CheckUser() {
        if (Connected()) {
            db.collection("Users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getId().equals("+962" + VerificayioncodeEditText.getText().toString().trim())) {
                                        Clickable(true);
                                        progressBarPhoneNumber.setVisibility(View.GONE);
                                        Toast.makeText(context, "هذا المستخدم موجود", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                AlertDialog dialog = PhoneNumberDialog();
                                dialog.show();
                            }
                        }
                    });
        } else {
            Toast.makeText(context, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        VerificayioncodeEditText = findViewById(R.id.VerificayioncodeEditText);
        PhoneNumberVerificationButton = findViewById(R.id.CheckPhoneNumberButtonText);
        context = this;
        progressBarPhoneNumber = findViewById(R.id.progressBarPhoneNumber);
    }

    private boolean CheckState() {
        if (VerificayioncodeEditText.getText().toString().trim().length() == 0) {
            VerificayioncodeEditText.setError("هذا الحقل مطلوب !");
            VerificayioncodeEditText.requestFocus();
            return false;
        }
        if (VerificayioncodeEditText.getText().toString().trim().length() != 9) {
            VerificayioncodeEditText.setError("الرقم غير صحيح !");
            VerificayioncodeEditText.requestFocus();
            return false;
        }
        if (VerificayioncodeEditText.getText().toString().trim().charAt(0) != '7' ||
                (VerificayioncodeEditText.getText().toString().trim().charAt(1) != '7' &&
                        VerificayioncodeEditText.getText().toString().trim().charAt(1) != '8' &&
                        VerificayioncodeEditText.getText().toString().trim().charAt(1) != '9')) {
            VerificayioncodeEditText.setError("الرقم غير صحيح !");
            VerificayioncodeEditText.requestFocus();
            return false;
        }
        return true;
    }

    private AlertDialog PhoneNumberDialog() {
        AlertDialog PhoneNumberDialog = new AlertDialog.Builder(this)
                .setTitle("التأكيد")
                .setMessage("هل أنت متأكد من رقم الهاتف ؟")
                .setIcon(R.drawable.ic_check)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Clickable(true);
                        progressBarPhoneNumber.setVisibility(View.GONE);
                        Intent toVerificationActivity = new Intent(PhoneNumberActivity.this, VerificationActivity.class);
                        toVerificationActivity.putExtra("phoneNumber", VerificayioncodeEditText.getText().toString().trim());
                        toVerificationActivity.putExtra("uri", getIntent().getStringExtra("uri"));
                        startActivity(toVerificationActivity);
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Clickable(true);
                        progressBarPhoneNumber.setVisibility(View.GONE);
                    }
                })
                .create();
        PhoneNumberDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
            }
        });

        return PhoneNumberDialog;
    }

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    public void Clickable(boolean b) {
        if (b) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public static void hideKeyboard(PhoneNumberActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(PhoneNumberActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}