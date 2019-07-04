package com.example.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.ArrayList;

public class StudentHomeFragment extends Fragment {

    private Button searchButton;
    private Spinner languagesDropDown;
    private EditText dateInput;
    private EditText locationInput;
    private ArrayList<String> languageList;
    private ArrayList<String> cities;
    ListView searchResults;
    TeacherCardAdapter teacherCardAdapter;


    ArrayList<Teacher> teachers;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.student_home_layout,container,false);

        searchButton = view.findViewById(R.id.home_search_button);
        searchResults = view.findViewById(R.id.home_search_results);
        languagesDropDown = view.findViewById(R.id.language);
        locationInput = view.findViewById(R.id.location);
        teachers = new ArrayList<Teacher>();


        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/language", new APICallBack() {
            @Override
            public void callBack(JsonReader jsonObject) {
                languageList = JsonAPIParser.ParseLanguageRequest(jsonObject);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, languageList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        languagesDropDown.setAdapter(adapter);
                    }
                });
            }
        });

//        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/city", new APICallBack() {
//            @Override
//            public void callBack(JsonReader jsonObject) {
//                cities = JsonAPIParser.ParseCityRequest(jsonObject);
//            }
//        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEACHER SEARCH","Button tapped");
                APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/teacher/", new APICallBack() {
                    @Override
                    public void callBack(JsonReader jsonObject) {
                        Log.e("TEACHER SEARCH","CallbackCalled");
                        teachers = JsonAPIParser.ParseTeacherRequest(jsonObject);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            teacherCardAdapter = new TeacherCardAdapter(getContext(),teachers);
                            searchResults.setAdapter(teacherCardAdapter);
                            }
                        });
                    }
                });

//                for(int i = 0; i<testNames.length; i++){
//                    Teacher t = new Teacher(testNames[i]);
//                    teachers.add(t);
//                }
            }
        });

        return view;
    }
}
