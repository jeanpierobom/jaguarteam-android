package com.example.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TeacherProfileFragment extends Fragment {

    ListView teacherResults;



    ArrayList<Teacher> teachers;
    AvailabilityCardAdapter teacherCardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_profile_layout, container, false);

        teachers = new ArrayList<Teacher>();

        teacherResults = view.findViewById(R.id.teacher_result);

        Log.e("PROFILE","STARTED");
        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/teacher/13", new APICallBack() {
            @Override
            public void callBack(JsonReader jsonObject) {
                Log.e("PROFILE","CallbackCalled");
                teachers.add(JsonAPIParser.ParseTeacherProfileRequest(jsonObject));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    teacherCardAdapter = new AvailabilityCardAdapter(getContext(),teachers.get(0).getAvailability());
                    teacherResults.setAdapter(teacherCardAdapter);

                    loadActivities(teachers);

                    }
                });
            }
        });

        return view;

    }


    public void loadActivities(ArrayList<Teacher> teachers){
        LinearLayout linearLayout = getView().findViewById(R.id.activity_result);
        for (String classType : teachers.get(0).getClassTypes()) {
            TextView newClassType = new TextView(linearLayout.getContext());
            newClassType.setText(classType);
            linearLayout.addView(newClassType);
        }

    }
}
