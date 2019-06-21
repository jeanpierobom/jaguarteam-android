package com.example.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;

public class SignInScreenFragment extends Fragment {


    private Button button;
    private TextView signUpButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout,container,false);

        signUpButton = view.findViewById(R.id.to_sign_up);
        button = view.findViewById(R.id.sign_in_button);

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
