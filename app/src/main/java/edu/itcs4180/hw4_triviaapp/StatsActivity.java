package edu.itcs4180.hw4_triviaapp;

/**
 * ITCS 4180 Homework4
 * Group39_HW04.zip
 * Dallas Sanchez
 * Patrick King
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {


    ArrayList<Question> questionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        questionsList = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.KEY_QUESTION_LIST);
        int correctAnswers = getIntent().getExtras().getInt(MainActivity.KEY_INT_CORRECT_ANSWERS);
        //correctAnswers = 9;

        Double scoreDecimal = Double.valueOf(correctAnswers) / questionsList.size();
        Double scorePercentage = scoreDecimal * 100;


        //currently rounding the proper way, may need to change to always round down
        int scoreRounded = (int)Math.round(scorePercentage);
        //scoreRounded = 65; //testing colors

        ProgressBar gradeBar = (ProgressBar) findViewById(R.id.progressGrade);
        gradeBar.setProgress(scoreRounded);


        TextView gradeLabel = (TextView)(findViewById(R.id.labelGrade));


        gradeLabel.setText(scoreRounded + "%");

        if (scoreRounded >= 75) {
            gradeLabel.setTextColor(ContextCompat.getColor(this, R.color.darkGreen));
        } else if (scoreRounded >= 50){
            gradeLabel.setTextColor(ContextCompat.getColor(this, R.color.darkYellow));
        } else {
            gradeLabel.setTextColor(ContextCompat.getColor(this, R.color.darkRed));
        }
    }

    public void clickTryAgain(View v) {
        Intent triviaIntent = new Intent(StatsActivity.this, TriviaActivity.class);
        triviaIntent.putExtra(MainActivity.KEY_QUESTION_LIST, questionsList);
        startActivity(triviaIntent);
        finish();
    }

    public void clickExitStats(View v) {
        finish();
    }
}
