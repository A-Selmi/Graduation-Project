package com.abdullah.graduationproject.Classes;

public class Reviews implements Comparable<Reviews> {

    String Id, Name, Rate,  Date, Text;
    Long Counter;

    public Reviews(String id, String name, String rate, String date, String text, Long counter) {
        Id = id;
        Name = name;
        Rate = rate;
        Date = date;
        Text = text;
        Counter = counter;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Long getCounter() {
        return Counter;
    }

    public void setCounter(Long counter) {
        Counter = counter;
    }

    @Override
    public int compareTo(Reviews reviews) {
        int compare = Counter.compareTo(reviews.Counter);
        return compare;
    }
}
