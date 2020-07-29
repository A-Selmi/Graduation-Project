package com.abdullah.graduationproject.Classes;

public class Items {

    String Id, Image, Name, Provider, Price,
            Rating, PhoneNumber, Location, Description;

    public Items() {
    }

    public Items(String id, String image, String name, String provider, String price, String rating, String phoneNumber, String location, String description) {
        Id = id;
        Image = image;
        Name = name;
        Provider = provider;
        Price = price;
        Rating = rating;
        PhoneNumber = phoneNumber;
        Location = location;
        Description = description;
    }

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

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}