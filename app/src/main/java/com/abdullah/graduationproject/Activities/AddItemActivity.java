package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdullah.graduationproject.LogInActivities.LoginActivity;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    EditText ItemNameEditTextAddItem, ItemPriceEditTextAddItem, ItemDiscriptionEditTextAddItem;
    ImageView ItemPictureImageViewAddItem, UploadImageViewAddItem;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    private Uri mImageUri;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    ProgressBar progressBarAddItemActivity;
    String ItemName;
    Map<String, Object> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
    }

    private void findView() {
        ItemNameEditTextAddItem = findViewById(R.id.ItemNameEditTextAddItem);
        ItemPriceEditTextAddItem = findViewById(R.id.ItemPriceEditTextAddItem);
        ItemDiscriptionEditTextAddItem = findViewById(R.id.ItemDiscriptionEditTextAddItem);
        ItemPictureImageViewAddItem = findViewById(R.id.ItemPictureImageViewAddItem);
        UploadImageViewAddItem = findViewById(R.id.UploadImageViewAddItem);
        progressBarAddItemActivity = findViewById(R.id.progressBarAddItemActivity);
        ItemName = getIntent().getStringExtra("Item");
        mStorageRef = FirebaseStorage.getInstance().getReference(getString(R.string.ItemsCollection));
        db = FirebaseFirestore.getInstance();
    }

    public void AddItemButtonAddItemClicked(View view) {
        hideKeyboard(this);
        if (Check()) {
            if (Connected()) {
                UploadToDataBase();
            } else {
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void UploadImageViewAddItemClicked(View view) {
        hideKeyboard(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            SelectPicture();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private boolean Check() {
        if (ItemNameEditTextAddItem.getText().toString().trim().isEmpty()) {
            ItemNameEditTextAddItem.setError("هذا الحقل مطلوب!");
            ItemNameEditTextAddItem.requestFocus();
            return false;
        }
        if (ItemPriceEditTextAddItem.getText().toString().trim().isEmpty()) {
            ItemPriceEditTextAddItem.setError("هذا الحقل مطلوب!");
            ItemPriceEditTextAddItem.requestFocus();
            return false;
        }
        if (ItemDiscriptionEditTextAddItem.getText().toString().trim().isEmpty()) {
            ItemDiscriptionEditTextAddItem.setError("هذا الحقل مطلوب!");
            ItemDiscriptionEditTextAddItem.requestFocus();
            return false;
        }
        if (mImageUri == null) {
            Toast.makeText(this, "يرجى وضع صورة", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void UploadToDataBase() {
        Clickable(false);
        progressBarAddItemActivity.setVisibility(View.VISIBLE);
        user = new HashMap<>();
        user.put("Product Name", ItemNameEditTextAddItem.getText().toString().trim());
        user.put("Provider", MainActivity.SaveSharedPreference.getFirstName(AddItemActivity.this)
                + " " + MainActivity.SaveSharedPreference.getLastName(AddItemActivity.this));
        user.put("Location", MainActivity.SaveSharedPreference.getLocation(AddItemActivity.this));
        user.put("Price", ItemPriceEditTextAddItem.getText().toString().trim());
        user.put("Phone Number", MainActivity.SaveSharedPreference.getPhoneNumber(AddItemActivity.this));
        user.put("Description", ItemDiscriptionEditTextAddItem.getText().toString().trim());
        final Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        user.put("Date", formattedDate);
        user.put("counter", MainActivity.SaveSharedPreference.getItemsCounter(this));

        final DocumentReference ref = db.collection(getString(R.string.UsersCollection))
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(AddItemActivity.this))
                .collection(getString(R.string.ItemsCollection)).document();
        ref.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UploadToDataBaseCollection(ref.getId());
                        UploadPicture(MainActivity.SaveSharedPreference.getPhoneNumber(AddItemActivity.this), ref.getId());
                        long oldCounter = Long.parseLong(MainActivity.SaveSharedPreference.getItemsCounter(AddItemActivity.this));
                        MainActivity.SaveSharedPreference.setItemsCounter(AddItemActivity.this, String.valueOf(oldCounter + 1));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Clickable(true);
                        progressBarAddItemActivity.setVisibility(View.GONE);
                        Toast.makeText(AddItemActivity.this, "حدث خطأ أثناء إضافة المنتج, الرجاء المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void UploadToDataBaseCollection(String id) {
        Clickable(false);
        progressBarAddItemActivity.setVisibility(View.VISIBLE);
        user = new HashMap<>();
        user.put("Product Name", ItemNameEditTextAddItem.getText().toString().trim());
        user.put("Provider", MainActivity.SaveSharedPreference.getFirstName(AddItemActivity.this)
                + " " + MainActivity.SaveSharedPreference.getLastName(AddItemActivity.this));
        user.put("Location", MainActivity.SaveSharedPreference.getLocation(AddItemActivity.this));
        user.put("Price", ItemPriceEditTextAddItem.getText().toString().trim());
        user.put("Phone Number", MainActivity.SaveSharedPreference.getPhoneNumber(AddItemActivity.this));
        user.put("Description", ItemDiscriptionEditTextAddItem.getText().toString().trim());
        user.put("Category", ItemName);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        user.put("Date", formattedDate);
        user.put("counter", MainActivity.SaveSharedPreference.getItemsCounter(this));

        final DocumentReference ref = db.collection(getString(R.string.ItemsCollection)).document(id);
        ref.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Clickable(true);
                        progressBarAddItemActivity.setVisibility(View.GONE);
                        Toast.makeText(AddItemActivity.this, "حدث خطأ أثناء إضافة المنتج, الرجاء المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                    }
                });
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
            Picasso.get().load(mImageUri.toString()).into(ItemPictureImageViewAddItem);
            ItemPictureImageViewAddItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Toast.makeText(this, "حدث خطأ خلال تحميل الصورة", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadPicture(final String phoneNumber, final String id) {
        if (mImageUri != null) {
            if (Connected()) {
                Toast.makeText(this, "تحميل...", Toast.LENGTH_SHORT).show();
                Clickable(false);
                progressBarAddItemActivity.setVisibility(View.VISIBLE);
                final StorageReference reference = mStorageRef.child(id);
                UploadTask task = reference.putFile(mImageUri);
                task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddItemActivity.this, "حدث خطا أثناء التحميل", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarAddItemActivity.setVisibility(View.GONE);
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull final Task<Uri> task) {
                        if (task.isSuccessful()) {
                            db.collection(getString(R.string.UsersCollection)).document(phoneNumber)
                                    .collection(getString(R.string.ItemsCollection)).document(id)
                                    .update("Image Url", task.getResult().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddItemActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                                    Clickable(true);
                                    progressBarAddItemActivity.setVisibility(View.GONE);
                                }
                            });
                            db.collection(getString(R.string.ItemsCollection)).document(id)
                                    .update("Image Url", task.getResult().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddItemActivity.this, "تم إضافة المنتج بنجاح", Toast.LENGTH_SHORT).show();
                                            Clickable(true);
                                            progressBarAddItemActivity.setVisibility(View.GONE);
                                            CategoryActivity.activity.finish();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddItemActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                                    Clickable(true);
                                    progressBarAddItemActivity.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Toast.makeText(AddItemActivity.this, "لم يتم تحميل الصورة", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarAddItemActivity.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                Clickable(true);
                progressBarAddItemActivity.setVisibility(View.GONE);
                Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Clickable(true);
            progressBarAddItemActivity.setVisibility(View.GONE);
            Toast.makeText(this, "لم يتم إختيار صورة", Toast.LENGTH_SHORT).show();
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}