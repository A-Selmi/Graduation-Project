package com.abdullah.graduationproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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

import com.abdullah.graduationproject.LogInActivities.SignUpActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    TextView AdviserLocationTextView, NameTextViewProfile, LocationTextViewProfile,
            PhoneNumberTextView, DateOfBirthTextViewProfile, RatingtextViewProfile, AboutWorkerTextViewProfileActivity;
    RatingBar ratingBar1Profile;
    ImageView ProfileImageView, UploadPictureProfileActivity, EditProfileProfileActivity, UploadPictureImageView;
    ListView ProfileListViewProfileActivity;
    Button AddPostButton, DeletePostButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    private Uri mImageUri;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private DocumentReference documentReference;
    ProgressBar ProgressBarHorizontal, progressBarProfileActivity;

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                ProfileImageView.setImageBitmap(bitmap);
                UploadPictureImageView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "حدث خطأ خلال تحميل الصورة", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadPicture() {
        if (mImageUri != null) {
            if (Connected()) {
                Toast.makeText(this, "تحميل...", Toast.LENGTH_SHORT).show();
                Clickable(false);
                ProgressBarHorizontal.setVisibility(View.VISIBLE);
                final StorageReference reference = mStorageRef.child(MainActivity.SaveSharedPreference.getPhoneNumber(this)
                        + "." + getFileExtension(mImageUri));
                UploadTask task = reference.putFile(mImageUri);
                task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            ProgressBarHorizontal.setVisibility(View.GONE);
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            db.collection("Users")
                                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this))
                                    .update("Image Url", task.getResult().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            MainActivity.SaveSharedPreference.setImage(ProfileActivity.this, "true");
                                            Toast.makeText(ProfileActivity.this, "تم تحميل الصورة", Toast.LENGTH_SHORT).show();
                                            Clickable(true);
                                            ProgressBarHorizontal.setVisibility(View.GONE);
                                            UploadPictureImageView.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                                    Clickable(true);
                                    ProgressBarHorizontal.setVisibility(View.GONE);
                                    UploadPictureImageView.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            ProgressBarHorizontal.setVisibility(View.GONE);
                            UploadPictureImageView.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                Clickable(true);
                ProgressBarHorizontal.setVisibility(View.GONE);
                UploadPictureImageView.setVisibility(View.GONE);
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Clickable(true);
            ProgressBarHorizontal.setVisibility(View.GONE);
            UploadPictureImageView.setVisibility(View.GONE);
            Toast.makeText(this, "لم يتم إختيار صورة", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckState() {
        CheckVisibility();
        if (!MainActivity.SaveSharedPreference.getImage(ProfileActivity.this).equals("")) {
            CheckPicture();
        }else {
            ProfileImageView.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
        }
        CheckData();
    }

    private void CheckPicture() {
        Clickable(false);
        progressBarProfileActivity.setVisibility(View.VISIBLE);
        documentReference = db.collection("Users")
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this));
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String Image = documentSnapshot.getString("Image Url");
                        Picasso.get().load(Image).into(ProfileImageView);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Clickable(true);
                                progressBarProfileActivity.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "حدث خطأ خلال تحميل الصورة", Toast.LENGTH_SHORT).show();
                ProfileImageView.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
                Clickable(true);
                progressBarProfileActivity.setVisibility(View.GONE);
            }
        });
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

    private void CheckVisibility() {
        if (MainActivity.SaveSharedPreference.getRole(this).equals("1")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileListViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منتج");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منتج");
            ProfileListViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("3")) {
            AdviserLocationTextView.setVisibility(View.VISIBLE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منشور");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منشور");
            ProfileListViewProfileActivity.setVisibility(View.VISIBLE);
        } else {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.VISIBLE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileListViewProfileActivity.setVisibility(View.GONE);
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
        ProfileListViewProfileActivity = findViewById(R.id.ProfileListViewProfileActivity);
        AddPostButton = findViewById(R.id.AddPostButton);
        DeletePostButton = findViewById(R.id.DeletePostButton);
        AboutWorkerTextViewProfileActivity = findViewById(R.id.AboutWorkerTextViewProfileActivity);
        UploadPictureProfileActivity = findViewById(R.id.UploadPictureProfileActivity);
        EditProfileProfileActivity = findViewById(R.id.EditProfileProfileActivity);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        db = FirebaseFirestore.getInstance();
        ProgressBarHorizontal = findViewById(R.id.ProgressBarHorizontal);
        UploadPictureImageView = findViewById(R.id.UploadPictureImageView);
        progressBarProfileActivity = findViewById(R.id.progressBarProfileActivity);
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
        startActivity(toDeletePostActivity);
    }

    public void AddPostButtonClicked(View view) {
        Intent toAddPostActivity = new Intent(ProfileActivity.this, AddPostActivity.class);
        startActivity(toAddPostActivity);
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
}