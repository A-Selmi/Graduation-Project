package com.abdullah.graduationproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.abdullah.graduationproject.R;

import java.util.Locale;

public class CategoryActivity extends AppCompatActivity {

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAppLocale("ar");
    }

    public void WaterNameButtonClicked(View view) {
        Intent toAddItemActivity = new Intent(this, AddItemActivity.class);
        toAddItemActivity.putExtra("Item", "Water");
        startActivity(toAddItemActivity);
    }

    public void SeedsNameButtonClicked(View view) {
        Intent toAddItemActivity = new Intent(this, AddItemActivity.class);
        toAddItemActivity.putExtra("Item", "Seeds");
        startActivity(toAddItemActivity);
    }

    public void FAndVNameButtonClicked(View view) {
        Intent toAddItemActivity = new Intent(this, AddItemActivity.class);
        toAddItemActivity.putExtra("Item", "FAndV");
        startActivity(toAddItemActivity);
    }

    public void ToolsNameButtonClicked(View view) {
        Intent toAddItemActivity = new Intent(this, AddItemActivity.class);
        toAddItemActivity.putExtra("Item", "Tools");
        startActivity(toAddItemActivity);
    }

    public void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, metrics);
    }
}