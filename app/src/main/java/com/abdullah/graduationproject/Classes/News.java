package com.abdullah.graduationproject.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Comparable<News>, Parcelable {

    String Id, Image, Name, Date, Description;
    Long Counter;

    public News(String id, String image, String name, String date, String description, Long counter) {
        Id = id;
        Image = image;
        Name = name;
        Date = date;
        Description = description;
        Counter = counter;
    }

    protected News(Parcel in) {
        Id = in.readString();
        Image = in.readString();
        Name = in.readString();
        Date = in.readString();
        Description = in.readString();
        if (in.readByte() == 0) {
            Counter = null;
        } else {
            Counter = in.readLong();
        }
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getCounter() {
        return Counter;
    }

    public void setCounter(Long counter) {
        Counter = counter;
    }

    @Override
    public int compareTo(News items) {
        int compare = Counter.compareTo(items.Counter);
        return compare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Image);
        parcel.writeString(Name);
        parcel.writeString(Date);
        parcel.writeString(Description);
        if (Counter == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(Counter);
        }
    }
}
