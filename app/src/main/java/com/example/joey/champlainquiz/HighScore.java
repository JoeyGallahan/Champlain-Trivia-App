package com.example.joey.champlainquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/*
 * HighScore stores all the necessary information for a High Score.
 * It performs all functions for a high score including saving, loading, and displaying
 * Created by Joey Gallahan 10/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

public class HighScore
{
    private LinearLayout mHighScoreBox;     //Where the highscores are displayed
    private ArrayList<Integer> mScoresList; //List of all the highscore values
    private ArrayList<String> mInitialsList;//List of all the highscore initials
    private Activity mActivity;             //The activity the highscore is on
    private String mFileName;               //The name of the file being saved & loaded

    HighScore(Activity activity, int layoutID, String file)
    {
        mActivity = activity;
        mHighScoreBox = (LinearLayout) mActivity.findViewById(layoutID);
        mFileName = file;

        mScoresList = new ArrayList<Integer>();
        mInitialsList = new ArrayList<String>();
    }

    public int getScore(int index){return mScoresList.get(index);}
    public String getInitial(int index){return mInitialsList.get(index);}
    public int getNumScores(){return mScoresList.size();}

    //Save the user's initals and high score into the text file
    //Returns true or false if it actually worked
    public boolean saveScore(String initials, int score)
    {
        /*
         * File output taken from http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
         * Modified by Joey Gallahan on 10/03/16
         */
        try
        {
            OutputStreamWriter outputStream = new OutputStreamWriter(mActivity.openFileOutput(mFileName, mActivity.MODE_APPEND));
            String temp = initials + "-" + String.valueOf(score) + ".";
            outputStream.append(temp);
            outputStream.close();
            return true;
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }

    //Loads all previous highscores that were saved to the text file
    //Returns a single string of everything in the text file
    public String loadScores()
    {
        String output = "";

        /*
         * File input taken from http://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
         * Modified by Joey Gallahan on 10/03/16
         */
        try
        {
            InputStream inputStream = mActivity.openFileInput(mFileName);

            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null )
                {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                output = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e("Exception", "File not found: " + e.toString());
        }
        catch (IOException e)
        {
            Log.e("Exception", "File read failed: " + e.toString());
        }

        return output;
    }

    //Parses one string to properly store all initials and scores
    public void parseScores(String allScores)
    {
        String temp = "";

        //separate initials and score by a '-' and next highschore with a '.'
        for (int i = 0; i < allScores.length(); i++)
        {
            if (allScores.charAt(i) != '-' && allScores.charAt(i) != '.')
            {

                temp += allScores.charAt(i);
            }
            else if (allScores.charAt(i) == '-')
            {
                //If it hits a '-' that means it reached the end of the initials
                mInitialsList.add(temp);
                temp = "";
            }
            else if (allScores.charAt(i) == '.')
            {
                //If it hits a '.' that means it reached the end of the score
                mScoresList.add(Integer.parseInt(temp));
                temp = "";
            }
        }

        sortScores();
    }

    //Adds a single set of initials and score to the screen
    public void addScoreToScreen(String initials, int score)
    {
        /*
         * Adding a Dynamic TextView was taken from http://stackoverflow.com/questions/7354034/dynamically-add-a-textview-android
         * Modified by Joey Gallahan on 10/03/16
         */

        String temp = initials + " - " + String.valueOf(score) + "\n"; //put everything together

        // Make the new text view
        TextView tv = new TextView(mActivity);
        tv.setText(temp);

        TextView test = (TextView) mActivity.findViewById(R.id.score_params);
        tv.setLayoutParams(test.getLayoutParams());
        tv.setVisibility(View.VISIBLE);

        //for some reason it wont carry over the following params in the xml file, but works if I specify here
        tv.setTextColor(mActivity.getResources().getColor(R.color.white));
        tv.setTextSize(20);
        tv.setTypeface(null, Typeface.BOLD);

        mHighScoreBox.addView(tv);
    }

    //Sort both arrays because they go hand in hand
    public void sortScores()
    {
        boolean doneSorting = false;

        //Sort the list by score Yay bubble sort
        while (!doneSorting)
        {
            doneSorting = true;
            for (int i = 0; i < mScoresList.size() - 1;i++)
            {
                if (mScoresList.get(i + 1) != null && mScoresList.get(i) < mScoresList.get(i+1))
                {
                    int score = mScoresList.get(i);
                    String inits = mInitialsList.get(i);

                    //Swap Scores
                    mScoresList.set(i, mScoresList.get(i+1));
                    mScoresList.set(i+1, score);

                    //Also swap initials
                    mInitialsList.set(i, mInitialsList.get(i+1));
                    mInitialsList.set(i+1, inits);

                    doneSorting = false;
                }
            }
        }
    }
}
