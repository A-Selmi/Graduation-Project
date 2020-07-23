package com.abdullah.graduationproject.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abdullah.graduationproject.Activity.AddPostActivity;
import com.abdullah.graduationproject.Activity.DeleteActivity;
import com.abdullah.graduationproject.Activity.MainActivity;
import com.abdullah.graduationproject.R;
import com.abdullah.graduationproject.LogInActivities.SignUpActivity;
import com.abdullah.graduationproject.Activity.WorkerDetailsActivity;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    View view;
    TextView AdviserLocationTextView, NameTextViewProfile, LocationTextViewProfile,
            PhoneNumberTextView, DateOfBirthTextViewProfile, RatingtextViewProfile, AboutWorkerTextViewProfileActivity;
    RatingBar ratingBar1Profile;
    ImageView ProfileImageView, UploadPictureProfileActivity, EditProfileProfileActivity;
    ListView ProfileListViewProfileActivity;
    Button AddPostButton, DeletePostButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        findview();
        AddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddPostActivity = new Intent(getActivity(), AddPostActivity.class);
                startActivity(toAddPostActivity);
            }
        });

        DeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDeletePostActivity = new Intent(getActivity(), DeleteActivity.class);
                startActivity(toDeletePostActivity);
            }
        });

        AboutWorkerTextViewProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toWorkerDetailsActivity = new Intent(getActivity(), WorkerDetailsActivity.class);
                startActivity(toWorkerDetailsActivity);
            }
        });

        UploadPictureProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Upload picture
            }
        });

        EditProfileProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUpActivity = new Intent(getActivity(), SignUpActivity.class);
                toSignUpActivity.putExtra("state", "Edit");
                startActivity(toSignUpActivity);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findview();
        CheckState();
        setAppLocale("ar");
    }

    private void CheckState() {
        //TODO the photo
        CheckVisibility();
        CheckData();
    }

    private void CheckData() {
        if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("1")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(getActivity()) + " " +
                    MainActivity.SaveSharedPreference.getLastName(getActivity()));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(getActivity()));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(getActivity()).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(getActivity()));
        } else if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("2")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(getActivity()) + " " +
                    MainActivity.SaveSharedPreference.getLastName(getActivity()));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(getActivity()));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(getActivity()).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(getActivity()));
            AddPostButton.setText(R.string.AddItem);
            DeletePostButton.setText(R.string.DeleteItem);
        } else if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("3")) {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(getActivity()) + " " +
                    MainActivity.SaveSharedPreference.getLastName(getActivity()));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(getActivity()));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(getActivity()).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(getActivity()));
            AddPostButton.setText(R.string.AddPost);
            DeletePostButton.setText(R.string.DeletePost);
            //TODO Read Reviews And add them to the list and calculate the reviews and add them to the text view
            RatingtextViewProfile.setText("4.7");
        } else {
            NameTextViewProfile.setText("الإسم :" + MainActivity.SaveSharedPreference.getFirstName(getActivity()) + " " +
                    MainActivity.SaveSharedPreference.getLastName(getActivity()));
            LocationTextViewProfile.setText("الموقع :" + MainActivity.SaveSharedPreference.getLocation(getActivity()));
            PhoneNumberTextView.setText("الهاتف :0" + MainActivity.SaveSharedPreference.getPhoneNumber(getActivity()).substring(4));
            DateOfBirthTextViewProfile.setText("العمر :" + MainActivity.SaveSharedPreference.getAge(getActivity()));
            //TODO Calculate the reviews and add them to the text view
            RatingtextViewProfile.setText("4.7");
        }
    }

    private void CheckVisibility() {
        if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("1")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileListViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("2")) {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.GONE);
            ratingBar1Profile.setVisibility(View.GONE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منتج");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منتج");
            ProfileListViewProfileActivity.setVisibility(View.GONE);
        } else if (MainActivity.SaveSharedPreference.getRole(getActivity()).equals("3")) {
            AdviserLocationTextView.setVisibility(View.VISIBLE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.GONE);
            DeletePostButton.setVisibility(View.VISIBLE);
            DeletePostButton.setText("حذف منشور");
            AddPostButton.setVisibility(View.VISIBLE);
            AddPostButton.setText("إضافة منشور");
            ProfileListViewProfileActivity.setVisibility(View.VISIBLE);
        } else {
            AdviserLocationTextView.setVisibility(View.GONE);
            RatingtextViewProfile.setVisibility(View.VISIBLE);
            ratingBar1Profile.setVisibility(View.VISIBLE);
            AboutWorkerTextViewProfileActivity.setVisibility(View.VISIBLE);
            DeletePostButton.setVisibility(View.GONE);
            AddPostButton.setVisibility(View.GONE);
            ProfileListViewProfileActivity.setVisibility(View.GONE);
        }
    }

    public void findview() {
        AdviserLocationTextView = view.findViewById(R.id.AdviserLocationTextView);
        NameTextViewProfile = view.findViewById(R.id.NameTextViewProfile);
        LocationTextViewProfile = view.findViewById(R.id.LocationTextViewProfile);
        PhoneNumberTextView = view.findViewById(R.id.PhoneNumberTextView);
        DateOfBirthTextViewProfile = view.findViewById(R.id.DateOfBirthTextViewProfile);
        RatingtextViewProfile = view.findViewById(R.id.RatingtextViewProfile);
        AboutWorkerTextViewProfileActivity = view.findViewById(R.id.AboutWorkerTextViewProfileActivity);
        ratingBar1Profile = view.findViewById(R.id.ratingBar1Profile);
        ProfileImageView = view.findViewById(R.id.ProfileImageView);
        UploadPictureProfileActivity = view.findViewById(R.id.UploadPictureProfileActivity);
        EditProfileProfileActivity = view.findViewById(R.id.EditProfileProfileActivity);
        ProfileListViewProfileActivity = view.findViewById(R.id.ProfileListViewProfileActivity);
        AddPostButton = view.findViewById(R.id.AddPostButton);
        DeletePostButton = view.findViewById(R.id.DeletePostButton);
        AboutWorkerTextViewProfileActivity = view.findViewById(R.id.AboutWorkerTextViewProfileActivity);
        UploadPictureProfileActivity = view.findViewById(R.id.UploadPictureProfileActivity);
        EditProfileProfileActivity = view.findViewById(R.id.EditProfileProfileActivity);
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
