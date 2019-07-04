package com.example.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignUpScreenFragment extends Fragment {
    private String userType = "student";
    private TextInputEditText fullName;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputEditText email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_layout, container, false);

        return view;
    }
}
