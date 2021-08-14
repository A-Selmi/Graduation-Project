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
import com.abdullah.graduationproject.Adapters.WorkersAdapter;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Workers;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WorkerFragment extends Fragment {

    TextView NoDataTextViewWorkerActivity;
    ProgressBar progressBarWorkerActivity;
    RecyclerView WorkerRecyclerView;
    WorkersAdapter adapter;
    FirebaseFirestore db;
    List<Workers> list;
    View view;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.worker, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findView();
        setAppLocale("ar");
        ReadWorkers();
    }

    private void findView() {
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        NoDataTextViewWorkerActivity = view.findViewById(R.id.NoDataTextViewWorkerActivity);
        progressBarWorkerActivity = view.findViewById(R.id.progressBarWorkerActivity);
        WorkerRecyclerView = view.findViewById(R.id.WorkerRecyclerView);
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

    private void ReadWorkers() {
        list.clear();
        if (Connected()) {
            Clickable(false);
            progressBarWorkerActivity.setVisibility(View.VISIBLE);
            WorkerRecyclerView.setVisibility(View.GONE);
            db.collection(getString(R.string.UsersCollection))
                    .orderBy("First Name", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Clickable(true);
                                progressBarWorkerActivity.setVisibility(View.GONE);
                                WorkerRecyclerView.setVisibility(View.GONE);
                                NoDataTextViewWorkerActivity.setVisibility(View.VISIBLE);
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getData().get("Role").equals("4")) {
                                        Clickable(false);
                                        progressBarWorkerActivity.setVisibility(View.VISIBLE);
                                        WorkerRecyclerView.setVisibility(View.GONE);
                                        NoDataTextViewWorkerActivity.setVisibility(View.GONE);
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
                                                                list.add(new Workers(document.getId(), document.getData().get("Age").toString(), document.getData().get("First Name").toString(),
                                                                        document.getData().get("Image Url").toString(), document.getData().get("Last Name").toString(),
                                                                        document.getData().get("Location").toString(), document.getData().get("Password").toString(),
                                                                        document.getData().get("Role").toString(), "0", document.getData().get("About").toString(),
                                                                        document.getData().get("PE").toString(), document.getData().get("PP").toString(), document.getData().get("PP").toString()));
                                                            } else {
                                                                TotalRating[0] /= count[0];
                                                                output[0] = new DecimalFormat("#.0").format(TotalRating[0]);
                                                                list.add(new Workers(document.getId(), document.getData().get("Age").toString(), document.getData().get("First Name").toString(),
                                                                        document.getData().get("Image Url").toString(), document.getData().get("Last Name").toString(),
                                                                        document.getData().get("Location").toString(), document.getData().get("Password").toString(),
                                                                        document.getData().get("Role").toString(), output[0], document.getData().get("About").toString(),
                                                                        document.getData().get("PE").toString(), document.getData().get("PP").toString(), document.getData().get("PP").toString()));
                                                            }
                                                            Collections.sort(list, Collections.<Workers>reverseOrder());
                                                            if (list.isEmpty()) {
                                                                NoDataTextViewWorkerActivity.setVisibility(View.VISIBLE);
                                                                WorkerRecyclerView.setVisibility(View.GONE);
                                                            } else {
                                                                NoDataTextViewWorkerActivity.setVisibility(View.GONE);
                                                                WorkerRecyclerView.setVisibility(View.VISIBLE);
                                                            }
                                                            adapter = new WorkersAdapter(list, mainActivity);
                                                            WorkerRecyclerView.hasFixedSize();
                                                            WorkerRecyclerView.setAdapter(adapter);
                                                            WorkerRecyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 1));
                                                            adapter.notifyDataSetChanged();
                                                            Clickable(true);
                                                            progressBarWorkerActivity.setVisibility(View.GONE);
                                                        } else {
                                                            Clickable(true);
                                                            progressBarWorkerActivity.setVisibility(View.GONE);
                                                            WorkerRecyclerView.setVisibility(View.VISIBLE);
                                                            NoDataTextViewWorkerActivity.setVisibility(View.GONE);
                                                            Toast.makeText(mainActivity, "حدث خطأ أثناء قرآءة التقييم", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            } else {
                                Clickable(true);
                                progressBarWorkerActivity.setVisibility(View.GONE);
                                WorkerRecyclerView.setVisibility(View.GONE);
                                Toast.makeText(mainActivity, "حدث خطأ, يرجى المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mainActivity, R.string.InternetConnectionMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
