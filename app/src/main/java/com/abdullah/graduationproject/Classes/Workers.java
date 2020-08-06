package com.abdullah.graduationproject.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Workers implements Parcelable, Comparable<Workers> {

    String ID, Age, FirstName, ImageUrl, LastName, Location, Password, Role, Rating,
            About, PE, PP, Skills;


    public Workers(String ID, String age, String firstName, String imageUrl, String lastName, String location, String password, String role, String rating, String about, String PE, String PP, String skills) {
        this.ID = ID;
        Age = age;
        FirstName = firstName;
        ImageUrl = imageUrl;
        LastName = lastName;
        Location = location;
        Password = password;
        Role = role;
        Rating = rating;
        About = about;
        this.PE = PE;
        this.PP = PP;
        Skills = skills;
    }

    protected Workers(Parcel in) {
        ID = in.readString();
        Age = in.readString();
        FirstName = in.readString();
        ImageUrl = in.readString();
        LastName = in.readString();
        Location = in.readString();
        Password = in.readString();
        Role = in.readString();
        Rating = in.readString();
        About = in.readString();
        PE = in.readString();
        PP = in.readString();
        Skills = in.readString();
    }

    public static final Creator<Workers> CREATOR = new Creator<Workers>() {
        @Override
        public Workers createFromParcel(Parcel in) {
            return new Workers(in);
        }

        @Override
        public Workers[] newArray(int size) {
            return new Workers[size];
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

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getPP() {
        return PP;
    }

    public void setPP(String PP) {
        this.PP = PP;
    }

    public String getSkills() {
        return Skills;
    }

    public void setSkills(String skills) {
        Skills = skills;
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
        parcel.writeString(About);
        parcel.writeString(PE);
        parcel.writeString(PP);
        parcel.writeString(Skills);
    }

    @Override
    public int compareTo(Workers workers) {
        int compare = Rating.compareTo(workers.Rating);
        return compare;
    }
}
