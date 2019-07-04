package com.example.capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityCardAdapter extends ArrayAdapter<Availability> {

    private Context context;
    private ArrayList<Availability> availabilities;

    public ActivityCardAdapter(Context context, ArrayList<Availability> availabilities){
        super(context,0, availabilities);
        this.context = context;
        this.availabilities = availabilities;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View teacherCard = convertView;


        Availability currentAvailabilities = availabilities.get(position);

        if(teacherCard == null){
            teacherCard = LayoutInflater.from(this.context).inflate(R.layout.availability_card,parent,false);


            LinearLayout linearLayout = ((LinearLayout)teacherCard.findViewById(R.id.linear_card));

            for (String timeSlot : currentAvailabilities.getTimeSlots()) {
                TextView newTimeSlot = new TextView(teacherCard.getContext());
                newTimeSlot.setText(timeSlot);
                linearLayout.addView(newTimeSlot);
            }

        }

        ((TextView)teacherCard.findViewById(R.id.availability_date)).setText(currentAvailabilities.getDate());

        return teacherCard;
    }
}
