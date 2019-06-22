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
        try{
            final java.net.URL url = new URL(URL);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpsURLConnection myConnexion = (HttpsURLConnection) url.openConnection();
                        int responseCode = myConnexion.getResponseCode();
                        if(responseCode == 200){
                            InputStream responseBody = myConnexion.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr;
                            while ((inputStr = reader.readLine()) != null)
                                responseStrBuilder.append(inputStr);

                            try{
                                jsonObject = new JSONObject(responseStrBuilder.toString());
                            }catch (JSONException e) {
                                e.printStackTrace();
                                jsonObject = null;
                            }
                        }
                        else{
                            Log.e("ASYNC ERROR", "Response from server was: " + String.valueOf(responseCode));
                        }
                        myConnexion.disconnect();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            call.callBack(jsonObject);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void Post(String URL, String JsonObject){

    }

}