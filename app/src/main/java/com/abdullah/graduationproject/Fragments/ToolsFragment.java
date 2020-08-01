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
import com.abdullah.graduationproject.Adapters.ToolsAdapter;
import com.abdullah.graduationproject.Adapters.WaterAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ToolsFragment extends Fragment {

    TextView NoDataTextViewToolsActivity;
    ProgressBar progressBarToolsActivity;
    RecyclerView ToolsRecyclerView;
    ToolsAdapter adapter;
    FirebaseFirestore db;
    List<Items> list;
    float TotalRating = 0;
    float count = 0;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tools, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        ReadToolsItems();
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        NoDataTextViewToolsActivity = view.findViewById(R.id.NoDataTextViewToolsActivity);
        progressBarToolsActivity = view.findViewById(R.id.progressBarToolsActivity);
        ToolsRecyclerView = view.findViewById(R.id.ToolsRecyclerView);
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
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public boolean Connected() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    private void ReadToolsItems() {
        list.clear();
        if (Connected()) {
            Clickable(false);
            progressBarToolsActivity.setVisibility(View.VISIBLE);
            ToolsRecyclerView.setVisibility(View.GONE);
            db.collection(getString(R.string.ItemsCollection))
                    .orderBy("Date", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("Category").equals("FAndV")) {
                                        ReadRating(document.getId());
                                        FavoriteState(document.getId());
                                        list.add(new Items(document.getId(), document.getData().get("Image Url").toString()
                                                , document.getData().get("Product Name").toString(),
                                                document.getData().get("Provider").toString(), document.getData().get("Price").toString(),
                                                MainActivity.SaveSharedPreference.getRating(getActivity()), document.getData().get("Phone Number").toString(),
                                                document.getData().get("Location").toString(), document.getData().get("Description").toString()));
                                    }
                                }
                                if (list.isEmpty()) {
                                    NoDataTextViewToolsActivity.setVisibility(View.VISIBLE);
                                    ToolsRecyclerView.setVisibility(View.GONE);
                                } else {
                                    NoDataTextViewToolsActivity.setVisibility(View.GONE);
                                    ToolsRecyclerView.setVisibility(View.VISIBLE);
                                }
                                adapter = new ToolsAdapter(list, getActivity());
                                ToolsRecyclerView.hasFixedSize();
                                ToolsRecyclerView.setAdapter(adapter);
                                ToolsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                                adapter.notifyDataSetChanged();
                                Clickable(true);
                                progressBarToolsActivity.setVisibility(View.GONE);
                            } else {
                                Clickable(true);
                                progressBarToolsActivity.setVisibility(View.GONE);
                                ToolsRecyclerView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void FavoriteState(String id) {
        //TODO Check favorite state
    }

    private void ReadRating(String id) {
        MainActivity.SaveSharedPreference.setRating(getActivity(), "0");
        db.collection(getString(R.string.ItemsCollection)).document(id)
                .collection(getString(R.string.ReviewCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TotalRating += Long.parseLong(document.getData().get("Review Rate").toString());
                                count++;
                            }
                            if (count == 0) {
                                MainActivity.SaveSharedPreference.setRating(getActivity(), "0");
                            } else {
                                TotalRating /= count;
                                String output = new DecimalFormat("#.0").format(TotalRating);
                                MainActivity.SaveSharedPreference.setRating(getActivity(), output);
                            }
                        } else {
                            Toast.makeText(getActivity(), "حدث خطأ أثناء قرآءة التقييم", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
