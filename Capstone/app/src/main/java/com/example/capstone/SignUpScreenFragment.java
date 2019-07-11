package com.example.capstone;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignUpScreenFragment extends Fragment {

    private EditText signupName;
    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupConfirmPassword;
    private EditText signupBirthDate;
    private Button signupButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_layout, container, false);

        signupName = view.findViewById(R.id.signup_name);
        signupEmail = view.findViewById(R.id.signup_email);
        signupPassword = view.findViewById(R.id.signup_password);
        signupConfirmPassword = view.findViewById(R.id.signup_confirm_password);
        signupBirthDate = view.findViewById(R.id.signup_birthdate);
        signupButton = view.findViewById(R.id.signup_submit_button);

        return view;
    }
}
