package com.abdullah.graduationproject.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Consultants implements Parcelable, Comparable<Consultants> {

    String ID, Age, FirstName, ImageUrl, LastName, Location, Password, Role, Rating;

    public Consultants(String ID, String age, String firstName, String imageUrl, String lastName, String location, String password, String role, String rating) {
        this.ID = ID;
        Age = age;
        FirstName = firstName;
        ImageUrl = imageUrl;
        LastName = lastName;
        Location = location;
        Password = password;
        Role = role;
        Rating = rating;
    }

    protected Consultants(Parcel in) {
        ID = in.readString();
        Age = in.readString();
        FirstName = in.readString();
        ImageUrl = in.readString();
        LastName = in.readString();
        Location = in.readString();
        Password = in.readString();
        Role = in.readString();
        Rating = in.readString();
    }

    public static final Creator<Consultants> CREATOR = new Creator<Consultants>() {
        @Override
        public Consultants createFromParcel(Parcel in) {
            return new Consultants(in);
        }

        @Override
        public Consultants[] newArray(int size) {
            return new Consultants[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(Age);
        parcel.writeString(FirstName);
        parcel.writeString(ImageUrl);
        parcel.writeString(LastName);
        parcel.writeString(Location);
        parcel.writeString(Password);
        parcel.writeString(Role);
        parcel.writeString(Rating);
    }

    @Override
    public int compareTo(Consultants consultants) {
        int compare = Rating.compareTo(consultants.Rating);
        return compare;
    }
}
