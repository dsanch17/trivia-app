package edu.itcs4180.hw4_triviaapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Async_FetchQuestions.urlDataReceiver {

    public static final String KEY_QUESTION_LIST = "trivia list";
    public static final String KEY_ANSWERS_LIST = "answers list";

    public static final int TIMER_LENGTH = 120;

    ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected()) {
            loadQuestions();
        } else {
            showNetworkErrorUI();
        }
    }

    void loadQuestions() {
        new Async_FetchQuestions(MainActivity.this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
    }


    boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        Log.d("internet",""+(netInfo != null && netInfo.isConnectedOrConnecting()));
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    public void clickStartTrivia(View v) {
        Intent triviaIntent = new Intent(MainActivity.this, TriviaActivity.class);
        triviaIntent.putExtra(KEY_QUESTION_LIST, questionList);
        startActivity(triviaIntent);
    }

    public void clickExit(View v) {
        finish();
    }

    @Override
    public void setData(ArrayList<Question> data) {
        questionList = data;
    }

    @Override
    public void showLoadingUI() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonStart).setEnabled(false);


        ImageView image = (ImageView) findViewById(R.id.imageTrivia);
        image.setVisibility(View.INVISIBLE);

        TextView text = (TextView) findViewById(R.id.labelStatus);
        text.setText(getResources().getString(R.string.loading_trivia));

    }

    @Override
    public void hideLoadingUI() {

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.buttonStart).setEnabled(true);



        ImageView image = (ImageView) findViewById(R.id.imageTrivia);
        image.setVisibility(View.VISIBLE);

        TextView text = (TextView) findViewById(R.id.labelStatus);
        text.setText(getResources().getString(R.string.trivia_ready));

    }

    void showNetworkErrorUI() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.buttonStart).setEnabled(false);

        TextView text = (TextView) findViewById(R.id.labelStatus);
        text.setText(getResources().getString(R.string.network_error));
    }
}
