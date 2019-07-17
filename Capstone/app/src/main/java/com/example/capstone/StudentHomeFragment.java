package com.example.capstone;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class StudentHomeFragment extends Fragment {

    private Button searchButton;
    private Spinner languagesDropDown;
    private EditText dateInput;
    private EditText locationInput;
    private Map<String, Integer> languageList;
    private Map<String,Integer> cities;
    private ListView searchResults;
    private TeacherCardAdapter teacherCardAdapter;


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
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Position Clicked","value: " + id);
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SHF Item Click Func","Event Called");
                //Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_student_home_search_to_teacher_profile_confirmation);
            }
        });


        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/language", new APICallBack() {
            @Override
            public void callBack(JsonReader jsonObject) {
                languageList = JsonAPIParser.ParseLanguageRequest(jsonObject);
                ArrayList<String> str = new ArrayList<String>();
                for(Map.Entry<String,Integer> entry: languageList.entrySet()){
                    str.add(entry.getKey());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, str);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        languagesDropDown.setAdapter(adapter);
                    }
                });
            }
        });

        APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/city", new APICallBack() {
            @Override
            public void callBack(JsonReader jsonObject) {
                cities = JsonAPIParser.ParseCityRequest(jsonObject);
                Log.e("SHF APICALL:","first city: " + cities.get("Vancouver"));
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cityID;
                int languageID = languageList.get(languagesDropDown.getSelectedItem().toString());
               if(locationInput.getText()!=null && cities!=null) {
                   cityID = cities.get(locationInput.getText().toString());
               }
               else{
                   cityID = 1;
               }
                //Log.e("SHF APICALL","location Input value:" + locationInput.getText().toString());
                APICaller.Get("https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/teacher/?cityId="+ String.valueOf(cityID)+"&languageId="+languageID, new APICallBack() {
                    @Override
                    public void callBack(JsonReader jsonObject) {
                        Log.e("TEACHER SEARCH","CallbackCalled");
                        teachers = JsonAPIParser.ParseTeacherRequest(jsonObject);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                teacherCardAdapter = new TeacherCardAdapter(getContext(),teachers);
                                searchResults.setAdapter(teacherCardAdapter);
                                Log.e("teacherBuilder","The function reached this point");
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
