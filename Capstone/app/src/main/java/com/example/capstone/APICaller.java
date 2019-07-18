package com.example.capstone;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.codelabs.appauth.OutcomAppAuthInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class APICaller {

    public static final String API_BASE_URL = "https://1hxwhklro6.execute-api.us-east-1.amazonaws.com/prod/";

    private static String getEndpoint(String URL) {
        String endpoint = URL;
        if (!endpoint.startsWith("https://")) {
            endpoint = API_BASE_URL + URL;
        }
        return endpoint;
    }

    public static void Get(String URL, final APICallBack call){
        String endpoint = getEndpoint(URL);
        log("Executing GET method on " + endpoint);

        try{
            final java.net.URL url = new URL(endpoint);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();
                        // Attach the token
                        String token = OutcomAppAuthInfo.getInstance().getToken();
                        if (token != null) {
                            myConnection.setRequestProperty("Authorization", String.format("Bearer %s", token));
                        }

                        int responseCode = myConnection.getResponseCode();
                        Log.e("GET Function","reaponse code: "+ responseCode);
                        if(responseCode == 200){
                            InputStream responseBody = myConnection.getInputStream();
                            JsonReader reader = new JsonReader(new InputStreamReader(responseBody, "UTF-8"));
                            Log.e("GET Function","Callback called");
                            call.callBack(reader);
                        }
                        else{
                            Log.e("ASYNC ERROR", "Response from server was: " + String.valueOf(responseCode));
                        }
                        Log.e("GET Function","disconnects");
                        myConnection.disconnect();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void Post(String URL, Map<String, String> params, final APICallBack call) {
        StringBuilder builder = new StringBuilder();
        int size = params.size();

        // Start object
        builder.append("{");

        // Create params
        int i = 1;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");

            if (i++ < size) {
                builder.append(",");
            }
        }

        // Finish object
        builder.append("}");

        Post(URL, builder.toString(), call);
    }

    public static void Post(String URL, final String jsonObject, final APICallBack call){
        String endpoint = getEndpoint(URL);
        log("Executing POST method on " + endpoint);
        try{
            log("Params: " + jsonObject);
            final java.net.URL url = new URL(endpoint);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                try {
                    // Perform a post request
                    HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();
                    myConnection.setRequestMethod("POST");
                    myConnection.setDoOutput(true);

                    // Attach the token
                    String token = OutcomAppAuthInfo.getInstance().getToken();
                    if (token != null) {
                        myConnection.setRequestProperty("Authorization", String.format("Bearer %s", token));
                    }

                    myConnection.getOutputStream().write(jsonObject.getBytes());
                    int responseCode = myConnection.getResponseCode();

                    // Check the results
                    if(responseCode == 200){
                        log("POST successful");

                        // JSON reader
                        InputStream responseBody = myConnection.getInputStream();
                        JsonReader reader = new JsonReader(new InputStreamReader(responseBody, "UTF-8"));
                        log("Calling callback");
                        call.callBack(reader);
                    }
                    else{
                        log("Response from POST request was " + String.valueOf(responseCode));
                        call.callBack(null);
                    }

                    myConnection.disconnect();
                }catch (IOException e) {
                    log("Error to perform a POST request: " + e.getMessage());
                    e.printStackTrace();
                }
                }
            });
        }catch (MalformedURLException e) {
            log("Error to perform a POST request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void log(String s) {
        Log.e("APICaller", s);
    }

}