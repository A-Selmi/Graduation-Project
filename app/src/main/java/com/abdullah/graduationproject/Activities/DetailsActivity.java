package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Adapters.FAndVAdapter;
import com.abdullah.graduationproject.Adapters.ReviewsAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Reviews;
import com.abdullah.graduationproject.LogInActivities.LoginActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    ConstraintLayout DetailsLayout;
    TextView TitleTextViewDetails, ItemNameTextViewDetails, PersonNameTextViewDetails,
            LocationTextViewDetailsActivity, PriceTextViewDetails, PhoneNumberTextViewDetails,
            DiscriptionTextViewDetails;
    RatingBar RatingBarDetails;
    EditText ReviewEditText;
    FirebaseFirestore db;
    ProgressBar DetailsActivityProgressBar;
    long counterDatabase = 0;
    String IntentId, IntentItemName;
    ImageView ItemImageViewDetails;
    RecyclerView ReviewRecycleView;
    ReviewsAdapter adapter;
    List<Reviews> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        SetData();
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        DetailsActivityProgressBar = findViewById(R.id.DetailsActivityProgressBar);
        TitleTextViewDetails = findViewById(R.id.TitleTextViewDetails);
        ItemNameTextViewDetails = findViewById(R.id.ItemNameTextViewDetails);
        PersonNameTextViewDetails = findViewById(R.id.PersonNameTextViewDetails);
        LocationTextViewDetailsActivity = findViewById(R.id.LocationTextViewDetailsActivity);
        PriceTextViewDetails = findViewById(R.id.PriceTextViewDetails);
        PhoneNumberTextViewDetails = findViewById(R.id.PhoneNumberTextViewDetails);
        DiscriptionTextViewDetails = findViewById(R.id.DiscriptionTextViewDetails);
        DetailsLayout = findViewById(R.id.DetailsLayout);
        RatingBarDetails = findViewById(R.id.RatingBarDetails);
        ReviewEditText = findViewById(R.id.ReviewEditText);
        IntentId = getIntent().getStringExtra("Id");
        IntentItemName = getIntent().getStringExtra("Item");
        ItemImageViewDetails = findViewById(R.id.ItemImageViewDetails);
        ReviewRecycleView = findViewById(R.id.ReviewRecycleView);
        list = new ArrayList<>();
    }

    private void SetData() {
        Clickable(false);
        DetailsActivityProgressBar.setVisibility(View.VISIBLE);
        DetailsLayout.setVisibility(View.GONE);
        db.collection(getString(R.string.ItemsCollection))
                .document(IntentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            if(IntentItemName.equals("FAndV")) {
                                TitleTextViewDetails.setText(getString(R.string.FAndV));
                            }else if(IntentItemName.equals("Seeds")) {
                                TitleTextViewDetails.setText(getString(R.string.Seeds));
                            }else if(IntentItemName.equals("Water")) {
                                TitleTextViewDetails.setText(getString(R.string.Water));
                            }else {
                                TitleTextViewDetails.setText(getString(R.string.Tools));
                            }
                            ItemNameTextViewDetails.setText(documentSnapshot.getData().get("Product Name").toString());
                            PersonNameTextViewDetails.setText(documentSnapshot.getData().get("Provider").toString());
                            LocationTextViewDetailsActivity.setText(documentSnapshot.getData().get("Location").toString());
                            PriceTextViewDetails.setText(documentSnapshot.getData().get("Price").toString() + " دينار");
                            PhoneNumberTextViewDetails.setText("0" + documentSnapshot.getData().get("Phone Number").toString().substring(4));
                            DiscriptionTextViewDetails.setText(documentSnapshot.getData().get("Description").toString());
                            Picasso.get().load(documentSnapshot.getData().get("Image Url").toString()).into(ItemImageViewDetails);
                            ReadReviews(IntentId);
                        } else {
                            Clickable(true);
                            DetailsActivityProgressBar.setVisibility(View.GONE);
                            DetailsLayout.setVisibility(View.GONE);
                            Toast.makeText(DetailsActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Clickable(true);
                        DetailsActivityProgressBar.setVisibility(View.GONE);
                        DetailsLayout.setVisibility(View.GONE);
                        Toast.makeText(DetailsActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ReadReviews(String intentId) {
        list.clear();
        Clickable(false);
        DetailsActivityProgressBar.setVisibility(View.VISIBLE);
        DetailsLayout.setVisibility(View.GONE);
        db.collection(getString(R.string.ItemsCollection))
                .document(intentId)
                .collection(getString(R.string.ReviewCollection))
                .orderBy("Review Date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(new Reviews(document.getId(), document.getData().get("Reviewer Name").toString()
                                        , document.getData().get("Review Rate").toString(), document.getData().get("Review Date").toString()
                                        , document.getData().get("Review Text").toString()));
                            }
                            adapter = new ReviewsAdapter(list, DetailsActivity.this);
                            ReviewRecycleView.hasFixedSize();
                            ReviewRecycleView.setAdapter(adapter);
                            ReviewRecycleView.setLayoutManager(new GridLayoutManager(DetailsActivity.this, 1));
                            adapter.notifyDataSetChanged();
                            Clickable(true);
                            DetailsActivityProgressBar.setVisibility(View.GONE);
                            DetailsLayout.setVisibility(View.VISIBLE);
                        } else {
                            Clickable(true);
                            DetailsActivityProgressBar.setVisibility(View.GONE);
                            DetailsLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(DetailsActivity.this, "حدث خطأ أثناء قرآءة ألمراجعات", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        Clickable(false);
        DetailsActivityProgressBar.setVisibility(View.VISIBLE);
        final Map<String, Object> user = new HashMap<>();
        user.put("Reviewer Name", MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                MainActivity.SaveSharedPreference.getLastName(this));
        user.put("Review Rate", String.valueOf(RatingBarDetails.getRating()));
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        user.put("Review Date", formattedDate);
        if (ReviewEditText.getText().toString().trim().isEmpty()) {
            user.put("Review Text", "");
        } else {
            user.put("Review Text", ReviewEditText.getText().toString().trim());
        }
        final Map<String, Object> user2 = new HashMap<>();
        user2.put("Review Rate", String.valueOf(RatingBarDetails.getRating()));

        db.collection(getString(R.string.UsersCollection)).document("+962" + PhoneNumberTextViewDetails.getText().toString().trim().substring(1))
                .collection(getString(R.string.ItemsCollection)).document(IntentId)
                .collection(getString(R.string.ReviewCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .set(user2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection(getString(R.string.ItemsCollection)).document(IntentId)
                                .collection(getString(R.string.ReviewCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(DetailsActivity.this))
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DetailsActivity.this, "تم إضافة التقييم", Toast.LENGTH_SHORT).show();
                                        ReadReviews(IntentId);
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
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailsActivity.this, "حدث خطأ أثناء إضافة التقييم", Toast.LENGTH_SHORT).show();
                Clickable(true);
                DetailsActivityProgressBar.setVisibility(View.GONE);
            }
        });
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