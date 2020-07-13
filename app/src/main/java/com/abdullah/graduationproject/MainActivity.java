package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    View HomeView;
    TextView loginNavHeaderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findHomeViews();
        ReadyForDrawer(savedInstanceState);
    }

    private void ReadyForDrawer(Bundle savedInstanceState) {
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (SaveSharedPreference.getUserName(MainActivity.this).equals("")) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                    break;
                case R.id.nav_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FavoriteFragment()).commit();
                    break;
                case R.id.nav_water:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new WaterFragment()).commit();
                    break;
                case R.id.nav_fruits_and_vegetables:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FruitsAndVegetablesFragment()).commit();
                    break;
                case R.id.nav_seeds:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SeedsFragment()).commit();
                    break;
                case R.id.nav_tools:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ToolsFragment()).commit();
                    break;
                case R.id.nav_worker:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new WorkerFragment()).commit();
                    break;
                case R.id.nav_Adviser:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AdviserFragment()).commit();
                    break;
                case R.id.nav_contact_us:
                    Toast.makeText(this, "Go to Contact Us web page", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_about_us:
                    Intent toAboutUsActivity = new Intent(this, AboutUsActivity.class);
                    startActivity(toAboutUsActivity);
                    break;
            }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void findHomeViews() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        HomeView = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawerLayout);
        loginNavHeaderTextView = HomeView.findViewById(R.id.loginNavHeaderTextView);
    }

    public void loginNavHeaderTextViewClicked(View view) {
        // check if it is تسجيل الدخول or تسجيل الخروج
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
    }

    public void AddPostButtonClicked(View view) {
        Intent toAddPostActivity = new Intent(this, AddPostActivity.class);
        startActivity(toAddPostActivity);
    }

    public void DeletePostButtonClicked(View view) {
        Intent toDeletePostActivity = new Intent(this, DeleteActivity.class);
        startActivity(toDeletePostActivity);
    }

    public void UserProfilePictureImageViewClicked(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProfileFragment()).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void WorkerButtonClicked(View view) {
        Toast.makeText(this, "the worker has been requested successfully", Toast.LENGTH_SHORT).show();
    }
}