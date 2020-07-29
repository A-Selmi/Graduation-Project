package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Adapters.DeletePostsProfileAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Posts;
import com.abdullah.graduationproject.LogInActivities.SignUpActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements DeletePostsProfileAdapter.OnPostClickListener {

    TextView AdviserLocationTextView, NameTextViewProfile, LocationTextViewProfile,
            PhoneNumberTextView, DateOfBirthTextViewProfile, RatingtextViewProfile, AboutWorkerTextViewProfileActivity;
    RatingBar ratingBar1Profile;
    CircleImageView ProfileImageView;
    ImageView UploadPictureProfileActivity, EditProfileProfileActivity, UploadPictureImageView;
    RecyclerView ProfileRecyceViewProfileActivity;
    Button AddPostButton, DeletePostButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    private Uri mImageUri;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    ProgressBar progressBarProfileActivity, progressBarPosts;
    DeletePostsProfileAdapter adapterPosts;
    List<Posts> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findview();
        CheckState();
    }

    @Override
    public void onResume() {
        super.onResume();
        findview();
        setAppLocale("ar");
        Clickable(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectPicture();
            } else {
                Toast.makeText(this, "الرجاء إعطاء الصلاحية", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SelectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri.toString()).into(ProfileImageView);
                UploadPictureImageView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "حدث خطأ خلال تحميل الصورة", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadPicture() {
        if (mImageUri != null) {
            if (Connected()) {
                Toast.makeText(this, "تحميل...", Toast.LENGTH_SHORT).show();
                Clickable(false);
                progressBarProfileActivity.setVisibility(View.VISIBLE);
                final StorageReference reference = mStorageRef.child(MainActivity.SaveSharedPreference.getPhoneNumber(this));
                UploadTask task = reference.putFile(mImageUri);
                task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarProfileActivity.setVisibility(View.GONE);
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull final Task<Uri> task) {
                        if (task.isSuccessful()) {
                            db.collection(getString(R.string.UsersCollection))
                                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this))
                                    .update("Image Url", task.getResult().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            MainActivity.SaveSharedPreference.setImage(ProfileActivity.this, task.getResult().toString());
                                            Toast.makeText(ProfileActivity.this, "تم تحميل الصورة", Toast.LENGTH_SHORT).show();
                                            Clickable(true);
                                            progressBarProfileActivity.setVisibility(View.GONE);
                                            UploadPictureImageView.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                                    Clickable(true);
                                    progressBarProfileActivity.setVisibility(View.GONE);
                                    UploadPictureImageView.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarProfileActivity.setVisibility(View.GONE);
                            UploadPictureImageView.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                Clickable(true);
                progressBarProfileActivity.setVisibility(View.GONE);
                UploadPictureImageView.setVisibility(View.GONE);
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Clickable(true);
            progressBarProfileActivity.setVisibility(View.GONE);
            UploadPictureImageView.setVisibility(View.GONE);
            Toast.makeText(this, "لم يتم إختيار صورة", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckState() {
        CheckVisibility();
        CheckPicture();
        CheckData();
    }

    private void CheckPicture() {
        if(MainActivity.SaveSharedPreference.getImage(this).equals("")) {
            ProfileImageView.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
        }else {
            Picasso.get().load(MainActivity.SaveSharedPreference.getImage(this)).into(ProfileImageView);
        }
    }

    private void CheckData() {
        if (MainActivity.SaveSharedPreference.getRole(this).equals("1")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            AddPostButton.setText(R.string.AddItem);
            DeletePostButton.setText(R.string.DeleteItem);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("3")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            AddPostButton.setText(R.string.AddPost);
            DeletePostButton.setText(R.string.DeletePost);
            SetRecyclerView();
            //TODO Read Reviews And add them to the list and calculate the reviews and add them to the text view
            RatingtextViewProfile.setText("4.7");
        } else {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            //TODO Calculate the reviews and add them to the text view
            RatingtextViewProfile.setText("4.7");
        }
    }

    private void SetRecyclerView() {
        posts.clear();
        ProfileRecyceViewProfileActivity.setAdapter(null);
        if (Connected()) {
            Clickable(false);
            progressBarPosts.setVisibility(View.VISIBLE);
            ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this))
                    .collection(getString(R.string.PostsCollection))
                    .orderBy("counter", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProfileRecyceViewProfileActivity.setVisibility(View.VISIBLE);
                                    posts.add(new Posts(document.getId(), document.getData().get("Title").toString()
                                            , document.getData().get("Text").toString(),
                                            document.getData().get("Date").toString()));
                                }
                                adapterPosts = new DeletePostsProfileAdapter(posts, ProfileActivity.this, ProfileActivity.this);
                                ProfileRecyceViewProfileActivity.hasFixedSize();
                                ProfileRecyceViewProfileActivity.setAdapter(adapterPosts);
                                ProfileRecyceViewProfileActivity.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
                                adapterPosts.notifyDataSetChanged();
                                Clickable(true);
                                progressBarPosts.setVisibility(View.GONE);
                            } else {
                                Clickable(true);
                                progressBarPosts.setVisibility(View.GONE);
                                ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
                                Toast.makeText(ProfileActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckVisibility() {
        if (MainActivity.SaveSharedPreference.getRole(this).equals("1")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منتج");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منتج");
            ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("3")) {
            AdviserLocationTextView.setVisibility(View.VISIBLE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منشور");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منشور");
            ProfileRecyceViewProfileActivity.setVisibility(View.VISIBLE);
        } else {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.VISIBLE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileRecyceViewProfileActivity.setVisibility(View.GONE);
        }
    }

    public void findview() {
        AdviserLocationTextView = findViewById(R.id.AdviserLocationTextView);
        NameTextViewProfile = findViewById(R.id.NameTextViewProfile);
        LocationTextViewProfile = findViewById(R.id.LocationTextViewProfile);
        PhoneNumberTextView = findViewById(R.id.PhoneNumberTextView);
        DateOfBirthTextViewProfile = findViewById(R.id.DateOfBirthTextViewProfile);
        RatingtextViewProfile = findViewById(R.id.RatingtextViewProfile);
        AboutWorkerTextViewProfileActivity = findViewById(R.id.AboutWorkerTextViewProfileActivity);
        ratingBar1Profile = findViewById(R.id.ratingBar1Profile);
        ProfileImageView = findViewById(R.id.ProfileImageView);
        UploadPictureProfileActivity = findViewById(R.id.UploadPictureProfileActivity);
        EditProfileProfileActivity = findViewById(R.id.EditProfileProfileActivity);
        ProfileRecyceViewProfileActivity = findViewById(R.id.ProfileRecyceViewProfileActivity);
        AddPostButton = findViewById(R.id.AddPostButton);
        DeletePostButton = findViewById(R.id.DeletePostButton);
        AboutWorkerTextViewProfileActivity = findViewById(R.id.AboutWorkerTextViewProfileActivity);
        UploadPictureProfileActivity = findViewById(R.id.UploadPictureProfileActivity);
        EditProfileProfileActivity = findViewById(R.id.EditProfileProfileActivity);
        mStorageRef = FirebaseStorage.getInstance().getReference(getString(R.string.UploadsCollection));
        db = FirebaseFirestore.getInstance();
        UploadPictureImageView = findViewById(R.id.UploadPictureImageView);
        progressBarProfileActivity = findViewById(R.id.progressBarProfileActivity);
        progressBarPosts = findViewById(R.id.progressBarPosts);
        posts = new ArrayList<>();
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

    public void DeletePostButtonClicked(View view) {
        Intent toDeletePostActivity = new Intent(ProfileActivity.this, DeleteActivity.class);
        if(MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            toDeletePostActivity.putExtra("Role", "2");
        }else {
            toDeletePostActivity.putExtra("Role", "3");
        }
        startActivity(toDeletePostActivity);
    }

    public void AddPostButtonClicked(View view) {
        if(MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            Intent toCategoryActivity = new Intent(ProfileActivity.this, CategoryActivity.class);
            startActivity(toCategoryActivity);
        }else {
            Intent toAddPostActivity = new Intent(ProfileActivity.this, AddPostActivity.class);
            startActivity(toAddPostActivity);
        }
    }

    public void AboutWorkerTextViewProfileActivityClicked(View view) {
        Intent toWorkerDetailsActivity = new Intent(ProfileActivity.this, WorkerDetailsActivity.class);
        startActivity(toWorkerDetailsActivity);
    }

    public void UploadPictureProfileActivityClicked(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            SelectPicture();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public void EditProfileProfileActivityClicked(View view) {
        Intent toSignUpActivity = new Intent(ProfileActivity.this, SignUpActivity.class);
        toSignUpActivity.putExtra("state", "Edit");
        startActivity(toSignUpActivity);
    }

    public void UploadPictureImageViewClicked(View view) {
        UploadPicture();
    }

    @Override
    public void onPostClick(int position) {

    }
}