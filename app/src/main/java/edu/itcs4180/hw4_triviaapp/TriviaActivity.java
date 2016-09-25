package edu.itcs4180.hw4_triviaapp;

/**
 * ITCS 4180 Homework4
 * Group39_HW04.zip
 * Dallas Sanchez
 * Patrick King
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements Async_FetchImage.ImageInterface, Async_Timer.TriviaTimer {

    RadioGroup answersGroup;
    ArrayList<Question> questionsList;
    int correctAnswers = 0;
    int currentIndex = 0;
    int numOfQuestions;
    Async_Timer timerATask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);


        answersGroup = (RadioGroup) findViewById(R.id.radioGroupAnswers);
        questionsList = (ArrayList<Question>) getIntent().getExtras().getSerializable(MainActivity.KEY_QUESTION_LIST);

        numOfQuestions = questionsList.size();

        displayQuestion(currentIndex);

        //startTimer
        timerATask =  new Async_Timer(TriviaActivity.this, MainActivity.TIMER_LENGTH);
        timerATask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    //on click check the answer and then display the next question
    public void clickNext(View v) {

        checkAnswer();

        currentIndex++;

        if (currentIndex < numOfQuestions) { //unless it's the last questions
            displayQuestion(currentIndex);  //then start the result screen
        } else {
            gotoResults();
        }
    }

    public void clickQuit(View v) {
        timerATask.cancel(true);
        finish();
    }

    void gotoResults() {
        timerATask.cancel(true);

        Intent resultIntent = new Intent(TriviaActivity.this, StatsActivity.class);
        resultIntent.putExtra(MainActivity.KEY_QUESTION_LIST, questionsList);
        resultIntent.putExtra(MainActivity.KEY_INT_CORRECT_ANSWERS, correctAnswers);
        startActivity(resultIntent);
        finish();
    }

    void checkAnswer() {
        if (currentIndex < numOfQuestions) {

            //since the id is set via i in the loop, it is only offset by 1 with the index of the item
            // (id can't be 0 so it has to be off by 1)
            int selected = answersGroup.getCheckedRadioButtonId() - 1;
            int correct = questionsList.get(currentIndex).answer;
            Log.d("testCheck", "selected: " + selected);
            Log.d("testCheck", "correct is: " + correct);
            if (selected == correct) {
                correctAnswers++;
            }
        }
    }

    void displayQuestion(int index) {
        Question question = questionsList.get(index);
        addAnswers(question);

        ((TextView)(findViewById(R.id.labelQuestionText))).setText(question.text);
        ((TextView)(findViewById(R.id.labelQuesitonNum))).setText("Q" + (index + 1));


        if (question.imageURL != null) {
            new Async_FetchImage(TriviaActivity.this, index).execute(question.imageURL);
        } else {
            findViewById(R.id.imageTrivia).setVisibility(View.INVISIBLE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

    }

    void removeAnswers() {
        answersGroup.clearCheck();
        answersGroup.removeAllViews();
    }

    void addAnswers(Question question) {
        //before adding answers remove the old ones
        removeAnswers();

        //add the questions to the radio group by adding to the top and working backwards
        for (int i = question.choices.length; i > 0; i--) {
            RadioButton newRadioButton = new RadioButton(this);
            newRadioButton.setText(question.choices[i - 1]);
            newRadioButton.setId(i); //id can't be 0 and wont
            //newRadioButton.setTextSize(25);
            // could change text size if I wanted, radioGroup is in a scrollView so it would work

            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            answersGroup.addView(newRadioButton, 0, layoutParams);

            newRadioButton.setChecked(true); //the last (top) button ends up being the only one checked
        }

    }

    @Override
    public void showLoading() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        ImageView image = (ImageView) findViewById(R.id.imageTrivia);
        image.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showImage(Bitmap image, int num) {
        //ensure that the image is only shown if the num from the task matches the currentIndex
        if (num == currentIndex) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            ImageView imageView = (ImageView) findViewById(R.id.imageTrivia);
            imageView.setImageBitmap(image);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTimer(int num) {
        ((TextView)(findViewById(R.id.labelTimeLeft))).setText("Time Left: " + num + " seconds");
    }

    @Override
    public void finishTimer() {
        checkAnswer();
        gotoResults();
    }
}
