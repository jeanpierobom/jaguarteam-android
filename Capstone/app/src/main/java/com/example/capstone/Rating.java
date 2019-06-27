package com.example.capstone;

import java.util.Date;

public class Rating {

    private int id;
    private float value;
    private String comment;
    private String author;
    private String date;

    Rating(){
        this.id = 0;
        this.value = 0;
        this.comment = "";
        this.author = "";
        this.date = "";
    }

    Rating(int id, float value, String comment, String author, String date){
        this.id = id;
        this.value = value;
        this.comment = comment;
        this.author = author;
        this.date = date;
    }

    public int getId(){return this.id;}
    public float getValue(){return this.value;}
    public String getComment(){return this.comment;}
    public String getAuthor(){return this.author;}
    public String getDate(){return this.date;}

}
