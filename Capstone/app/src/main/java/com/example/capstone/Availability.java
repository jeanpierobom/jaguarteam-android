package com.example.capstone;

public class Availability {

    private int id;
    private String name;

    public Availability(){
        this.id = 0;
        this.name = "John Doe";
    }

    public Availability(String name){
        this.id = 0;
        this.name = name;
    }

    public Availability(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
}
