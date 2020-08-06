package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Activities.DetailsActivity;
import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.Activities.ProfileOthorUsersActivity;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Classes.Workers;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkersAdapterHolder> {

    Context context;
    List<Workers> WorkersList;
    View view;

    public WorkersAdapter(List<Workers> workersList, Context context) {
        this.WorkersList = workersList;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkersAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.worker_and_adviser_custom, parent, false);
        return new WorkersAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkersAdapterHolder holder, final int position) {
        final Workers workers = WorkersList.get(position);
        if (!workers.getImageUrl().equals("")) {
            Picasso.get()
                    .load(workers.getImageUrl())
                    .placeholder(R.mipmap.ic_icon)
                    .fit()
                    .centerCrop()
                    .into(holder.ImageUrl);
        } else {
            holder.ImageUrl.setImageDrawable(context.getResources().getDrawable(R.drawable.profiledefault));
        }
        holder.Name.setText(workers.getFirstName() + " " + workers.getLastName());
        holder.Location.setText(workers.getLocation());
        holder.Rating.setText(workers.getRating());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toProfileOthorUsersActivity = new Intent(context, ProfileOthorUsersActivity.class);
                toProfileOthorUsersActivity.putExtra("Role", "4");
                toProfileOthorUsersActivity.putExtra("Parcelable", workers);
                MainActivity.SaveSharedPreference.setFragment(context, "6");
                context.startActivity(toProfileOthorUsersActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return WorkersList.size();
    }

    public class WorkersAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView ImageUrl;
        TextView Name, Location, Rating;

        public WorkersAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ImageUrl = itemView.findViewById(R.id.WorkerAndAdviserImageView);
            Name = itemView.findViewById(R.id.WorkerAndAdviserNameTextView);
            Location = itemView.findViewById(R.id.LocationTextView);
            Rating = itemView.findViewById(R.id.RatingTextViewWorker);
            constraintLayout = itemView.findViewById(R.id.worker_and_adviser_constraint_layout);
        }
    }

}
