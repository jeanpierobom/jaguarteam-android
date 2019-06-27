package com.example.capstone;

public class Teacher {

    private int id;
    private String name;
    private String email;
    private String city;
    private String bio;
    private String[] classTypes;
    private int country;
    private String[] languages;
    private float hourlyRate;
    private float distance;
    private float rating;
    private Rating[] ratinsList;

    public Teacher(){
        String[] defaultLanguage = {"English"};
        String[] defaultType = {"Groceries"};
        Rating[] defaultRating = {new Rating(0,2,"","","")};
        this.id = 0;
        this.name = "John Doe";
        this.email = "john.doe@email.com";
        this.city = "Townsvile";
        this.bio = "nothing much is known about this guy";
        this.classTypes = defaultType;
        this.country = 0;
        this.languages = defaultLanguage;
        this.hourlyRate = 15.5f;
        this.distance = 1;
        this.rating = 3;
        this.ratinsList = defaultRating;
    }

    public Teacher(String name){
        String[] defaultLanguage = {"English"};
        String[] defaultType = {"Groceries"};
        Rating[] defaultRating = {new Rating(0,2,"","","")};
        this.id = 0;
        this.name = name;
        this.email = "john.doe@email.com";
        this.city = "Townsvile";
        this.bio = "nothing much is known about this guy";
        this.classTypes = defaultType;
        this.country = 0;
        this.languages = defaultLanguage;
        this.hourlyRate = 15.5f;
        this.distance = 1;
        this.rating = 3;
        this.ratinsList = defaultRating;
    }

    public Teacher(int id, String name, String email, String bio, String city, String[] classTypes, int country, String[] languages, float hourlyRate, float distance, float rating, Rating[] ratings){
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.bio = bio;
        this.classTypes = classTypes;
        this.country = country;
        this.languages = languages;
        this.hourlyRate = hourlyRate;
        this.distance = distance;
        this.rating = rating;
        this.ratinsList = ratings;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getEmail(){return this.email;}
    public String getCity(){return this.city;}
    public int getCountry(){return this.country;}
    public String[] getLanguages(){return this.languages;}
    public String getLanguage(int i){
        if(languages.length<1){
            return "No Language";
        }
        return this.languages[i];
    }
    public float getHourlyRate(){return this.hourlyRate;}
    public float getDistance(){return this.distance;}
    public float getRating(){return this.rating;}
}
