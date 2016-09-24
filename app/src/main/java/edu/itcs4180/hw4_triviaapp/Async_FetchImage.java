package edu.itcs4180.hw4_triviaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dallas on 9/22/16.
 */
public class Async_FetchImage extends AsyncTask<String, Void, Bitmap> {

    ImageInterface imageInterface;
    InputStream urlStream = null;

    Async_FetchImage(ImageInterface i) {
        this.imageInterface = i;
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
        imageInterface.showImage(bitmap);
    }


    interface ImageInterface {
        void showLoading();
        void showImage(Bitmap image);
    }
}
