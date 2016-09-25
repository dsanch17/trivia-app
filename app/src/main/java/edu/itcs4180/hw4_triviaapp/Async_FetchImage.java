package edu.itcs4180.hw4_triviaapp;

/**
 * ITCS 4180 Homework4
 * Group39_HW04.zip
 * Dallas Sanchez
 * Patrick King
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Async_FetchImage extends AsyncTask<String, Void, Bitmap> {

    ImageInterface imageInterface;
    int imgIndex;
    InputStream urlStream = null;

    Async_FetchImage(ImageInterface i, int num) {
        this.imageInterface = i;
        this.imgIndex = num;
    }




    @Override
    protected Bitmap doInBackground(String... strings) {


        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            urlStream = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(urlStream);
            return  bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlStream != null )
                try {
                    urlStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        if (urlStream != null )
            try {
                urlStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onPreExecute() {
        imageInterface.showLoading();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageInterface.showImage(bitmap, imgIndex);
    }


    interface ImageInterface {
        void showLoading();
        void showImage(Bitmap image, int num);
    }
}
