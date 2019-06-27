package com.example.capstone;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class JsonAPIParser {

    public static ArrayList<String> ParseLanguageRequest(JsonReader reader){
        ArrayList<String> LanguageList = new ArrayList<String>();

        try{
            reader.beginArray();
            while(reader.hasNext()){
                LanguageList.add(readLanguageObject(reader));
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return LanguageList;
    }

    public static ArrayList<String> ParseCityRequest(JsonReader reader){
        ArrayList<String> cities = new ArrayList<>();

        try {
            reader.beginArray();
            while(reader.hasNext()){

            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public static ArrayList<Teacher> ParseTeacherRequest(JsonReader reader){
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        try {
            reader.beginArray();
            while(reader.hasNext()){
                teachers.add(readTeacherObject(reader));
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    private static String readCityObject(JsonReader reader){
        String city = "";
        String name;
        try{
            reader.beginObject();
            while(reader.hasNext()){
                name  = reader.nextName();
                if(name.equals("name")){
                    city = reader.nextString();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    private static String readLanguageObject(JsonReader reader){
        String language = "";
        String name;
        try {
            reader.beginObject();
            while(reader.hasNext()){
                name = reader.nextName();
                if(name.equals("name")) {
                    language = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return language;
    }

    private static String readClassTypeObject(JsonReader reader){
        String classType = "";
        String name;
        try {
            reader.beginObject();
            while(reader.hasNext()){
                name = reader.nextName();
                if(name.equals("name")) {
                    classType = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classType;
    }

    private static Teacher readTeacherObject(JsonReader reader){
        int id = 0;
        String name = "";
        String email = "";
        String city = "";
        String bio = "";
        ArrayList<String> classes = new ArrayList<String>();
        int countryId = -1;
        ArrayList<String> languages = new ArrayList<String>();
        float hourRate = -1;
        float distance = 0;
        float rating = 0;
        ArrayList<Rating> ratings = new ArrayList<Rating>();

        String key;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                key = reader.nextName();
                Log.e("TEACHER PARSER", "key content: " + key);
                if(key.equals("id")){
                    id = reader.nextInt();
                }else if(key.equals("email")){
                    email = reader.nextString();
                }else if(key.equals("name")){
                    name = reader.nextString();
                }else if(key.equals("cityAsString")){
                    city = reader.nextString();
                }else if(key.equals("bio")){
                    bio = reader.nextString();
                }else if(key.equals("motherCountryId")){
                    if(reader.peek()==JsonToken.NULL){
                        countryId = -1;
                        reader.skipValue();
                    }
                    else {
                        countryId = reader.nextInt();
                    }
                }else if(key.equals("teacherPrice")){
                    if(reader.peek()== JsonToken.NULL){
                        hourRate = 0;
                        reader.skipValue();
                    }
                    else {
                        hourRate = (float) reader.nextDouble();
                    }
                }else if(key.equals("averageRatingToTeacher")){
                    if(reader.peek()==JsonToken.NULL){
                        rating = 0;
                        reader.skipValue();
                    }
                    else {
                        rating = (float) reader.nextDouble();
                    }
                }else if(key.equals("language")){
                    Log.e("TEACHER PARSER", "string content: " + reader.peek());
                    if(reader.peek() == JsonToken.BEGIN_ARRAY){
                        reader.beginArray();
                        while(reader.hasNext()){
                            languages.add(readLanguageObject(reader));
                        }
                        reader.endArray();
                    }
                    else if(reader.peek() == JsonToken.STRING) {
                        languages.add(reader.nextString());
                        Log.e("TEACHER PARSER", "string content: " + languages.get(0));
                    }else if(reader.peek()==JsonToken.NULL){
                        reader.skipValue();
                    }
                }else if(key.equals("classTypes")){
                    reader.beginArray();
                    while(reader.hasNext()){
                        classes.add(readClassTypeObject(reader));
                    }
                    reader.endArray();
                }else if(key.equals("ratings")){
                    reader.beginArray();
                    while(reader.hasNext()){
                        ratings.add(readRatingObject(reader));
                    }
                    reader.endArray();
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Teacher(id,name,email,bio,city,classes.toArray(new String[0]),countryId,languages.toArray(new String[0]),hourRate,distance,rating,ratings.toArray(new Rating[0]));
    }

    private static Rating readRatingObject(JsonReader reader){
        int id = -1;
        float value = -1;
        String comment = "";
        String author = "";
        String date = "";

        String name = "";

        try {
            reader.beginObject();
            while(reader.hasNext()){
                name = reader.nextName();
                if(name.equals("score")){
                    value = (float)reader.nextDouble();
                }else if(name.equals("comment")){
                    comment = reader.nextString();
                }else if(name.equals("author")){
                    author = reader.nextString();
                }else if(name.equals("date")){
                    date = reader.nextString();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Rating(id,value,comment,author,date);
    }
}
