package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Adapters.DeletePostsProfileAdapter;
import com.abdullah.graduationproject.Classes.Posts;
import com.abdullah.graduationproject.LogInActivities.PhoneNumberActivity;
import com.abdullah.graduationproject.LogInActivities.SignUpActivity;
import com.abdullah.graduationproject.LogInActivities.VerificationActivity;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    float TotalRating = 0;
    float count = 0;
    ImageView CancelPictureProfileActivity, DeletePictureProfileActivityClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void onResume() {
        super.onResume();
        findview();
        Clickable(false);
        setAppLocale("ar");
        CheckState();
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri.toString()).into(ProfileImageView);
            CancelPictureProfileActivity.setVisibility(View.VISIBLE);
            UploadPictureImageView.setVisibility(View.VISIBLE);
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
                                            CancelPictureProfileActivity.setVisibility(View.GONE);
                                            UploadPictureImageView.setVisibility(View.GONE);
                                            CheckPicture();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                                    Clickable(true);
                                    progressBarProfileActivity.setVisibility(View.GONE);
                                    CancelPictureProfileActivity.setVisibility(View.GONE);
                                    UploadPictureImageView.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarProfileActivity.setVisibility(View.GONE);
                            CancelPictureProfileActivity.setVisibility(View.GONE);
                            UploadPictureImageView.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                Clickable(true);
                progressBarProfileActivity.setVisibility(View.GONE);
                CancelPictureProfileActivity.setVisibility(View.GONE);
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
        if (MainActivity.SaveSharedPreference.getImage(this).equals("")) {
            ProfileImageView.setImageDrawable(getResources().getDrawable(R.drawable.profiledefault));
        } else {
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
            Clickable(true);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            AddPostButton.setText(R.string.AddItem);
            DeletePostButton.setText(R.string.DeleteItem);
            Clickable(true);
        } else if (MainActivity.SaveSharedPreference.getRole(this).equals("3")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            AddPostButton.setText(R.string.AddPost);
            DeletePostButton.setText(R.string.DeletePost);
            SetRecyclerView();
            ReadRating();
        } else {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(this) + " " +
                    MainActivity.SaveSharedPreference.getLastName(this));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(this));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(this).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(this));
            ReadRating();
        }
    }

    private void ReadRating() {
        Clickable(false);
        progressBarProfileActivity.setVisibility(View.VISIBLE);
        db.collection("Users").document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .collection("Rating")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RatingtextViewProfile.setText("0");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TotalRating += Float.parseFloat(document.getData().get("Rating").toString());
                                count++;
                            }
                            if (count == 0) {
                                RatingtextViewProfile.setText("0");
                            } else {
                                TotalRating /= count;
                                String output = new DecimalFormat("#.0").format(TotalRating);
                                RatingtextViewProfile.setText(output);
                            }
                            Clickable(true);
                            progressBarProfileActivity.setVisibility(View.GONE);
                        } else {
                            Clickable(true);
                            progressBarProfileActivity.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, "حدث خطأ أثناء قرآءة التقييم", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                    .orderBy("Date", Query.Direction.ASCENDING)
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
                                            document.getData().get("Date").toString(),
                                            Long.parseLong(document.getData().get("counter").toString())));
                                }
                                Collections.sort(posts, Collections.<Posts>reverseOrder());
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
        CancelPictureProfileActivity = findViewById(R.id.CancelPictureProfileActivity);
        DeletePictureProfileActivityClicked = findViewById(R.id.DeletePictureProfileActivity);
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
        if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            toDeletePostActivity.putExtra("Role", "2");
        } else {
            toDeletePostActivity.putExtra("Role", "3");
        }
        startActivity(toDeletePostActivity);
    }

    public void AddPostButtonClicked(View view) {
        if (MainActivity.SaveSharedPreference.getRole(this).equals("2")) {
            Intent toCategoryActivity = new Intent(ProfileActivity.this, CategoryActivity.class);
            startActivity(toCategoryActivity);
        } else {
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

    public void DeletePictureProfileActivityClicked(View view) {
        AlertDialog dialog = DeletePictureDialog();
        dialog.show();
    }

    public void CancelPictureProfileActivityClicked(View view) {
        CheckPicture();
        CancelPictureProfileActivity.setVisibility(View.GONE);
        UploadPictureImageView.setVisibility(View.GONE);
    }

    private AlertDialog DeletePictureDialog() {
        final AlertDialog DeletePictureDialog = new AlertDialog.Builder(this)
                .setTitle("حذف الصورة")
                .setMessage("هل تريد حذف الصورة ؟")
                .setIcon(R.drawable.ic_delete_account)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        DeleteProfilePicture();
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        DeletePictureDialog.setCanceledOnTouchOutside(false);
        DeletePictureDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                DeletePictureDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Red));
                DeletePictureDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
            }
        });
        return DeletePictureDialog;
    }

    private void DeleteProfilePicture() {
        progressBarProfileActivity.setVisibility(View.VISIBLE);
        db.collection(getString(R.string.UsersCollection))
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this))
                .update("Image Url", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!MainActivity.SaveSharedPreference.getImage(ProfileActivity.this).equals("")) {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference photoRef = storageRef.child("uploads").child(MainActivity.SaveSharedPreference.getPhoneNumber(ProfileActivity.this));
                            photoRef.delete();
                            MainActivity.SaveSharedPreference.setImage(ProfileActivity.this, "");
                        }
                        Picasso.get().load(R.drawable.profiledefault).into(ProfileImageView);
                        progressBarProfileActivity.setVisibility(View.GONE);
                        CancelPictureProfileActivity.setVisibility(View.GONE);
                        UploadPictureImageView.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "تم حذف الصورة", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                Clickable(true);
                progressBarProfileActivity.setVisibility(View.GONE);
                CancelPictureProfileActivity.setVisibility(View.GONE);
                UploadPictureImageView.setVisibility(View.GONE);
            }
        });
    }
}