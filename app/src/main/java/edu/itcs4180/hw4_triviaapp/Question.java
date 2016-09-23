package edu.itcs4180.hw4_triviaapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Dallas on 9/21/16.
 */
public class Question implements Serializable{

    int id;
    String text;
    String imageURL;
    String[] choices;
    int answer;

    public Question(int id, String text, String imageURL, String[] choices, int answer) {
        this.id = id;
        this.text = text;
        this.imageURL = imageURL;
        this.choices = choices;
        this.answer = answer;
    }

    // take Json 1 of the JSON objects and return a question object
    public static Question createQuestionFromJSONOBject(JSONObject q) throws JSONException {
        int id = q.getInt("id");
        String text = q.getString("text");
        String imageURL = null;
        try {
         imageURL = q.getString("image");
        } catch (JSONException j) {
            imageURL = null;
        }

        JSONObject choicesObject = q.optJSONObject("choices");
        int answer = choicesObject.getInt("answer") - 1;

        JSONArray choicesJSONArray = choicesObject.getJSONArray("choice");

        String[] choices = new String[choicesJSONArray.length()];

        for (int i = 0; i < choicesJSONArray.length(); i++) {
            choices[i] = choicesJSONArray.getString(i);
        }


        Question newQ = new Question(id, text, imageURL, choices, answer);

        return newQ;


    }


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", choices=" + Arrays.toString(choices) +
                ", answer=" + answer +
                '}';
    }
}

/**
 * JSON Structure:
 *
 * Object {
 *   has all the questions
 *   question Array [
 *      array is of question objects
 *      {
 *      "id": "9",
 *      "text": "Which of the following countries is landlocked?",
 *      "image": !-- Optional --! "http://dev.theappsdr.com/apis/trivia_json/photos/amphibians.png",
 *      "choices": Object{
 *              "choice": Array[
 *                  "Brazil",
 *                  "Cuba",
 *                  ...
 *              ],
 *              "answer":"3" //not 0 indexed
 *          }
 *      }
 *   .
 *   . //more question objects
 *   .
 *   ]
 * }
 *
 *
 *
 */
