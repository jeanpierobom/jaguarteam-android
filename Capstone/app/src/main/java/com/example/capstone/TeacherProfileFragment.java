package com.example.capstone;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.capstone.asynctasks.ImageDownloader;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeacherProfileFragment extends Fragment {

    ImageView userAvatar;
    RatingBar userRating;
    TextView userName, userDescription, classTime, classLanguage, classCost;
    AlertDialog.Builder alert;

    List<TextView> activities = new ArrayList<TextView>();
    List<TextView> availabilitiesTimeSlots = new ArrayList<TextView>();
    List<TextView> availabilitiesDate = new ArrayList<TextView>();
    Availability availabilityDateSelected;
    String availabilityTimeSlotSelected;
    String activityToBook = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_profile_layout, container, false);


        //==========================================================================================
        userAvatar = view.findViewById(R.id.userAvatar);
        userName = view.findViewById(R.id.userName);
        userDescription = view.findViewById(R.id.userDescription);
        userRating = view.findViewById(R.id.userRating);
        classTime = view.findViewById(R.id.classTime);
        classLanguage = view.findViewById(R.id.classLanguage);
        classCost = view.findViewById(R.id.classCost);


        Teacher selectedTeacher = ((MainActivity)getActivity()).getTeacher();
        Log.e("Retrieving Teacher:","Retrieved Teacher is: " + selectedTeacher.getName());



        // TODO: the following data should come from the database:
        ImageDownloader imgDownloader = new ImageDownloader(userAvatar, selectedTeacher.getId());
        imgDownloader.execute("");
        String name = selectedTeacher.getName();
        String teacherType = "Community Teacher";
        String location = selectedTeacher.getCity();
        float rating = selectedTeacher.getRating();
        String time = "3:00 PM - 4:00 PM Sat, Jun 3";
        String language = selectedTeacher.getLanguage(0);
        double cost = selectedTeacher.getHourlyRate();

        // Populating UI
        // userAvatar.setImageResource(avatar);
        userName.setText(name);
        userDescription.setText(teacherType + " from " + location);
        userRating.setRating(rating);
        classTime.setText(time);
        classLanguage.setText(language);
        classCost.setText("CA$ " + cost);

        //==========================================================================================


        loadActivities(view, selectedTeacher);
        loadAvailabilities(view, selectedTeacher);
        loadBookClass(view);
        availabilitiesDate.get(0).callOnClick();

        return view;

    }

    public void loadBookClass(View view){

        Button btn = view.findViewById(R.id.button_book_class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).setClassTypes(activityToBook);
                ((MainActivity)getActivity()).setAvailability(new Availability(availabilityDateSelected.getDate(), availabilityTimeSlotSelected));
                Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_student_home_search_to_teacher_profile_confirmation);

            }
        });

    }


    public void loadActivities(View view, Teacher teacher){

        FrameLayout frame = view.findViewById(R.id.activities_box);
        int count = 0;

        LinearLayout linearLayoutActivities = new LinearLayout(frame.getContext());
        frame.addView(linearLayoutActivities);

        for (String classType : teacher.getClassTypes()) {

            count++;

            if(count > 3){
                count = 1;
                linearLayoutActivities = new LinearLayout(frame.getContext());
                frame.addView(linearLayoutActivities);
            }

            TextView newClassType = new TextView(linearLayoutActivities.getContext());
            newClassType.setText(classType);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            params.weight = 1;
            params.gravity = Gravity.CENTER;
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 10;
            params.bottomMargin = 10;

            newClassType.setLayoutParams(params);
            newClassType.setGravity(Gravity.CENTER);
            newClassType.setPadding(20,20,20,20);

            newClassType.setBackgroundColor(getResources().getColor(R.color.background_color));

//            newClassType.setBackground(R.drawable.textview_border);
            newClassType.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_border));

            newClassType.setTextColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
            newClassType.setTag("NOT SELECTED");

            activities.add(newClassType);

            newClassType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    for (TextView activity :activities) {
                        activity.setBackgroundColor(getResources().getColor(R.color.background_color));
                        ((TextView)activity).setTextColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
                        activity.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_border));
                        activity.setTag("NOT SELECTED");
                    }

                    v.setTag("SELECTED");
                    v.setBackgroundColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
                    ((TextView)v).setTextColor(getResources().getColor(R.color.buttonPrimaryTextColor));
                    v.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_border_selected));
                    activityToBook = ((TextView) v).getText().toString();



                }
            });

            linearLayoutActivities.addView(newClassType);

        }
    }

    public String parseDate(View view, String date){
        String finalDate = "";

        //2019-07-26

        Date realDate = new Date();

        try {
            realDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String year = new SimpleDateFormat("yyyy").format(realDate);
        String dayOfTheWeek = new SimpleDateFormat("EE").format(realDate);
        String month = new SimpleDateFormat("MMMM").format(realDate);
        String day = new SimpleDateFormat("dd").format(realDate);

        TextView availabilityDate = view.findViewById(R.id.availability_date);
        availabilityDate.setText(month + ", " +year);

        finalDate = dayOfTheWeek + "\n" + day;

        return finalDate;
    }


    public void loadAvailabilities(final View view, final Teacher teacher){
        LinearLayout frame = view.findViewById(R.id.availabilities_dates);

        for (Availability availability : teacher.getAvailability()) {

            TextView newClassType = new TextView(frame.getContext());
//            newClassType.setText(availability.getDate());
            newClassType.setTag(availability.getDate());

            newClassType.setText(parseDate(view, availability.getDate()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            params.weight = 1;
            params.gravity = Gravity.CENTER;
            params.leftMargin = 5;
            params.rightMargin = 5;
            params.topMargin = 10;
            params.bottomMargin = 10;

            newClassType.setLayoutParams(params);
            newClassType.setGravity(Gravity.CENTER);

            newClassType.setBackgroundColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
            newClassType.setTextColor(getResources().getColor(R.color.buttonPrimaryTextColor));

            availabilitiesDate.add(newClassType);

            newClassType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (TextView availabilityDate : availabilitiesDate) {
                        availabilityDate.setBackgroundColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
                        ((TextView) availabilityDate).setTextColor(getResources().getColor(R.color.buttonPrimaryTextColor));
                    }

                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    ((TextView) v).setTextColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
                    v.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_border));

                    for (Availability availabilityAux : teacher.getAvailability()) {
                        if(availabilityAux.getDate().equals(((TextView) v).getTag())){
                            availabilityDateSelected = availabilityAux;
                        }
                    }

                    loadAvailabilitiesTimeSlots(view);

                }
            });

            frame.addView(newClassType);
        }
    }


    public void loadAvailabilitiesTimeSlots(View view){

        LinearLayout frame = view.findViewById(R.id.availabilities_box_timeslots);
        frame.removeAllViews();
        int count = 0;

        LinearLayout linearLayoutActivities = new LinearLayout(frame.getContext());
        frame.addView(linearLayoutActivities);

        for (String timeSlot : availabilityDateSelected.getTimeSlots()) {

            count++;

            if (count > 3) {
                count = 1;
                linearLayoutActivities = new LinearLayout(frame.getContext());
                frame.addView(linearLayoutActivities);
            }

            TextView newClassType = new TextView(linearLayoutActivities.getContext());
            newClassType.setText(timeSlot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            params.weight = 1;
            params.gravity = Gravity.CENTER;
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 10;
            params.bottomMargin = 10;

            newClassType.setLayoutParams(params);
            newClassType.setGravity(Gravity.CENTER);

            newClassType.setBackgroundColor(getResources().getColor(R.color.background_color));
//            newClassType.setTextColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
            newClassType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            newClassType.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_timeslot));

            newClassType.setTag("NOT SELECTED");

            availabilitiesTimeSlots.add(newClassType);

            newClassType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (TextView timeSlot : availabilitiesTimeSlots) {
                        timeSlot.setBackgroundColor(getResources().getColor(R.color.background_color));
                        ((TextView) timeSlot).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        timeSlot.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_timeslot));
                    }

                    v.setBackgroundColor(getResources().getColor(R.color.buttonPrimaryBackgroundColor));
                    ((TextView) v).setTextColor(getResources().getColor(R.color.buttonPrimaryTextColor));
                    v.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.textview_border_selected));

                    availabilityTimeSlotSelected = ((TextView) v).getText().toString();
                }
            });

            linearLayoutActivities.addView(newClassType);
        }
    }

}
