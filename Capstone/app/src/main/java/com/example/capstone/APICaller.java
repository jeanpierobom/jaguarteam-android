package com.example.capstone;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APICaller {

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
                            JsonReader bodyParser = new JsonReader(new InputStreamReader(responseBody, "UTF-8"));
                            call.callBack(bodyParser);
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
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void push(String URL, String JsonObject){

    }

}