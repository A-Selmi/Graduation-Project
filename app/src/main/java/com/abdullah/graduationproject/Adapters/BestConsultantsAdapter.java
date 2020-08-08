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
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestConsultantsAdapter extends RecyclerView.Adapter<BestConsultantsAdapter.BestconsultantsAdapterHolder> {

    Context context;
    List<Consultants> consultantsList;
    View view;

    public BestConsultantsAdapter(List<Consultants> consultantsList, Context context) {
        this.consultantsList = consultantsList;
        this.context = context;
    }

    @NonNull
    @Override
    public BestconsultantsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.adviser_custom, parent, false);
        return new BestconsultantsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BestconsultantsAdapterHolder holder, final int position) {
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
        holder.Rating.setText(consultants.getRating());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toProfileOthorUsersActivity = new Intent(context, ProfileOthorUsersActivity.class);
                toProfileOthorUsersActivity.putExtra("Role", "3");
                toProfileOthorUsersActivity.putExtra("Parcelable", consultants);
                MainActivity.SaveSharedPreference.setFragment(context, "");
                context.startActivity(toProfileOthorUsersActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultantsList.size();
    }

    public class BestconsultantsAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView ImageUrl;
        TextView Name, Rating;

        public BestconsultantsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ImageUrl = itemView.findViewById(R.id.AdviserImageViewAdviser);
            Name = itemView.findViewById(R.id.AdviserNameTextViewAdviser);
            Rating = itemView.findViewById(R.id.RatingTextViewStoreAdviser);
            constraintLayout = itemView.findViewById(R.id.best_adviser_constraint_layout);
        }
    }

}
