package edu.itcs4180.hw4_triviaapp;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dallas on 9/21/16.
 */
public class Async_FetchQuestions extends AsyncTask <String, Void, ArrayList<Question>>{

    urlDataReceiver dataReceiver;

    Async_FetchQuestions(urlDataReceiver dataReceiver){
        this.dataReceiver = dataReceiver;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
        //Connect to the URL and get the data
        BufferedReader reader = null;
        ArrayList<Question> questionsList = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK){
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

                String dataString = sb.toString();
                Log.d("data", dataString);

                /** parse dataString into Question objects arraylist */

                questionsList = new ArrayList<>();

                JSONArray questionsArray = new JSONObject(dataString).getJSONArray("questions");

                for (int i = 0; i < questionsArray.length(); i++) {
                    Log.d("test", "calling createFromJSON... " + i);
                    questionsList.add(Question.createQuestionFromJSONOBject(questionsArray.getJSONObject(i)));
                }


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(reader  != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return questionsList;

    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        //pass questions back to main activity
        for (int i = 0; i < questions.size(); i++) {
            Log.d("tesData", questions.get(i).toString());
        }
        dataReceiver.setData(questions);
        dataReceiver.hideLoadingUI();
    }

    @Override
    protected void onPreExecute() {
        dataReceiver.showLoadingUI();
    }

    interface urlDataReceiver{
        public void setData(ArrayList<Question> data);
        public void showLoadingUI();
        public void hideLoadingUI();
    }
}
