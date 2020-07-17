package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    public void AboutWorkerTextViewProfileActivity(View view) {
        Intent toWorkerDetailsActivity = new Intent(this, WorkerDetailsActivity.class);
        startActivity(toWorkerDetailsActivity);
    }

    public void ReviewButtonProfileActivityClicked(View view) {
        Toast.makeText(this, "Review Added!", Toast.LENGTH_SHORT).show();
    }

    public void EditProfileProfileActivityClicked(View view) {
        Intent toSignUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(toSignUpActivity);
    }

    public void UploadPictureProfileActivityClicked(View view) {
        Toast.makeText(this, "Upload A Picture", Toast.LENGTH_SHORT).show();
    }

    public static class SaveSharedPreference {
        static final String PREF_USERNAME = "UserName";
        static final String PREF_LOCATION = "Location";
        static final String PREF_Age = "Age";
        static final String PREF_PHONE_NUMBER = "PhoneNumber";
        static final String PREF_ROLE = "Role";

        static SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static void setUserName(Context ctx, String username) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USERNAME, username);
            editor.apply();
        }

        public static String getUserNmae(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
        }

        public static void setLocation(Context ctx, String location) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_LOCATION, location);
            editor.apply();
        }

        public static String getLocation(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_LOCATION, "");
        }

        public static void setAge(Context ctx, String age) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_Age, age);
            editor.apply();
        }

        public static String getAge(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_Age, "");
        }

        public static void setPhoneNumber(Context ctx, String phonenumber) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_PHONE_NUMBER, phonenumber);
            editor.apply();
        }

        public static String getPhoneNumber(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_PHONE_NUMBER, "");
        }

        public static void setRole(Context ctx, String role) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_ROLE, role);
            editor.apply();
        }

        public static String getRole(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_ROLE, "");
        }
    }
}