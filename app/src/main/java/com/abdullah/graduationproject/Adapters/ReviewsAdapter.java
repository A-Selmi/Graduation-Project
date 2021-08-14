package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Classes.Reviews;
import com.abdullah.graduationproject.R;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.DetailsAdapterHolder> {

    Context context;
    List<Reviews> ReviewList;

    public ReviewsAdapter(List<Reviews> reviewList, Context context) {
        this.ReviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_custom, parent, false);
        return new DetailsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapterHolder holder, int position) {
        Reviews reviews = ReviewList.get(position);
        holder.Name.setText(reviews.getName());
        holder.Rate.setRating((int)(Float.parseFloat(reviews.getRate())));
        holder.Date.setText(reviews.getDate());
        if(reviews.getText().isEmpty()) {
            holder.Text.setText("لا يوجد مراجعة");
        }else {
            holder.Text.setText(reviews.getText());
        }
    }

    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    public class DetailsAdapterHolder extends RecyclerView.ViewHolder {

        TextView Name, Date, Text;
        RatingBar Rate;

        public DetailsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.NameTextViewReview);
            Rate = itemView.findViewById(R.id.ratingBarReview);
            Date = itemView.findViewById(R.id.DateTextViewReview);
            Text = itemView.findViewById(R.id.ReviewDetailsTextViewReview);
        }
    }

}
