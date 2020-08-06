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
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.Fragments.FavoriteFragment;
import com.abdullah.graduationproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterHolder> {

    Context context;
    List<Items> itemsList;
    FavoriteFragment favoriteFragment;

    public FavoriteAdapter(List<Items> itemsList, Context context, FavoriteFragment favoriteFragment) {
        this.itemsList = itemsList;
        this.context = context;
        this.favoriteFragment = favoriteFragment;
    }

    @NonNull
    @Override
    public FavoriteAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_custom, parent, false);
        return new FavoriteAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapterHolder holder, final int position) {
        final Items items = itemsList.get(position);
        Picasso.get()
                .load(items.getImage())
                .placeholder(R.mipmap.ic_icon)
                .fit()
                .centerCrop()
                .into(holder.Image);
        holder.Name.setText(items.getName());
        holder.Price.setText(items.getPrice() + " دينار");
        holder.Rating.setText(items.getRating());
        holder.NotFavoriteImageView.setVisibility(View.GONE);
        holder.FavoriteImageView.setVisibility(View.VISIBLE);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDetailsActivity = new Intent(context, DetailsActivity.class);
                toDetailsActivity.putExtra("Item", "FAndV");
                toDetailsActivity.putExtra("Id", items.getId());
                MainActivity.SaveSharedPreference.setFragment(context, "1");
                context.startActivity(toDetailsActivity);
            }
        });

        holder.FavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteFragment.DeleteFavoriteAtPosition(items.getId(), position);
                holder.NotFavoriteImageView.setVisibility(View.VISIBLE);
                holder.FavoriteImageView.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class FavoriteAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView Image;
        TextView Name, Price, Rating;
        ImageView NotFavoriteImageView, FavoriteImageView;

        public FavoriteAdapterHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.ItemImageView);
            Name = itemView.findViewById(R.id.ItemTextView);
            Price = itemView.findViewById(R.id.PriceTextView);
            Rating = itemView.findViewById(R.id.RatingTextViewStore);
            NotFavoriteImageView = itemView.findViewById(R.id.NotFavoriteImageView);
            FavoriteImageView = itemView.findViewById(R.id.FavoriteImageView);
            constraintLayout = itemView.findViewById(R.id.store_constraint_layout);
        }
    }

}
