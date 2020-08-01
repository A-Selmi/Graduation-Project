package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdullah.graduationproject.LogInActivities.LoginActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    EditText TitleAddPostEditText, DetailsAddPostEditText;
    ProgressBar progressBarAddPostActivity;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
    }

    private void findView() {
        TitleAddPostEditText = findViewById(R.id.TitleAddPostEditText);
        DetailsAddPostEditText = findViewById(R.id.DetailsAddPostEditText);
        progressBarAddPostActivity = findViewById(R.id.progressBarAddPostActivity);
        db = FirebaseFirestore.getInstance();
    }

    public void AddPostButtonClicked(View view) {
        hideKeyboard(this);
        if(Check()) {
            if(Connected()) {
                UploadToDataBase();
            }else {
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadToDataBase() {
        Clickable(false);
        progressBarAddPostActivity.setVisibility(View.VISIBLE);
        Map<String, Object> user = new HashMap<>();
        user.put("Title", TitleAddPostEditText.getText().toString().trim());
        user.put("Text", DetailsAddPostEditText.getText().toString().trim());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        user.put("Date", formattedDate);

        db.collection(getString(R.string.UsersCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .collection(getString(R.string.PostsCollection)).document()
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddPostActivity.this, "تم إضافة المنشور", Toast.LENGTH_SHORT).show();
                        Clickable(true);
                        progressBarAddPostActivity.setVisibility(View.GONE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostActivity.this, "حدث خطأ أثناء إضافة المنشور", Toast.LENGTH_SHORT).show();
                Clickable(true);
                progressBarAddPostActivity.setVisibility(View.GONE);
            }
        });
    }

    private boolean Check() {
        if(TitleAddPostEditText.getText().toString().trim().isEmpty()) {
            TitleAddPostEditText.setError("هذا الحقل مطلوب!");
            TitleAddPostEditText.requestFocus();
            return false;
        }
        if(DetailsAddPostEditText.getText().toString().trim().isEmpty()) {
            DetailsAddPostEditText.setError("هذا الحقل مطلوب!");
            DetailsAddPostEditText.requestFocus();
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}