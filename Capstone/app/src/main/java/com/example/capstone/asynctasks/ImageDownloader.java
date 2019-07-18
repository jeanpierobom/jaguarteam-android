package com.example.capstone.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private int userId;

    public ImageDownloader(ImageView imageView, int userId) {
        this.imageView = imageView;
        this.userId = userId;
        Log.i("ImageDownloader", "Downloading the image...");
    }

    protected Bitmap doInBackground(String... params) {
        if (userId <= 0) {
            return null;
        }

        String imageURL = "https://outcom-uploads.s3.amazonaws.com/" + userId + ".jpg";
        Bitmap bimage = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }

    protected void onPostExecute(Bitmap result) {
        if (imageView!= null && result !=null) {
            imageView.setImageBitmap(result);
        }
    }

}