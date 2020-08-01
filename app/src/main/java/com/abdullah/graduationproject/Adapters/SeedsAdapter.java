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

import com.abdullah.graduationproject.Activities.DetailsActivity;
import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeedsAdapter extends RecyclerView.Adapter<SeedsAdapter.DeleteItemsAdapterHolder> {

    Context context;
    List<Items> itemsList;

    public SeedsAdapter(List<Items> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeleteItemsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_custom, parent, false);
        return new DeleteItemsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteItemsAdapterHolder holder, int position) {
        final Items items = itemsList.get(position);
        Picasso.get()
                .load(items.getImage())
                .placeholder(R.mipmap.ic_icon)
                .fit()
                .centerCrop()
                .into(holder.Image);
        holder.Name.setText(items.getName());
        holder.Price.setText(items.getPrice());
        holder.Rating.setText(items.getRating());
        holder.FavoriteImageView.setVisibility(View.VISIBLE);
        holder.NotFavoriteImageView.setVisibility(View.VISIBLE);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDetailsActivity = new Intent(context, DetailsActivity.class);
                toDetailsActivity.putExtra("Item", "Seeds");
                toDetailsActivity.putExtra("Id", items.getId());
                context.startActivity(toDetailsActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class DeleteItemsAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView Image;
        TextView Name, Price, Rating;
        ImageView NotFavoriteImageView, FavoriteImageView;

        public DeleteItemsAdapterHolder(@NonNull View itemView) {
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
