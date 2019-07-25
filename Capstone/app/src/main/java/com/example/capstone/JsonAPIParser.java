package com.example.capstone;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonAPIParser {

    public static Map<String, Integer> ParseLanguageRequest(JsonReader reader){
        Map<String, Integer> languageList = new HashMap<String, Integer>();
        Pair<String, Integer> language;
        try{
            reader.beginArray();
            while(reader.hasNext()){
                language = readLanguageObject(reader);
                languageList.put(language.first,language.second);
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return languageList;
    }

    public static LoginDTO ParseLoginRequest(JsonReader reader){

        LoginDTO dto = new LoginDTO();

        String fieldName;
        try{
            reader.beginObject();
            while(reader.hasNext()){
                fieldName = reader.nextName();

                // Read the ID
                if(fieldName.equals("id")) {
                    dto.setId(reader.nextInt());
                }

                // Read the token
                if(fieldName.equals("token")) {
                    dto.setToken(reader.nextString());
                }
            }
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dto;
    }

    public static Map<String,Integer> ParseCityRequest(JsonReader reader){
        Map<String,Integer> cities = new HashMap<String, Integer>();
        Pair<Integer,String> city;
        try {
            reader.beginArray();
            while(reader.hasNext()){
                city = readCityObject(reader);
                cities.put(city.second,city.first);
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


    private static Pair<Integer,String> readCityObject(JsonReader reader){
        String city = "";
        int id = -1;
        String name;
        try{
            reader.beginObject();
            while(reader.hasNext()){
                name  = reader.nextName();
                if(name.equals("name")){
                    city = reader.nextString();
                }
                else if(name.equals("id")){
                    id = reader.nextInt();
                }
                else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(id,city);
    }

    private static Pair<String,Integer> readLanguageObject(JsonReader reader){
        String language = "";
        int id = -1;
        String name;
        try {
            reader.beginObject();
            while(reader.hasNext()){
                name = reader.nextName();
                if(name.equals("name")) {
                    language = reader.nextString();
                }
                else if(name.equals("id")){
                    id = reader.nextInt();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<String,Integer>(language,id);
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

    private static Availability readAvailabilityTypeObject(JsonReader reader){
        String date = "";
        String name = "";
        ArrayList<String> availabilities = new ArrayList<String>();
        try {
            reader.beginObject();
            while(reader.hasNext()){
                name = reader.nextName();
                if(name.equals("date")) {
                    date = reader.nextString();
                }
                else if(name.equals("timeSlots")){
                    reader.beginArray();
//                    while(reader.hasNext()){

//                        try {
//                            reader.beginObject();
                            while(reader.hasNext()){
                                availabilities.add(reader.nextString());
                            }
//                            reader.endObject();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

//                    }
                    reader.endArray();
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Availability(name, date, availabilities);
    }

    private static Teacher readTeacherObject(JsonReader reader){
        int id = 0;
        String name = "";
        String email = "";
        String city = "";
        String bio = "";
        ArrayList<String> classes = new ArrayList<String>();
        int countryId = -1;
        Map<String,Integer> languages = new HashMap<String, Integer>();
        float hourRate = -1;
        float distance = 0;
        float rating = 0;
        char teacherType = ' ';
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        ArrayList<Availability> availability = new ArrayList<Availability>();

        String key;
        Pair<String,Integer> aux;

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
                            aux = readLanguageObject(reader);
                            languages.put(aux.first,aux.second);
                        }
                        reader.endArray();
                    }
                    else if(reader.peek() == JsonToken.STRING) {
                        languages.put(reader.nextString(),0);
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
                }else if(key.equals("availability")){
                    reader.beginArray();
                    while(reader.hasNext()){
                        availability.add(readAvailabilityTypeObject(reader));
                    }
                    reader.endArray();
                }else if(key.equals("ratings")){
                    reader.beginArray();
                    while(reader.hasNext()){
                        ratings.add(readRatingObject(reader));
                    }
                    reader.endArray();
                }else if(key.equals("price")){
                    hourRate = (float)reader.nextDouble();
                }else if(key.equals("teacherType")){
                    teacherType = reader.nextString().charAt(0);
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] tempLanguages = new String[languages.size()];
        int cont = 0;
        for(Map.Entry<String,Integer> entry: languages.entrySet()){
            tempLanguages[cont++] = entry.getKey();
        }

        return new Teacher(id,name,email,bio,city,classes.toArray(new String[0]),countryId,tempLanguages,hourRate,distance,rating,ratings.toArray(new Rating[0]), availability, teacherType);
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
