package com.example.capstone;

public class Teacher {
    private String name;
    private int country;
    private String[] languages;
    private float hourlyRate;
    private float distance;
    private float rating;

    public Teacher(){
        String[] defaultLanguage = {"English"};
        this.name = "John Doe";
        this.country = 0;
        this.languages = defaultLanguage;
        this.hourlyRate = 15.5f;
        this.distance = 1;
        this.rating = 3;
    }

    public Teacher(String name){
        String[] defaultLanguage = {"English"};
        this.name = name;
        this.country = 0;
        this.languages = defaultLanguage;
        this.hourlyRate = 15.5f;
        this.distance = 1;
        this.rating = 3;
    }

    public Teacher(String name, int country, String[] languages, float hourlyRate, float distance, float rating){
        this.name = name;
        this.country = country;
        this.languages = languages;
        this.hourlyRate = hourlyRate;
        this.distance = distance;
        this.rating = rating;
    }

    public String getName(){return this.name;}
    public int getCountry(){return this.country;}
    public String[] getLanguages(){return this.languages;}
    public String getLanguage(int i){return this.languages[i];}
    public float getHourlyRate(){return this.hourlyRate;}
    public float getDistance(){return this.distance;}
    public float getRating(){return this.rating;}
}
