package com.abdullah.graduationproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullah.graduationproject.Adapters.DeleteItemsAdapter;
import com.abdullah.graduationproject.Adapters.DeletePostsAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Posts;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeleteActivity extends AppCompatActivity implements DeleteItemsAdapter.OnItemClickListener, DeletePostsAdapter.OnPostClickListener {
    TextView DeleteTextView, NoDataTextViewDeleteActivity;
    RecyclerView DeleteRecycleView;
    DeleteItemsAdapter adapter;
    DeletePostsAdapter adapterPosts;
    FirebaseFirestore db;
    List<Items> list;
    List<Posts> posts;
    ProgressBar progressBarDeleteActivity;
    float TotalRating = 0;
    float count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        CheckState();
        DeleteRecycleView.setHasFixedSize(true);
        DeleteRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        if (getIntent().getStringExtra("Role").equals("2")) {
            GetItems();
        } else {
            GetPosts();
        }
    }

    private void GetPosts() {
        posts.clear();
        if (Connected()) {
            Clickable(false);
            progressBarDeleteActivity.setVisibility(View.VISIBLE);
            DeleteRecycleView.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(DeleteActivity.this))
                    .collection(getString(R.string.PostsCollection))
                    .orderBy("Date", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                NoDataTextViewDeleteActivity.setVisibility(View.VISIBLE);
                                DeleteRecycleView.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    NoDataTextViewDeleteActivity.setVisibility(View.GONE);
                                    DeleteRecycleView.setVisibility(View.VISIBLE);
                                    posts.add(new Posts(document.getId(), document.getData().get("Title").toString()
                                            , document.getData().get("Text").toString(),
                                            document.getData().get("Date").toString()));
                                }
                                adapterPosts = new DeletePostsAdapter(posts, DeleteActivity.this, DeleteActivity.this);
                                DeleteRecycleView.setAdapter(null);
                                DeleteRecycleView.setAdapter(adapterPosts);
                                DeleteRecycleView.setLayoutManager(new GridLayoutManager(DeleteActivity.this, 1));
                                Clickable(true);
                                progressBarDeleteActivity.setVisibility(View.GONE);
                            } else {
                                Clickable(true);
                                progressBarDeleteActivity.setVisibility(View.GONE);
                                DeleteRecycleView.setVisibility(View.GONE);
                                Toast.makeText(DeleteActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void GetItems() {
        list.clear();
        if (Connected()) {
            Clickable(false);
            progressBarDeleteActivity.setVisibility(View.VISIBLE);
            DeleteRecycleView.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(DeleteActivity.this))
                    .collection(getString(R.string.ItemsCollection))
                    .orderBy("Date", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String rating = ReadRating(document.getId());
                                    list.add(new Items(document.getId(), document.getData().get("Image Url").toString()
                                            , document.getData().get("Product Name").toString(),
                                            document.getData().get("Provider").toString(), document.getData().get("Price").toString(),
                                            rating, document.getData().get("Phone Number").toString(),
                                            document.getData().get("Location").toString(), document.getData().get("Description").toString()));
                                }
                                if (list.isEmpty()) {
                                    NoDataTextViewDeleteActivity.setVisibility(View.VISIBLE);
                                    DeleteRecycleView.setVisibility(View.GONE);
                                } else {
                                    NoDataTextViewDeleteActivity.setVisibility(View.GONE);
                                    DeleteRecycleView.setVisibility(View.VISIBLE);
                                }
                                adapter = new DeleteItemsAdapter(list, DeleteActivity.this, DeleteActivity.this);
                                DeleteRecycleView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Clickable(true);
                                progressBarDeleteActivity.setVisibility(View.GONE);
                            } else {
                                Clickable(true);
                                progressBarDeleteActivity.setVisibility(View.GONE);
                                DeleteRecycleView.setVisibility(View.GONE);
                                Toast.makeText(DeleteActivity.this, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private String ReadRating(String id) {
        db.collection(getString(R.string.UsersCollection))
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(DeleteActivity.this))
                .collection(getString(R.string.ItemsCollection)).document(id)
                .collection(getString(R.string.ReviewCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TotalRating += Float.parseFloat(document.getData().get("Review Rate").toString());
                                count++;
                            }
                        } else {
                            Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء قرآءة التقييم", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        if (count == 0) {
            return "0";
        } else {
            TotalRating /= count;
            String output = new DecimalFormat("#.0").format(TotalRating);
            return output;
        }
    }

    private AlertDialog DeleteDialog(final int position) {
        final AlertDialog DeleteDialog = new AlertDialog.Builder(this)
                .setTitle("حذف منتج")
                .setMessage("هل تريد حذف هذا المنتج ؟")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (Connected()) {
                            DeleteItemPicture(position);
                            DeleteItem(position);
                            DeleteItemsCollection(position);
                        } else {
                            Toast.makeText(DeleteActivity.this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        DeleteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                DeleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Red));
            }
        });

        return DeleteDialog;
    }

    private void DeleteItemsCollection(int position) {
        Clickable(false);
        progressBarDeleteActivity.setVisibility(View.VISIBLE);
        DeleteItemsReviewCollection(list.get(position).getId());
        db.collection(getString(R.string.ItemsCollection)).document(list.get(position).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Clickable(true);
                        progressBarDeleteActivity.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء الحذف", Toast.LENGTH_SHORT).show();
                Clickable(true);
                progressBarDeleteActivity.setVisibility(View.GONE);
            }
        });
    }

    private void DeleteItemsReviewCollection(final String id) {
        db.collection(getString(R.string.ItemsCollection)).document(id)
                .collection(getString(R.string.ReviewCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection(getString(R.string.ItemsCollection))
                                        .document(id)
                                        .collection(getString(R.string.ReviewCollection))
                                        .document(document.getId())
                                        .delete();
                            }
                            Clickable(true);
                            progressBarDeleteActivity.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء الحذف", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarDeleteActivity.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void DeleteItemPicture(int position) {
        Clickable(false);
        progressBarDeleteActivity.setVisibility(View.VISIBLE);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = storageRef.child("Items").child(list.get(position).getId());
        photoRef.delete();
        Clickable(true);
        progressBarDeleteActivity.setVisibility(View.GONE);
    }

    private void DeleteItem(final int position) {
        Clickable(false);
        progressBarDeleteActivity.setVisibility(View.VISIBLE);
        DeleteItemsReview(list.get(position).getId());
        db.collection(getString(R.string.UsersCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .collection(getString(R.string.ItemsCollection)).document(list.get(position).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DeleteActivity.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        DeleteRecycleView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                        Clickable(true);
                        progressBarDeleteActivity.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء الحذف", Toast.LENGTH_SHORT).show();
                Clickable(true);
                progressBarDeleteActivity.setVisibility(View.GONE);
            }
        });
    }

    private void DeleteItemsReview(final String id) {
        db.collection(getString(R.string.UsersCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .collection(getString(R.string.ItemsCollection)).document(id)
                .collection(getString(R.string.ReviewCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection(getString(R.string.UsersCollection))
                                        .document(MainActivity.SaveSharedPreference.getPhoneNumber(DeleteActivity.this))
                                        .collection(getString(R.string.ItemsCollection))
                                        .document(id)
                                        .collection(getString(R.string.ReviewCollection))
                                        .document(document.getId())
                                        .delete();
                            }
                            Clickable(true);
                            progressBarDeleteActivity.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء الحذف", Toast.LENGTH_SHORT).show();
                            Clickable(true);
                            progressBarDeleteActivity.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void CheckState() {
        if (getIntent().getStringExtra("Role").equals("2")) {
            DeleteTextView.setText("حذف منتج");
        } else {
            DeleteTextView.setText("حذف منشور");
        }
    }

    private void findView() {
        DeleteTextView = findViewById(R.id.DeleteTextView);
        DeleteRecycleView = findViewById(R.id.DeleteRecycleView);
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        posts = new ArrayList<>();
        progressBarDeleteActivity = findViewById(R.id.progressBarDeleteActivity);
        NoDataTextViewDeleteActivity = findViewById(R.id.NoDataTextViewDeleteActivity);
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
    public void onItemClick(int position) {
        AlertDialog dialog = DeleteDialog(position);
        dialog.show();
    }

    @Override
    public void onPostClick(int position) {
        AlertDialog dialog = DeletePostDialog(position);
        dialog.show();
    }

    private AlertDialog DeletePostDialog(final int position) {
        final AlertDialog DeleteDialog = new AlertDialog.Builder(this)
                .setTitle("حذف منشور")
                .setMessage("هل تريد حذف هذا المنشور ؟")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (Connected()) {
                            DeletePost(position);
                        } else {
                            Toast.makeText(DeleteActivity.this, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        DeleteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                DeleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Red));
            }
        });

        return DeleteDialog;
    }

    private void DeletePost(final int position) {
        Clickable(false);
        progressBarDeleteActivity.setVisibility(View.VISIBLE);
        db.collection(getString(R.string.UsersCollection)).document(MainActivity.SaveSharedPreference.getPhoneNumber(this))
                .collection(getString(R.string.PostsCollection)).document(posts.get(position).getID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DeleteActivity.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                        posts.remove(position);
                        DeleteRecycleView.removeViewAt(position);
                        adapterPosts.notifyItemRemoved(position);
                        adapterPosts.notifyDataSetChanged();
                        Clickable(true);
                        progressBarDeleteActivity.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeleteActivity.this, "حدث خطأ أثناء الحذف", Toast.LENGTH_SHORT).show();
                Clickable(true);
                progressBarDeleteActivity.setVisibility(View.GONE);
            }
        });
    }
}