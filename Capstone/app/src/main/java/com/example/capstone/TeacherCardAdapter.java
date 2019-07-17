package com.example.capstone;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone.asynctasks.ImageDownloader;

import java.util.ArrayList;

public class TeacherCardAdapter extends ArrayAdapter<Teacher> {

    private Context context;
    private ArrayList<Teacher> teachers;

    public TeacherCardAdapter(Context context, ArrayList<Teacher> teachers){
        super(context,0, teachers);
        this.context = context;
        this.teachers = teachers;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View teacherCard = convertView;

        if(teacherCard == null){
            teacherCard = LayoutInflater.from(this.context).inflate(R.layout.teacher_card,parent,false);
        }

        ImageView image = teacherCard.findViewById(R.id.cardImage);

        Teacher currentTeacher = teachers.get(position);
        ImageDownloader img = new ImageDownloader(image,currentTeacher.getId());
        img.execute("");
        image.getLayoutParams().height = 150;
            image.getLayoutParams().width= 100;
        image.requestLayout();
        ((TextView)teacherCard.findViewById(R.id.name)).setText(currentTeacher.getName());
        ((TextView)teacherCard.findViewById(R.id.languages)).setText(currentTeacher.getLanguage(0));
        ((TextView)teacherCard.findViewById(R.id.distance)).setText(String.valueOf(currentTeacher.getDistance()));
        ((TextView)teacherCard.findViewById(R.id.hourly_rate)).setText(String.valueOf(currentTeacher.getHourlyRate()));

        return teacherCard;
    }
}
