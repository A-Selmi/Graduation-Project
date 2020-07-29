package com.abdullah.graduationproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullah.graduationproject.Classes.Posts;
import com.abdullah.graduationproject.R;

import java.util.List;

public class DeletePostsAdapter extends RecyclerView.Adapter<DeletePostsAdapter.DeletePostsAdapterHolder> {

    Context context;
    List<Posts> PostsList;
    OnPostClickListener postClickListener;

    public DeletePostsAdapter(List<Posts> PostsList, Context context, OnPostClickListener onPostClickListener) {
        this.PostsList = PostsList;
        this.context = context;
        postClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public DeletePostsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_custom, parent, false);
        return new DeletePostsAdapterHolder(view, postClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletePostsAdapterHolder holder, int position) {
        Posts Posts = PostsList.get(position);
        holder.Title.setText(Posts.getTitle());
        holder.Text.setText(Posts.getText());
        holder.Date.setText(Posts.getDate());
    }

    @Override
    public int getItemCount() {
        return PostsList.size();
    }

    public class DeletePostsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Title, Text, Date;
        OnPostClickListener listener;

        public DeletePostsAdapterHolder(@NonNull View postView, OnPostClickListener onClickListener) {
            super(postView);
            Title = postView.findViewById(R.id.TitleTextViewPost);
            Text = postView.findViewById(R.id.DescriptionTextViewPost);
            Date = postView.findViewById(R.id.DateTextView);
            listener = onClickListener;

            postView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onPostClick(getAdapterPosition());
        }
    }

    public  interface OnPostClickListener {
        void onPostClick(int position);
    }

}
