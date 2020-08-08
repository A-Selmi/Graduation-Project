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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.Adapters.BestConsultantsAdapter;
import com.abdullah.graduationproject.Adapters.ConsultantsAdapter;
import com.abdullah.graduationproject.Adapters.FAndVAdapter;
import com.abdullah.graduationproject.Adapters.ItemsAdapter;
import com.abdullah.graduationproject.Adapters.LastItemsAdapter;
import com.abdullah.graduationproject.Adapters.NewsAdapter;
import com.abdullah.graduationproject.Classes.Consultants;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.News;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    ProgressBar HomeProgressBar;
    RecyclerView LastNewsRecycleView, LastItemsRecycleView, BestAdviserRecycleView;
    TextView LastNewsTextView, BestItemsTextView, BestAdviserTextView;
    NewsAdapter newsAdapter;
    LastItemsAdapter itemsAdapter;
    BestConsultantsAdapter consultantsAdapter;
    FirebaseFirestore db;
    List<News> news;
    List<News> news2;
    List<Items> items;
    List<Items> items2;
    List<Consultants> consultants;
    List<Consultants> consultants2;
    View view;
    MainActivity mainActivity;
    ScrollView HomeScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findView();
        HomeScrollView.setVisibility(View.GONE);
        setAppLocale("ar");
        if (Connected()) {
            ReadNews();
        } else {
            Toast.makeText(mainActivity, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void ReadNews() {
        news.clear();
        news2.clear();
        HomeProgressBar.setVisibility(View.VISIBLE);
        LastNewsRecycleView.setItemViewCacheSize(View.GONE);
        db.collection(mainActivity.getString(R.string.NewsCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HomeProgressBar.setVisibility(View.GONE);
                            LastNewsTextView.setText("لا يوجدأخبار");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeProgressBar.setVisibility(View.VISIBLE);
                                LastNewsTextView.setText("آخر الأخبار");
                                news.add(new News(document.getId(), document.getData().get("Image Url").toString(),
                                        document.getData().get("Name").toString(), document.getData().get("Date").toString(),
                                        document.getData().get("Description").toString(), Long.parseLong(document.getData().get("counter").toString())));
                            }
                            Collections.sort(news, Collections.<News>reverseOrder());
                            if (!(news.size() == 0)) {
                                for (int i = 0; i < 6; i++) {
                                    if (news.size() > i) {
                                        news2.add(new News(news.get(i).getId(), news.get(i).getImage(), news.get(i).getName(), news.get(i).getDate(),
                                                news.get(i).getDescription(), news.get(i).getCounter()));
                                    }
                                }
                                newsAdapter = new NewsAdapter(news2, mainActivity);
                                LastNewsRecycleView.hasFixedSize();
                                LastNewsRecycleView.setAdapter(newsAdapter);
                                LastNewsRecycleView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                newsAdapter.notifyDataSetChanged();
                                HomeProgressBar.setVisibility(View.GONE);
                                LastNewsRecycleView.setItemViewCacheSize(View.VISIBLE);
                            } else {
                                HomeProgressBar.setVisibility(View.GONE);
                                LastNewsTextView.setText("لا يوجدأخبار");
                            }
                        } else {
                            Toast.makeText(mainActivity, "حدث خطأ أثناء التحميل", Toast.LENGTH_SHORT).show();
                            HomeProgressBar.setVisibility(View.GONE);
                            LastNewsRecycleView.setItemViewCacheSize(View.GONE);
                            LastNewsTextView.setText("لا يوجدأخبار");
                        }
                        ReadItems();
                    }
                });
    }

    private void ReadItems() {
        items.clear();
        items2.clear();
        HomeProgressBar.setVisibility(View.VISIBLE);
        LastItemsRecycleView.setItemViewCacheSize(View.GONE);
        db.collection(mainActivity.getString(R.string.ItemsCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HomeProgressBar.setVisibility(View.GONE);
                            BestItemsTextView.setText("لا يوجد منتجات");
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                HomeProgressBar.setVisibility(View.VISIBLE);
                                BestItemsTextView.setText("آخر المنتجات");
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
                                                    items.add(new Items(document.getId(), document.getData().get("Image Url").toString()
                                                            , document.getData().get("Product Name").toString(),
                                                            document.getData().get("Provider").toString(), document.getData().get("Price").toString(),
                                                            output[0], document.getData().get("Phone Number").toString(),
                                                            document.getData().get("Location").toString(), document.getData().get("Description").toString(),
                                                            Long.parseLong(document.getData().get("counter").toString())
                                                            , document.getData().get("Category").toString()));
                                                    Collections.sort(items, Collections.<Items>reverseOrder());
                                                    items2.clear();
                                                    if (!(items.size() == 0)) {
                                                        for (int i = 0; i < 6; i++) {
                                                            if (items.size() > i) {
                                                                items2.add(new Items(items.get(i).getId(), items.get(i).getImage(), items.get(i).getName(),
                                                                        items.get(i).getProvider(), items.get(i).getPrice(), items.get(i).getRating(),
                                                                        items.get(i).getPhoneNumber(), items.get(i).getLocation(), items.get(i).getDescription(),
                                                                        items.get(i).getCounter(), items.get(i).getCategory()));
                                                            }
                                                        }
                                                        itemsAdapter = new LastItemsAdapter(items2, mainActivity);
                                                        LastItemsRecycleView.hasFixedSize();
                                                        LastItemsRecycleView.setAdapter(itemsAdapter);
                                                        LastItemsRecycleView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                        itemsAdapter.notifyDataSetChanged();
                                                        HomeProgressBar.setVisibility(View.GONE);
                                                        LastItemsRecycleView.setItemViewCacheSize(View.VISIBLE);
                                                    } else {
                                                        HomeProgressBar.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    Toast.makeText(mainActivity, "حدث خطأ أثناء التحميل", Toast.LENGTH_SHORT).show();
                                                    HomeProgressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                            ReadConsultants();
                        } else {
                            Toast.makeText(mainActivity, "حدث خطأ أثناء التحميل", Toast.LENGTH_SHORT).show();
                            HomeProgressBar.setVisibility(View.GONE);
                            LastItemsRecycleView.setItemViewCacheSize(View.GONE);
                            BestItemsTextView.setText("لا يوجد منتجات");
                        }
                    }
                });
    }

    private void ReadConsultants() {
        consultants.clear();
        consultants2.clear();
        if (Connected()) {
            HomeProgressBar.setVisibility(View.VISIBLE);
            BestAdviserRecycleView.setVisibility(View.GONE);
            db.collection(mainActivity.getString(R.string.UsersCollection))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                HomeProgressBar.setVisibility(View.GONE);
                                BestAdviserTextView.setText("لا يوجد مستشارين");
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().get("Role").equals("3")) {
                                        HomeProgressBar.setVisibility(View.VISIBLE);
                                        BestAdviserTextView.setText("أفضل المستشارين");
                                        db.collection("Users").document(document.getId())
                                                .collection("Rating")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            final float[] TotalRating = {0};
                                                            final float[] count = {0};
                                                            final String[] output = {"0"};
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                TotalRating[0] += Float.parseFloat(document.getData().get("Rating").toString());
                                                                count[0]++;
                                                            }
                                                            if (count[0] == 0) {
                                                                TotalRating[0] = 0;
                                                                output[0] = "0";
                                                            } else {
                                                                TotalRating[0] /= count[0];
                                                                output[0] = new DecimalFormat("0.0").format(TotalRating[0]);
                                                            }
                                                            consultants.add(new Consultants(document.getId(), document.getData().get("Age").toString(), document.getData().get("First Name").toString(),
                                                                    document.getData().get("Image Url").toString(), document.getData().get("Last Name").toString(),
                                                                    document.getData().get("Location").toString(), document.getData().get("Password").toString(),
                                                                    document.getData().get("Role").toString(), output[0]));
                                                            Collections.sort(consultants, Collections.<Consultants>reverseOrder());
                                                            consultants2.clear();
                                                            if (!(consultants.size() == 0)) {
                                                                for (int i = 0; i < 5; i++) {
                                                                    if (consultants.size() > i) {
                                                                        consultants2.add(new Consultants(consultants.get(i).getID(), consultants.get(i).getAge(),
                                                                                consultants.get(i).getFirstName(), consultants.get(i).getImageUrl(),
                                                                                consultants.get(i).getLastName(), consultants.get(i).getLocation(),
                                                                                consultants.get(i).getPassword(), consultants.get(i).getRole(),
                                                                                consultants.get(i).getRating()));
                                                                    }
                                                                }
                                                                consultantsAdapter = new BestConsultantsAdapter(consultants2, mainActivity);
                                                                BestAdviserRecycleView.hasFixedSize();
                                                                BestAdviserRecycleView.setAdapter(consultantsAdapter);
                                                                BestAdviserRecycleView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                                                                consultantsAdapter.notifyDataSetChanged();
                                                                HomeProgressBar.setVisibility(View.GONE);
                                                                BestAdviserRecycleView.setVisibility(View.VISIBLE);
                                                            } else {
                                                                HomeProgressBar.setVisibility(View.GONE);
                                                            }
                                                        } else {
                                                            Toast.makeText(mainActivity, "حدث خطأ أثناء التحميل", Toast.LENGTH_SHORT).show();
                                                            HomeProgressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    }
                                }
                                HomeScrollView.setVisibility(View.VISIBLE);
                                LastNewsRecycleView.setFocusable(false);
                                LastItemsRecycleView.setFocusable(false);
                                BestAdviserRecycleView.setFocusable(false);
                            } else {
                                Toast.makeText(mainActivity, "حدث خطأ أثناء التحميل", Toast.LENGTH_SHORT).show();
                                HomeProgressBar.setVisibility(View.GONE);
                                BestAdviserRecycleView.setVisibility(View.GONE);
                                BestAdviserTextView.setText("لا يوجد مستشارين");
                            }
                        }
                    });
        } else {
            Toast.makeText(mainActivity, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        news = new ArrayList<>();
        news2 = new ArrayList<>();
        items = new ArrayList<>();
        items2 = new ArrayList<>();
        consultants = new ArrayList<>();
        consultants2 = new ArrayList<>();
        HomeProgressBar = view.findViewById(R.id.HomeProgressBar);
        LastNewsRecycleView = view.findViewById(R.id.LastNewsRecycleView);
        LastItemsRecycleView = view.findViewById(R.id.LastItemsRecycleView);
        BestAdviserRecycleView = view.findViewById(R.id.BestAdviserRecycleView);
        LastNewsTextView = view.findViewById(R.id.LastNewsTextView);
        BestItemsTextView = view.findViewById(R.id.BestItemsTextView);
        BestAdviserTextView = view.findViewById(R.id.BestAdviserTextView);
        mainActivity = (MainActivity) getActivity();
        HomeScrollView = view.findViewById(R.id.HomeScrollView);
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

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }
}
