package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Classes.Items;
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DeleteItemsAdapter extends RecyclerView.Adapter<DeleteItemsAdapter.DeleteItemsAdapterHolder> {

    Context context;
    List<Items> itemsList;
    OnItemClickListener itemClickListener;

    public DeleteItemsAdapter(List<Items> itemsList, Context context, OnItemClickListener onItemClickListener) {
        this.itemsList = itemsList;
        this.context = context;
        itemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DeleteItemsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_delete_custom, parent, false);
        return new DeleteItemsAdapterHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteItemsAdapterHolder holder, int position) {
        Items items = itemsList.get(position);
        Picasso.get()
                .load(items.getImage())
                .placeholder(R.mipmap.ic_icon)
                .fit()
                .centerCrop()
                .into(holder.Image);
        holder.Name.setText(items.getName());
        holder.Price.setText(items.getPrice()+ " دينار");
        holder.Rating.setText(items.getRating());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class DeleteItemsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView Image;
        TextView Name, Price, Rating;
        OnItemClickListener listener;

        public DeleteItemsAdapterHolder(@NonNull View itemView, OnItemClickListener onClickListener) {
            super(itemView);
            Image = itemView.findViewById(R.id.ItemImageView);
            Name = itemView.findViewById(R.id.ItemTextView);
            Price = itemView.findViewById(R.id.PriceTextView);
            Rating = itemView.findViewById(R.id.RatingTextViewStore);
            listener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public  interface OnItemClickListener {
        void onItemClick(int position);
    }

}
