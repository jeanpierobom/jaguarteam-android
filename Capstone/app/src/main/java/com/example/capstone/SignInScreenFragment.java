package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;

public class SignInScreenFragment extends Fragment {


    private Button button;
    private Button signInGoogleButton;
    private TextView signUpButton;
    private Button toTeacherProfileConfirmation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout,container,false);

        signUpButton = view.findViewById(R.id.to_sign_up);
        signInGoogleButton = view.findViewById(R.id.sign_in_google_button);
        toTeacherProfileConfirmation = view.findViewById(R.id.to_teacher_profile_confirmation);
        button = view.findViewById(R.id.sign_in_button);

        toTeacherProfileConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_teacher_profile_confirmation);
            }
        });

        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.google.codelabs.appauth.MainActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_sign_up_screen);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_student_home_search);
            }
        });

        return view;
    }


}
