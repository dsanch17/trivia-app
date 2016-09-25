package edu.itcs4180.hw4_triviaapp;

/**
 * ITCS 4180 Homework4
 * Group39_HW04.zip
 * Dallas Sanchez
 * Patrick King
 */

import android.os.AsyncTask;
import android.util.Log;

import java.util.GregorianCalendar;


public class Async_Timer extends AsyncTask<Void, Integer, Void> {

    int totalTime;
    TriviaTimer triviaTimer;

    Async_Timer(TriviaTimer triviaTimer, int totalTime) {
        this.totalTime = totalTime;
        this.triviaTimer = triviaTimer;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        //initialize counter can and find initial time
        int timeLeft = totalTime;
        int counter = 0;
        int initTime = (int) (new GregorianCalendar().getTimeInMillis() / 1000);
        Log.d("testTime","initTime: " + initTime);


        //while the timeLeft value is greater than 0 execute as long as not canceled
        while (timeLeft > 0 && !this.isCancelled()) {

            //get the currentTime in seconds at the top of the loop
            int timeNow = (int) (new GregorianCalendar().getTimeInMillis() / 1000);
            int timeElapsed = timeNow - initTime;

            //compare the time elapsed to the counter value
            if (timeElapsed != counter) { //if they dont match we need to update the timeLeft and UI
                Log.d("testTime", "timeElapsed: " + timeElapsed);
                timeLeft--;
                counter++; //the counter actually makes this impossible to cheat with calender change
                publishProgress(timeLeft);
            } else {
                try {      //else wait 50ms and check again - this ensures 1/20th second accuracy
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        triviaTimer.finishTimer();
    }

    @Override
    protected void onCancelled() {
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
