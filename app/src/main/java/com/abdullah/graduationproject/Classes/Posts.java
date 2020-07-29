package com.abdullah.graduationproject.Classes;

public class Posts {

    String ID, Title, Text, Date;

    public Posts(String id, String title, String text, String date) {
        ID = id;
        Title = title;
        Text = text;
        Date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
