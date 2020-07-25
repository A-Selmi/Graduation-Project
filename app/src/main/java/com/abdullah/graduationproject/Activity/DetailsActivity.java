package com.abdullah.graduationproject.Activity;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.LogInActivities.LoginActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    RatingBar RatingBarDetails;
    EditText ReviewEditText;
    FirebaseFirestore db;
    TextView TitleTextViewDetails;
    ProgressBar DetailsActivityProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
    }

    private void findView() {
        RatingBarDetails = findViewById(R.id.RatingBarDetails);
        ReviewEditText = findViewById(R.id.ReviewEditText);
        db = FirebaseFirestore.getInstance();
        TitleTextViewDetails = findViewById(R.id.TitleTextViewDetails);
        DetailsActivityProgressBar = findViewById(R.id.DetailsActivityProgressBar);
    }

    public void ReviewButtonClicked(View view) {
        hideKeyboard(this);
        if (RatingBarDetails.getRating() > 0) {
            if (Connected()) {
                UploadToFireBase();
            } else {
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "يرجى التقييم", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadToFireBase() {
        try {
            Clickable(false);
            DetailsActivityProgressBar.setVisibility(View.VISIBLE);
            Map<String, Object> user = new HashMap<>();
            user.put("Reviewer Name", MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            user.put("Review Rate", String.valueOf((int) RatingBarDetails.getRating()));
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            user.put("Review Date", formattedDate);
            if (ReviewEditText.getText().toString().trim().isEmpty()) {
                user.put("Review Text", "");
            } else {
                user.put("Review Text", ReviewEditText.getText().toString().trim());
            }

            db.collection("Category").document(TitleTextViewDetails.getText().toString().trim())
                    .collection("Items").document()
                    .collection("Review").document(MainActivity.SaveSharedPreference.getPhoneNumber(DetailsActivity.this))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailsActivity.this, "تم إضافة التقييم", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            DetailsActivityProgressBar.setVisibility(View.GONE);
                            RatingBarDetails.setRating(0);
                            ReviewEditText.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailsActivity.this, "حدث خطأ أثناء إضافة التقييم", Toast.LENGTH_SHORT).show();
                    Clickable(true);
                    DetailsActivityProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}