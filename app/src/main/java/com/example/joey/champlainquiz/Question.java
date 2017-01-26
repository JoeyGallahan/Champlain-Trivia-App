package com.example.joey.champlainquiz;

import android.widget.TextView;

import java.util.ArrayList;

/*
 * Question stores all the necessary information for a question
 * It holds all of its answers and finds the correct one
 * Created by Joey Gallahan 09/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

public class Question
{
    private String mQuestionText;
    private int mCorrectAnswerIndex;
    private ArrayList<Answer> mAnswers;

    //Constructor
    public Question(String questionText, ArrayList<Answer> answers)
    {
        mQuestionText = questionText;
        mAnswers = answers;
        for (int i = 0; i < answers.size(); i++)
        {
            if (mAnswers.get(i).getIsCorrect())
            {
                mCorrectAnswerIndex = i;
            }
        }

    }

    //Accessors
    public String getQuestionText(){return mQuestionText;}
    public ArrayList<Answer> getAnswers(){return mAnswers;}
    public int getCorrectAnswerIndex(){return mCorrectAnswerIndex;};
    public void setCorrectAnswerIndex(int index){mCorrectAnswerIndex = index;};
}