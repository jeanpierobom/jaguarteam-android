package com.example.capstone;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APICaller {

    private static JSONObject jsonObject;

    public static void Get(String URL, final APICallBack call){
        Log.e("GET Function","entered function");
        try{
            final java.net.URL url = new URL(URL);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpsURLConnection myConnexion = (HttpsURLConnection) url.openConnection();
                        int responseCode = myConnexion.getResponseCode();
                        Log.e("GET Function","reaponse code: "+ responseCode);
                        if(responseCode == 200){
                            InputStream responseBody = myConnexion.getInputStream();
                            JsonReader reader = new JsonReader(new InputStreamReader(responseBody, "UTF-8"));
                            Log.e("GET Function","Callback called");
                            call.callBack(reader);
                        }
                        else{
                            Log.e("ASYNC ERROR", "Response from server was: " + String.valueOf(responseCode));
                        }
                        Log.e("GET Function","disconnects");
                        myConnexion.disconnect();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void Post(String URL, String JsonObject){

    }

}