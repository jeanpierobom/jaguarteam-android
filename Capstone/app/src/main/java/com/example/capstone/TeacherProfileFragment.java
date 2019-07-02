package com.example.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TeacherProfileFragment extends Fragment {

    private ArrayList<String> languageList;
    ListView availabilityResults;

    ArrayList<Availability> availabilities;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_profile_layout, container, false);

        availabilityResults = view.findViewById(R.id.availability_results);

        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/teacher/", new APICallBack() {
            @Override
            public void callBack(JsonReader jsonObject) {
//                availabilities = JsonAPIParser.ParseTeacherRequest(jsonObject);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        teacherCardAdapter = new TeacherCardAdapter(getContext(),availabilities);
//                        availabilityResults.setAdapter(teacherCardAdapter);
//                    }
//                });
            }
        });


        return view;
    }
}
