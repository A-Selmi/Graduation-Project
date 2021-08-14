package com.abdullah.graduationproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdullah.graduationproject.Classes.News;
import com.abdullah.graduationproject.R;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView newsImageViewDetails;
    TextView newsTitleTextViewDetails, newsDateTextViewDetails, DescriptionTextViewDetails;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findView();
        SetData();
    }

    private void findView() {
        newsImageViewDetails = findViewById(R.id.newsImageViewDetails);
        newsTitleTextViewDetails = findViewById(R.id.newsTitleTextViewDetails);
        newsDateTextViewDetails = findViewById(R.id.newsDateTextViewDetails);
        DescriptionTextViewDetails = findViewById(R.id.DescriptionTextViewDetails);
        news = getIntent().getParcelableExtra("Parcelable");
    }

    private void SetData() {
        Picasso.get()
                .load(news.getImage())
                .placeholder(R.mipmap.ic_icon)
                .into(newsImageViewDetails);
        newsTitleTextViewDetails.setText(news.getName());
        newsDateTextViewDetails.setText(news.getDate());
        DescriptionTextViewDetails.setText(news.getDescription());
    }
}