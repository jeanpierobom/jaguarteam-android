package com.example.capstone;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.codelabs.appauth.OutcomAppAuthInfo;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.HashMap;
import java.util.Map;

public class SignInScreenFragment extends Fragment {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button signInButton;
    private SignInButton signInGoogleButton;
    private TextView signUpButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout,container,false);

        emailInput = view.findViewById(R.id.sign_in_input_email);
        passwordInput = view.findViewById(R.id.sign_in_input_password);
        signUpButton = view.findViewById(R.id.to_sign_up);
        signInGoogleButton = view.findViewById(R.id.sign_in_google_button);
        signInButton = view.findViewById(R.id.sign_in_button);
        ((MainActivity)getActivity()).hideBottomNavigation();

        TextView textView = (TextView) signInGoogleButton.getChildAt(0);
        textView.setText("Sign In With Google");
        signInGoogleButton.setOnClickListener(new AuthorizeListener());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_sign_up_screen);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //String email = "student@outcom.io";
            //String password = "secret";
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Build params
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            try {
                APICaller.Post("login", params, new APICallBack() {
                    @Override
                    public void callBack(JsonReader jsonObject) {
                        try {
                            LoginDTO dto = JsonAPIParser.ParseLoginRequest(jsonObject);
                            OutcomAppAuthInfo.getInstance().setLoginInfo(dto, "Token retrieved successfully");
                            Log.e("TEST POST REQUEST","token: " + dto.getToken());
                            Navigation.findNavController(getActivity(),R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_student_home_search);
                        } catch(Exception e) {
                            Log.e("TEST GET REQUEST","Error while parsing token: " + e);
                            OutcomAppAuthInfo.getInstance().setLoginInfo(null, "Error to retrieve token: " + e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("TEST POST REQUEST","An exception has occurred: " + e);
            } finally {
                Log.e("TEST POST REQUEST","This is finally");
            }
            }
        });

        return view;
    }

    /**
     * Kicks off the authorization flow.
     */
    public static class AuthorizeListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {

            // code from the step 'Create the Authorization Request',
            // and the step 'Perform the Authorization Request' goes here.
            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // auth endpoint ,
                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
            );

            // Outcom Appauth
            String clientId = "1024868053428-6jbbeltd4dosd5c3vqq4ochnq5kgvo8f.apps.googleusercontent.com";
            // Outcom Outcom
            Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    clientId,
                    AuthorizationRequest.RESPONSE_TYPE_CODE,
                    redirectUri
            );
            builder.setScopes("profile");
            AuthorizationRequest request = builder.build();

            AuthorizationService authorizationService = new AuthorizationService(view.getContext());

            String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
            Intent postAuthorizationIntent = new Intent(action);
            PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), request.hashCode(), postAuthorizationIntent, 0);
            authorizationService.performAuthorizationRequest(request, pendingIntent);
        }
    }

}
