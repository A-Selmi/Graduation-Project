package com.abdullah.graduationproject.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.Adapters.FAndVAdapter;
import com.abdullah.graduationproject.Adapters.FavoriteAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FavoriteFragment extends Fragment {

    TextView NoDataTextViewFavoriteActivity;
    ProgressBar progressBarFavoriteActivity;
    RecyclerView FavoriteRecyclerView;
    FavoriteAdapter adapter;
    FirebaseFirestore db;
    List<Items> list;
    View view;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        ReadFavorite();
        ReadFavoriteSorted();
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        NoDataTextViewFavoriteActivity = view.findViewById(R.id.NoDataTextViewFavoriteActivity);
        progressBarFavoriteActivity = view.findViewById(R.id.progressBarFavoriteActivity);
        FavoriteRecyclerView = view.findViewById(R.id.FavoriteRecyclerView);
        mainActivity = (MainActivity) getActivity();
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
            mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            mainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    private void ReadFavorite() {
        list.clear();
        if (Connected()) {
            Clickable(false);
            progressBarFavoriteActivity.setVisibility(View.VISIBLE);
            FavoriteRecyclerView.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(mainActivity))
                    .collection(getString(R.string.FavoriteCollection))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Clickable(true);
                                progressBarFavoriteActivity.setVisibility(View.GONE);
                                FavoriteRecyclerView.setVisibility(View.GONE);
                                NoDataTextViewFavoriteActivity.setVisibility(View.VISIBLE);
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    Clickable(false);
                                    progressBarFavoriteActivity.setVisibility(View.VISIBLE);
                                    FavoriteRecyclerView.setVisibility(View.GONE);
                                    NoDataTextViewFavoriteActivity.setVisibility(View.GONE);
                                    db.collection("Items")
                                            .document(document.getId())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (!documentSnapshot.exists()) {
                                                        DeleteFavorite(document.getId());
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Clickable(true);
                                progressBarFavoriteActivity.setVisibility(View.GONE);
                                FavoriteRecyclerView.setVisibility(View.GONE);
                                Toast.makeText(mainActivity, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mainActivity, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void ReadFavoriteSorted() {
        list.clear();
        if (Connected()) {
            Clickable(false);
            progressBarFavoriteActivity.setVisibility(View.VISIBLE);
            FavoriteRecyclerView.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .document(MainActivity.SaveSharedPreference.getPhoneNumber(mainActivity))
                    .collection(getString(R.string.FavoriteCollection))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Clickable(true);
                                progressBarFavoriteActivity.setVisibility(View.GONE);
                                FavoriteRecyclerView.setVisibility(View.GONE);
                                NoDataTextViewFavoriteActivity.setVisibility(View.VISIBLE);
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    Clickable(false);
                                    progressBarFavoriteActivity.setVisibility(View.VISIBLE);
                                    FavoriteRecyclerView.setVisibility(View.GONE);
                                    NoDataTextViewFavoriteActivity.setVisibility(View.GONE);
                                    db.collection("Items").document(document.getId())
                                            .collection("Review")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        final float[] TotalRating = {0};
                                                        final float[] count = {0};
                                                        final String[] output = {"0"};
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            TotalRating[0] += Float.parseFloat(document.getData().get("Review Rate").toString());
                                                            count[0]++;
                                                        }
                                                        if (count[0] == 0) {
                                                            TotalRating[0] = 0;
                                                            output[0] = "0";
                                                        } else {
                                                            TotalRating[0] /= count[0];
                                                            output[0] = new DecimalFormat("0.0").format(TotalRating[0]);
                                                        }
                                                        list.add(new Items(document.getId(), document.getData().get("Image Url").toString()
                                                                , document.getData().get("Product Name").toString(),
                                                                document.getData().get("Provider").toString(), document.getData().get("Price").toString(),
                                                                output[0], document.getData().get("Phone Number").toString(),
                                                                document.getData().get("Location").toString(), document.getData().get("Description").toString(),
                                                                Long.parseLong(document.getData().get("counter").toString()),
                                                                document.getData().get("Category").toString()));
                                                        Collections.sort(list, Collections.<Items>reverseOrder());
                                                        if (list.isEmpty()) {
                                                            NoDataTextViewFavoriteActivity.setVisibility(View.VISIBLE);
                                                            FavoriteRecyclerView.setVisibility(View.GONE);
                                                        } else {
                                                            NoDataTextViewFavoriteActivity.setVisibility(View.GONE);
                                                            FavoriteRecyclerView.setVisibility(View.VISIBLE);
                                                        }
                                                        adapter = new FavoriteAdapter(list, mainActivity, FavoriteFragment.this);
                                                        FavoriteRecyclerView.hasFixedSize();
                                                        FavoriteRecyclerView.setAdapter(adapter);
                                                        FavoriteRecyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 3));
                                                        adapter.notifyDataSetChanged();
                                                        Clickable(true);
                                                        progressBarFavoriteActivity.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(mainActivity, "حدث خطأ أثناء قرآءة التقييم", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Clickable(true);
                                progressBarFavoriteActivity.setVisibility(View.GONE);
                                FavoriteRecyclerView.setVisibility(View.GONE);
                                Toast.makeText(mainActivity, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mainActivity, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void DeleteFavorite(String id) {
        db.collection(getString(R.string.UsersCollection))
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(mainActivity))
                .collection(getString(R.string.FavoriteCollection)).document(id).delete();
    }

    public void DeleteFavoriteAtPosition(String id, int position) {
        db.collection(getString(R.string.UsersCollection))
                .document(MainActivity.SaveSharedPreference.getPhoneNumber(mainActivity))
                .collection(getString(R.string.FavoriteCollection)).document(id).delete();
        Toast.makeText(mainActivity, "تمت إزالة المنتج من المفضلة", Toast.LENGTH_SHORT).show();
        list.remove(position);
        FavoriteRecyclerView.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }
}
