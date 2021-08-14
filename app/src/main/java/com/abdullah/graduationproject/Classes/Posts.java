package com.abdullah.graduationproject.Classes;

public class Posts implements Comparable<Posts> {

    String ID, Title, Text, Date;
    Long Counter;

    public Posts(String id, String title, String text, String date, Long counter) {
        ID = id;
        Title = title;
        Text = text;
        Date = date;
        Counter = counter;
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

    public Long getCounter() {
        return Counter;
    }

    public void setCounter(Long counter) {
        Counter = counter;
    }

    @Override
    public int compareTo(Posts posts) {
        int compare = Counter.compareTo(posts.Counter);
        return compare;
    }
}
