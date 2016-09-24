package edu.itcs4180.hw4_triviaapp;

import android.content.Intent;
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
        //correctAnswers = 15;

        Double scoreDecimal = Double.valueOf(correctAnswers) / questionsList.size();
        Double scorePercentage = scoreDecimal * 100;
        Log.d("test", "score percentage: " + scorePercentage);




        ((ProgressBar)(findViewById(R.id.progressGrade))).setProgress(scorePercentage.intValue());
        ((TextView)(findViewById(R.id.labelGrade))).setText(String.valueOf(scorePercentage.intValue()) + "%");
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
