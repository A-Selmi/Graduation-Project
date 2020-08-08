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
import com.abdullah.graduationproject.Activities.NewsDetailsActivity;
import com.abdullah.graduationproject.Activities.ProfileOthorUsersActivity;
import com.abdullah.graduationproject.Classes.Consultants;
import com.abdullah.graduationproject.Classes.News;
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder> {

    Context context;
    List<News> newsList;
    View view;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.news_custom, parent, false);
        return new NewsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapterHolder holder, final int position) {
        final News news = newsList.get(position);
        if (!news.getImage().equals("")) {
            Picasso.get()
                    .load(news.getImage())
                    .placeholder(R.mipmap.ic_icon)
                    .fit()
                    .centerCrop()
                    .into(holder.ImageUrl);
        } else {
            holder.ImageUrl.setImageDrawable(context.getResources().getDrawable(R.drawable.profiledefault));
        }
        holder.Name.setText(news.getName());
        holder.Date.setText(news.getDate());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toNewsDetailsActivity = new Intent(context, NewsDetailsActivity.class);
                toNewsDetailsActivity.putExtra("Parcelable", news);
                MainActivity.SaveSharedPreference.setFragment(context, "");
                context.startActivity(toNewsDetailsActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsAdapterHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView ImageUrl;
        TextView Name, Date;

        public NewsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ImageUrl = itemView.findViewById(R.id.NewsImageView);
            Name = itemView.findViewById(R.id.NewsTitleTextView);
            Date = itemView.findViewById(R.id.NewsDateTextView);
            constraintLayout = itemView.findViewById(R.id.news_constraint_layout);
        }
    }

}
