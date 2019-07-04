package com.example.capstone;

import java.util.ArrayList;

public class Availability {

    private int id;
    private String name;
    private String date;
    private ArrayList<String> timeSlots;

    public Availability(){
        this.id = 0;
        this.name = "John Doe";
    }

    public Availability(String name, String date, ArrayList<String> timeSlots){
        this.id = 0;
        this.name = name;
        this.date = date;
        this.timeSlots = timeSlots;
    }

    public Availability(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}

    public String getDate() {
        return date;
    }

    public ArrayList<String> getTimeSlots() {
        return timeSlots;
    }
}
