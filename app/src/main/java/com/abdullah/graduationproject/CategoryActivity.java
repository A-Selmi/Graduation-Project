package com.abdullah.graduationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    public void CategoryNameButtonClicked(View view) {
        // get item by id and send the name
        Intent toAddItemActivity = new Intent(this, AddItemActivity.class);
        toAddItemActivity.putExtra("item", "ButtonName");
        startActivity(toAddItemActivity);
    }

}