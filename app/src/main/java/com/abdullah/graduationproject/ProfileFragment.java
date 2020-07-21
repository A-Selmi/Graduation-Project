package com.abdullah.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

            }
        });

        UploadPictureProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        EditProfileProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        findview();
        CheckState();
    }

    private void CheckState() {
        CheckVisibility();
        CheckData();
    }

    private void CheckData() {

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

}
