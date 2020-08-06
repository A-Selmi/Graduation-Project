package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Activities.MainActivity;
import com.abdullah.graduationproject.Activities.ProfileOthorUsersActivity;
import com.abdullah.graduationproject.Classes.Consultants;
import com.abdullah.graduationproject.Classes.Workers;
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConsultantsAdapter extends RecyclerView.Adapter<ConsultantsAdapter.consultantsAdapterHolder> {

    Context context;
    List<Consultants> consultantsList;
    View view;

    public ConsultantsAdapter(List<Consultants> consultantsList, Context context) {
        this.consultantsList = consultantsList;
        this.context = context;
    }

    @NonNull
    @Override
    public consultantsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.worker_and_adviser_custom, parent, false);
        return new consultantsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final consultantsAdapterHolder holder, final int position) {
        final Consultants consultants = consultantsList.get(position);
        if (!consultants.getImageUrl().equals("")) {
            Picasso.get()
                    .load(consultants.getImageUrl())
                    .placeholder(R.mipmap.ic_icon)
                    .fit()
                    .centerCrop()
                    .into(holder.ImageUrl);
        } else {
            holder.ImageUrl.setImageDrawable(context.getResources().getDrawable(R.drawable.profiledefault));
        }
        holder.Name.setText(consultants.getFirstName() + " " + consultants.getLastName());
        holder.Location.setText(consultants.getLocation());
        holder.Rating.setText(consultants.getRating());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toProfileOthorUsersActivity = new Intent(context, ProfileOthorUsersActivity.class);
                toProfileOthorUsersActivity.putExtra("Role", "3");
                toProfileOthorUsersActivity.putExtra("Parcelable", consultants);
                MainActivity.SaveSharedPreference.setFragment(context, "7");
                context.startActivity(toProfileOthorUsersActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultantsList.size();
    }

    public class consultantsAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView ImageUrl;
        TextView Name, Location, Rating;

        public consultantsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ImageUrl = itemView.findViewById(R.id.WorkerAndAdviserImageView);
            Name = itemView.findViewById(R.id.WorkerAndAdviserNameTextView);
            Location = itemView.findViewById(R.id.LocationTextView);
            Rating = itemView.findViewById(R.id.RatingTextViewWorker);
            constraintLayout = itemView.findViewById(R.id.worker_and_adviser_constraint_layout);
        }
    }

}
