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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class StudentHomeFragment extends Fragment {

    Button searchButton;
    ListView searchResults;
    TeacherCardAdapter teacherCardAdapter;
    String[] testNames = {"Anderson Borbs", "Jean Pierot", "Cassius Montanhes", "Gian Pasta", "Ed Cabron"};
    ArrayList<Teacher> teachers;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.student_home_layout,container,false);

        searchButton = view.findViewById(R.id.home_search_button);
        searchResults = view.findViewById(R.id.home_search_results);
        teachers = new ArrayList<Teacher>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<testNames.length; i++){

                    APICaller.Get("www.get.this", new APICallBack() {
                        @Override
                        public void callBack(JSONObject jsonObject) {

                        }
                    });
                    Teacher t = new Teacher(testNames[i]);
                    teachers.add(t);
                }
                teacherCardAdapter = new TeacherCardAdapter(getContext(),teachers);
                searchResults.setAdapter(teacherCardAdapter);

            }
        });

        return view;
    }
}
