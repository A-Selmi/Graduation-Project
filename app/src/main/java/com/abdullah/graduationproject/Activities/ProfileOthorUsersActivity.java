package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Adapters.ConsultantsAdapter;
import com.abdullah.graduationproject.Adapters.DeletePostsProfileAdapter;
import com.abdullah.graduationproject.Classes.Consultants;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Posts;
import com.abdullah.graduationproject.Classes.Workers;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOthorUsersActivity extends AppCompatActivity implements DeletePostsProfileAdapter.OnPostClickListener {

    TextView AdviserLocationTextViewOther, NameTextViewProfileOther, LocationTextViewProfileOther,
            DateOfBirthTextViewProfileOther, PhoneNumberTextViewOther, AboutWorkerTextViewProfileActivityOther,
            RatingtextViewProfileOther;
    CircleImageView ProfileImageViewOther;
    RatingBar ratingBar2ProfileOther;
    RecyclerView ProfileListViewProfileActivityOther;
    ProgressBar progressBar;
    FirebaseFirestore db;
    DeletePostsProfileAdapter adapterPosts;
    List<Posts> posts;
    String Role;
    Workers workers;
    Consultants consultants;
    Map<String, Object> user;
    ScrollView ScrollView1, ScrollView2;
    ConstraintLayout ConstraintLayout;
    TextView experienceTextView3, projectsTextView3, skillsTextView3, otherTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_othor_users);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        ScrollView1.setVisibility(View.VISIBLE);
        ScrollView2.setVisibility(View.GONE);
        SetVisibility();
        SetData();
    }

    private void SetData() {
        if (Role.equals("3")) {
            NameTextViewProfileOther.setText("الإسم:" + consultants.getFirstName() + " " + consultants.getLastName());
            LocationTextViewProfileOther.setText("الموقع:" + consultants.getLocation());
            DateOfBirthTextViewProfileOther.setText("العمر:" + consultants.getAge());
            PhoneNumberTextViewOther.setText("الهاتف:0" + consultants.getID().substring(4));
            RatingtextViewProfileOther.setText(consultants.getRating());
            if(consultants.getImageUrl().equals("")) {
                ProfileImageViewOther.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
            }else {
                Picasso.get().load(consultants.getImageUrl()).into(ProfileImageViewOther);
            }
            SetRecyclerView(consultants.getID());
        } else {
            NameTextViewProfileOther.setText("الإسم:" + workers.getFirstName() + " " + workers.getLastName());
            LocationTextViewProfileOther.setText("الموقع:" + workers.getLocation());
            DateOfBirthTextViewProfileOther.setText("العمر:" + workers.getAge() + " سنة");
            PhoneNumberTextViewOther.setText("الهاتف:0" + workers.getID().substring(4));
            RatingtextViewProfileOther.setText(workers.getRating());
            if(workers.getImageUrl().equals("")) {
                ProfileImageViewOther.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
            }else {
                Picasso.get().load(workers.getImageUrl()).into(ProfileImageViewOther);
            }
            SetExtra();
        }
    }

    private void SetExtra() {
        if(workers.getPE().trim().isEmpty()) {
            experienceTextView3.setText("لا يوجد خبرات سابقة");
        }else {
            experienceTextView3.setText(workers.getPE().trim());
        }

        if(workers.getPP().trim().isEmpty()) {
            projectsTextView3.setText("لا يوجد مشاريع سابقة");
        }else {
            projectsTextView3.setText(workers.getPP().trim());
        }

        if(workers.getSkills().trim().isEmpty()) {
            skillsTextView3.setText("لا يوجد مهارات سابقة");
        }else {
            skillsTextView3.setText(workers.getSkills().trim());
        }

        if(workers.getAbout().trim().isEmpty()) {
            otherTextView3.setText("لا يوجد معلومات أخرى");
        }else {
            otherTextView3.setText(workers.getAbout().trim());
        }
    }

    private void SetVisibility() {
        if (Role.equals("3")) {
            AdviserLocationTextViewOther.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivityOther.setVisibility(View.GONE);
            ProfileListViewProfileActivityOther.setVisibility(View.VISIBLE);
        } else {
            AdviserLocationTextViewOther.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivityOther.setVisibility(View.VISIBLE);
            ProfileListViewProfileActivityOther.setVisibility(View.GONE);;
        }
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        AdviserLocationTextViewOther = findViewById(R.id.AdviserLocationTextViewOther);
        NameTextViewProfileOther = findViewById(R.id.NameTextViewProfileOther);
        LocationTextViewProfileOther = findViewById(R.id.LocationTextViewProfileOther);
        DateOfBirthTextViewProfileOther = findViewById(R.id.DateOfBirthTextViewProfileOther);
        PhoneNumberTextViewOther = findViewById(R.id.PhoneNumberTextViewOther);
        AboutWorkerTextViewProfileActivityOther = findViewById(R.id.AboutWorkerTextViewProfileActivityOther);
        RatingtextViewProfileOther = findViewById(R.id.RatingtextViewProfileOther);
        ProfileImageViewOther = findViewById(R.id.ProfileImageViewOther);
        ratingBar2ProfileOther = findViewById(R.id.ratingBar2ProfileOther);
        ProfileListViewProfileActivityOther = findViewById(R.id.ProfileListViewProfileActivityOther);
        progressBar = findViewById(R.id.progressBar);
        Role = getIntent().getStringExtra("Role");
        ScrollView1 = findViewById(R.id.ScrollView1);
        ScrollView2 = findViewById(R.id.ScrollView2);
        if (Role.equals("3")) {
            consultants = getIntent().getParcelableExtra("Parcelable");
        } else {
            workers = getIntent().getParcelableExtra("Parcelable");
        }
        experienceTextView3 = findViewById(R.id.experienceTextView3);
        projectsTextView3 = findViewById(R.id.projectsTextView3);
        skillsTextView3 = findViewById(R.id.skillsTextView3);
        otherTextView3 = findViewById(R.id.otherTextView3);
        ConstraintLayout = findViewById(R.id.ConstraintLayout);
    }

    public void ReviewButtonProfileActivityOtherClicked(View view) {
        String Id;
        if(Role.equals("3")) {
            Id = consultants.getID();
        }else {
            Id = workers.getID();
        }
        if (MainActivity.SaveSharedPreference.getLogIn(this).equals("true")) {
            if (ratingBar2ProfileOther.getRating() > 0) {
                if(!MainActivity.SaveSharedPreference.getPhoneNumber(this).equals(Id)) {
                    if (Connected()) {
                        UploadToDataBase();
                    } else {
                        Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "لا تستطيع تقييم نفسك", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "يرجى التقييم", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "يرجى تسجيل الدخول", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadToDataBase() {
        String Number;
        if(Role.equals("3")) {
            Number = consultants.getID();
            user = new HashMap<>();
            user.put("ID", consultants.getID());
            user.put("Rating", ratingBar2ProfileOther.getRating());
        }else {
            Number = workers.getID();
            user = new HashMap<>();
            user.put("ID", workers.getID());
            user.put("Rating", ratingBar2ProfileOther.getRating());
        }

        Clickable(false);
        progressBar.setVisibility(View.VISIBLE);
        db.collection(getString(R.string.UsersCollection)).document(Number)
                .collection(getString(R.string.RatingCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Clickable(true);
                        progressBar.setVisibility(View.GONE);
                        ratingBar2ProfileOther.setRating(0);
                        Toast.makeText(ProfileOthorUsersActivity.this, "تم إضافة التقييم بنجاح", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileOthorUsersActivity.this, "حدث خطأ أثناء التقييم", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetRecyclerView(String id) {
        posts.clear();
        if (Connected()) {
            Clickable(false);
            progressBar.setVisibility(View.VISIBLE);
            ProfileListViewProfileActivityOther.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(id)
                    .collection(getString(R.string.PostsCollection))
                    .orderBy("Date", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ProfileListViewProfileActivityOther.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProfileListViewProfileActivityOther.setVisibility(View.VISIBLE);
                                    posts.add(new Posts(document.getId(), document.getData().get("Title").toString()
                                            , document.getData().get("Text").toString(),
                                            document.getData().get("Date").toString(),
                                            Long.parseLong(document.getData().get("counter").toString())));
                                }
                                Collections.sort(posts, Collections.<Posts>reverseOrder());
                                adapterPosts = new DeletePostsProfileAdapter(posts, ProfileOthorUsersActivity.this, ProfileOthorUsersActivity.this);
                                ProfileListViewProfileActivityOther.hasFixedSize();
                                ProfileListViewProfileActivityOther.setAdapter(adapterPosts);
                                ProfileListViewProfileActivityOther.setLayoutManager(new GridLayoutManager(ProfileOthorUsersActivity.this, 1));
                                adapterPosts.notifyDataSetChanged();
                                Clickable(true);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Clickable(true);
                                progressBar.setVisibility(View.GONE);
                                ProfileListViewProfileActivityOther.setVisibility(View.GONE);
                                Toast.makeText(ProfileOthorUsersActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
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

    public void Clickable(boolean b) {
        if (b) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onPostClick(int position) {

    }

    public void ConstraintLayoutClicked(View view) {
        if(ScrollView2.getVisibility() == View.VISIBLE) {
            ScrollView2.setVisibility(View.GONE);
        }
    }

    public void AboutWorkerTextViewProfileActivityOtherClicked(View view) {
        ScrollView2.setVisibility(View.VISIBLE);
    }
}