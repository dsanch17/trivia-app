package edu.itcs4180.hw4_triviaapp;

import android.os.AsyncTask;
import android.os.Handler;


/**
 * Created by Dallas on 9/23/16.
 */
public class Async_Timer extends AsyncTask<Void, Integer, Void> {

    int timeLeft;
    int totalTime;
    TriviaTimer triviaTimer;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    Async_Timer(TriviaTimer triviaTimer, int totalTime) {
        this.totalTime = totalTime;
        this.timeLeft = totalTime;
        this.triviaTimer = triviaTimer;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        while (timeLeft > 0) {
            publishProgress(timeLeft);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
            timeLeft--;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        triviaTimer.finishTimer();
    }

    @Override
    protected void onPreExecute() {
        triviaTimer.setTimer(totalTime);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        triviaTimer.setTimer(values[0]);
    }

    interface TriviaTimer {
        public void setTimer(int num);
        public void finishTimer();
    }
}
