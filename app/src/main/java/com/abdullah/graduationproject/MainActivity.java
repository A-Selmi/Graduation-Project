package com.abdullah.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    View HomeView;
    TextView loginNavHeaderTextView;
    Context context;
    ImageView UserProfilePictureImageView;
    TextView UserNameTextView;
    Intent toLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findHomeViews();
        ReadyForDrawer(savedInstanceState);
        if (SaveSharedPreference.getLogIn(context).equals("true")) {
            Toast.makeText(context, "مرحبًا " + SaveSharedPreference.getFirstName(context), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
        setAppLocale("ar");
        findHomeViews();
        if (SaveSharedPreference.getLogIn(context).equals("true")) {
            loginNavHeaderTextView.setText("تسجيل الخروج");
            loginNavHeaderTextView.setTextColor(getResources().getColor(R.color.Red));
            UserNameTextView.setText(SaveSharedPreference.getFirstName(context) + " " + SaveSharedPreference.getLastName(context));
            UserNameTextView.setVisibility(View.VISIBLE);
            //TODO Check The Photo
            UserProfilePictureImageView.setImageResource(R.drawable.image);
            Toast.makeText(context, "مرحبًا " + SaveSharedPreference.getFirstName(context), Toast.LENGTH_SHORT).show();
        } else {
            loginNavHeaderTextView.setText("تسجيل الدخول");
            loginNavHeaderTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            UserProfilePictureImageView.setImageResource(R.drawable.profiledefault);
            UserNameTextView.setText("");
            UserNameTextView.setVisibility(View.GONE);
        }
    }

    public void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, metrics);
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
        if (SaveSharedPreference.getLogIn(context).equals("true")) {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                    break;
                case R.id.nav_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FavoriteFragment()).commit();
                    break;
            }
        } else {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    startActivity(toLoginActivity);
                    break;
                case R.id.nav_favorite:
                    startActivity(toLoginActivity);
                    break;
            }
        }
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
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
        context = this;
        HomeView = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawerLayout);
        loginNavHeaderTextView = HomeView.findViewById(R.id.loginNavHeaderTextView);
        UserProfilePictureImageView = HomeView.findViewById(R.id.UserProfilePictureImageView);
        UserNameTextView = HomeView.findViewById(R.id.UserNameTextView);
        toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
    }

    public void loginNavHeaderTextViewClicked(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (SaveSharedPreference.getLogIn(context).equals("true")) {
            AlertDialog dialog = LogoutDialog();
            dialog.show();
        } else {
            Intent toLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(toLoginActivity);
        }
    }

    public void UserProfilePictureImageViewClicked(View view) {
        if (SaveSharedPreference.getLogIn(context).equals("true")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        } else {
            startActivity(toLoginActivity);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public static class SaveSharedPreference {
        static final String PREF_LOGIN = "LogIn";
        static final String PREF_FIRSTNAME = "FirstName";
        static final String PREF_LASTTNAME = "LastName";
        static final String PREF_LOCATION = "Location";
        static final String PREF_Age = "Age";
        static final String PREF_PHONE_NUMBER = "PhoneNumber";
        static final String PREF_ROLE = "Role";
        static final String PREF_PASSWORD = "Password";
        static final String PREF_PE = "PE";
        static final String PREF_PP = "PP";
        static final String PREF_SKILLS = "Skills";
        static final String PREF_ABOUT = "About";

        static SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static void setLogIn(Context ctx, String login) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_LOGIN, login);
            editor.apply();
        }

        public static String getLogIn(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_LOGIN, "");
        }

        public static void setFirstName(Context ctx, String firstname) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_FIRSTNAME, firstname);
            editor.apply();
        }

        public static String getFirstName(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_FIRSTNAME, "");
        }

        public static void setLastName(Context ctx, String lastname) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_LASTTNAME, lastname);
            editor.apply();
        }

        public static String getLastName(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_LASTTNAME, "");
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

        public static void setPassword(Context ctx, String password) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_PASSWORD, password);
            editor.apply();
        }

        public static String getPassword(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_PASSWORD, "");
        }

        public static void setPE(Context ctx, String PE) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_PE, PE);
            editor.apply();
        }

        public static String getPE(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_PE, "");
        }

        public static void setPP(Context ctx, String PP) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_PP, PP);
            editor.apply();
        }

        public static String getPP(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_PP, "");
        }

        public static void setSkills(Context ctx, String skills) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_SKILLS, skills);
            editor.apply();
        }

        public static String getSkills(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_SKILLS, "");
        }

        public static void setAbout(Context ctx, String about) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_ABOUT, about);
            editor.apply();
        }

        public static String getAbout(Context ctx) {
            return getSharedPreferences(ctx).getString(PREF_ABOUT, "");
        }
    }

    private AlertDialog LogoutDialog() {
        final AlertDialog LogoutDialog = new AlertDialog.Builder(this)
                .setTitle("تسجيل الخروج")
                .setMessage("هل تريد إكمال تسجيل الخروج ؟")
                .setIcon(R.drawable.ic_log_out)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        SaveSharedPreference.setLogIn(context, "false");
                        SaveSharedPreference.setFirstName(context, "");
                        SaveSharedPreference.setLastName(context, "");
                        SaveSharedPreference.setLocation(context, "");
                        SaveSharedPreference.setAge(context, "");
                        SaveSharedPreference.setRole(context, "");
                        SaveSharedPreference.setPassword(context, "");
                        loginNavHeaderTextView.setText("تسجيل الدخول");
                        loginNavHeaderTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        UserProfilePictureImageView.setImageResource(R.drawable.profiledefault);
                        UserNameTextView.setText("");
                        UserNameTextView.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_home);
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        LogoutDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                LogoutDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Red));
            }
        });

        return LogoutDialog;
    }
}