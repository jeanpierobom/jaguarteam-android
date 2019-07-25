package com.example.capstone;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.codelabs.appauth.MainApplication;
import com.google.codelabs.appauth.OutcomAppAuthInfo;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavController navigator;
    public static final String LOG_TAG = "Outcom";
    private static final String SHARED_PREFERENCES_NAME = "AuthStatePreference";
    private static final String AUTH_STATE = "AUTH_STATE";
    private static final String USED_INTENT = "USED_INTENT";

    private Teacher selectedTeacher;
    private Availability selectedAvailability;
    private List<String> selectedClassTypes;
    private View bottomNavigation;

    MainApplication mMainApplication;
    AuthState mAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.nav_bottom_view);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this,R.id.navHostFragment).navigateUp();
    }

    public NavController getNavController(){
        return navigator;
    }

    public void hideBottomNavigation(){
        bottomNavigation.setVisibility(View.GONE);
    }

    public void showBottomNavigation(){
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void setTeacher(Teacher t){
        selectedTeacher = t;
    }

    public Teacher getTeacher(){
        return selectedTeacher;
    }

    public void setAvailability(Availability a){
        selectedAvailability = a;
    }
    public Availability getAvailability(){
        return selectedAvailability;
    }

    public void setClassTypes(List<String> c){
        selectedClassTypes = c;
    }
    public List<String> getClassTypes(){
        return selectedClassTypes;
    }
    //OAuth
    @Override
    protected void onNewIntent(Intent intent) {
        checkIntent(intent);
    }

    private void checkIntent(@Nullable Intent intent) {
        Log.e(LOG_TAG, "CHECK INTENT");

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            switch (action) {
                case "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE":
                    if (!intent.hasExtra(USED_INTENT)) {
                        handleAuthorizationResponse(intent);
                        intent.putExtra(USED_INTENT, true);
                    }
                    break;
                default:
                    // do nothing
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIntent(getIntent());
    }

    /**
     * Kicks off the authorization flow.
     */
//    public static class AuthorizeListener implements Button.OnClickListener {
//        @Override
//        public void onClick(View view) {
//
//            // code from the step 'Create the Authorization Request',
//            // and the step 'Perform the Authorization Request' goes here.
//            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
//                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
//                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
//            );
//
//            // Outcom Appauth
//            String clientId = "1024868053428-6jbbeltd4dosd5c3vqq4ochnq5kgvo8f.apps.googleusercontent.com";
//            // Outcom Outcom
//            Uri redirectUri = Uri.parse("com.google.codelabs.appauth:/oauth2callback");
//            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
//                    serviceConfiguration,
//                    clientId,
//                    AuthorizationRequest.RESPONSE_TYPE_CODE,
//                    redirectUri
//            );
//            builder.setScopes("profile");
//            AuthorizationRequest request = builder.build();
//
//            AuthorizationService authorizationService = new AuthorizationService(view.getContext());
//
//            String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
//            Intent postAuthorizationIntent = new Intent(action);
//            PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), request.hashCode(), postAuthorizationIntent, 0);
//            authorizationService.performAuthorizationRequest(request, pendingIntent);
//        }
//    }

    /**
     * Exchanges the code, for the {@link TokenResponse}.
     *
     * @param intent represents the {@link Intent} from the Custom Tabs or the System Browser.
     */
    private void handleAuthorizationResponse(@NonNull Intent intent) {

        // code from the step 'Handle the Authorization Response' goes here.
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        final AuthState authState = new AuthState(response, error);

        if (response != null) {
            Log.i(LOG_TAG, String.format("Handled Authorization Response %s ", authState.toJsonString()));
            AuthorizationService service = new AuthorizationService(this);

            final Activity activity = this;

            service.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                    if (exception != null) {
                        Log.w(LOG_TAG, "Token Exchange failed", exception);
                    } else {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            persistAuthState(authState);
                            Log.i(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken));

                            // Store access token for future use
                            OutcomAppAuthInfo.getInstance().setToken(tokenResponse.idToken);
                            OutcomAppAuthInfo.getInstance().setMessage("Google Token was retrieve successfully");
                            OutcomAppAuthInfo.getInstance().setLoginGoogle(true);

                            // Navigate to home screen
                            Navigation.findNavController(activity,R.id.navHostFragment).navigate(R.id.action_sign_in_screen_to_student_home_search);
                        }
                    }
                }
            });
        }
    }

    private void persistAuthState(@NonNull AuthState authState) {
        getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                .putString(AUTH_STATE, authState.toJsonString())
                .commit();

        Log.e("PERSISTING AUTH STATE", "AUTH STATE toString(): " + authState.toJsonString());
        //enablePostAuthorizationFlows();
    }

    private void clearAuthState() {
        getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(AUTH_STATE)
                .apply();
    }

    @Nullable
    private AuthState restoreAuthState() {
        String jsonString = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(AUTH_STATE, null);
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return AuthState.fromJson(jsonString);
            } catch (JSONException jsonException) {
                // should never happen
            }
        }
        return null;
    }

}
