package com.example.joey.champlainquiz;

/*
 * QuizActivity performs the main functions of the quiz
 * It gathers all the questions and answers from the xml files, displays them, and then
 * updates based on the user's input
 * Created by Joey Gallahan 10/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private static final String CATEGORY = "com.example.joey.champlainquiz";

    public static Intent newIntent(Context packageContext, int questionCategory)
    {
        Intent i = new Intent(packageContext, QuizActivity.class);
        i.putExtra(CATEGORY, questionCategory);
        return i;
    }

    //Buttons
    private Button mNextButton, mHintButton;              //Buttons to submit your answer and get a hint
    private TextView mQuestionTextView, mScore;           //Textviews for the question and the score
    private int mCategory, mScoreValue = 0, mCurIndex = 0;//The current category, the score, and index of the current question
    private Question[] mQuestions;                        //Array of question objects
    private String[] mAnswerTexts, mQuestionTexts;        //Stores the text of the answers for a single question and the text of that question
    private String[][] mAllCategoryAnswers;               //All the answers texts for all questions in a category
    private ArrayList<Answer> mAnswers;                   //All the answer objects for a single question
    private boolean mUsedHint = false;                    //Whether or not you used a hint this round
    private MediaPlayer mMediaPlayer;                     //To play the sounds
    private boolean mWasCorrect = false;                  //Whether or not they answered correctly
    private int mImageAnswerIndex = -1;                   //The index of the question that has image answers (will stay at -1 if no images)
    private TypedArray mImageAnswers;                     //To get the image IDs from the xml file. IDK apparently this is how it's done.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        mCategory = getIntent().getIntExtra(CATEGORY, 1);

        mMediaPlayer = new MediaPlayer();

        //Get all the answer texts for a category. Sorry this is so gross
        switch (mCategory)
        {
            case 1:
                mQuestionTexts = getResources().getStringArray(R.array.category1_questions);
                mAllCategoryAnswers = new String[][]{
                        getResources().getStringArray(R.array.category1_question1_answers),
                        getResources().getStringArray(R.array.category1_question2_answers),
                        getResources().getStringArray(R.array.category1_question3_answers),
                        getResources().getStringArray(R.array.category1_question4_answers),
                        getResources().getStringArray(R.array.category1_question5_answers),
                        getResources().getStringArray(R.array.category1_question6_answers),
                        getResources().getStringArray(R.array.category1_question7_answers),
                        getResources().getStringArray(R.array.category1_question8_answers),
                        getResources().getStringArray(R.array.category1_question9_answers),
                        getResources().getStringArray(R.array.category1_question10_answers),
                };
                break;
            case 2:
                mQuestionTexts = getResources().getStringArray(R.array.category2_questions);
                mAllCategoryAnswers = new String[][]{
                        getResources().getStringArray(R.array.category2_question1_answers),
                        getResources().getStringArray(R.array.category2_question2_answers),
                        getResources().getStringArray(R.array.category2_question3_answers),
                        getResources().getStringArray(R.array.category2_question4_answers),
                        getResources().getStringArray(R.array.category2_question5_answers),
                        getResources().getStringArray(R.array.category2_question6_answers),
                        getResources().getStringArray(R.array.category2_question7_answers),
                        getResources().getStringArray(R.array.category2_question8_answers),
                        getResources().getStringArray(R.array.category2_question9_answers),
                        getResources().getStringArray(R.array.category2_question10_answers),
                };
                break;
            case 3:
                mQuestionTexts = getResources().getStringArray(R.array.category3_questions);
                mImageAnswerIndex = getResources().getInteger(R.integer.category3_imageIndex);
                mImageAnswers = getResources().obtainTypedArray(R.array.category3_image_answers);
                mAllCategoryAnswers = new String[][]{
                        getResources().getStringArray(R.array.category3_question1_answers),
                        getResources().getStringArray(R.array.category3_question2_answers),
                        getResources().getStringArray(R.array.category3_question3_answers),
                        getResources().getStringArray(R.array.category3_question4_answers),
                        getResources().getStringArray(R.array.category3_question5_answers),
                        getResources().getStringArray(R.array.category3_question6_answers),
                        getResources().getStringArray(R.array.category3_question7_answers),
                        getResources().getStringArray(R.array.category3_question8_answers),
                        getResources().getStringArray(R.array.category3_question9_answers),
                        getResources().getStringArray(R.array.category3_question10_answers),
                };
                break;
        }

        mAnswers = new ArrayList<Answer>();
        mQuestions = new Question[10];//there will always be 10 questions no matter what

        //Make Answers
        updateAnswerTexts();

        //Put all the questions together for 1 whole category
        for (int i = 0; i < mQuestionTexts.length; i++)
        {
            mQuestions[i] = new Question(mQuestionTexts[i], mAnswers);
        }

        //Set the question text view and make the 1st question show up
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setText(mQuestions[mCurIndex].getQuestionText());

        mScore = (TextView) findViewById(R.id.score_value); // set the original score value

        addRadioButtons();  //add the radio buttons for the answers to the screen

        //Next Button
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mCurIndex == (mQuestions.length-1))//last question
                {
                    boolean answerChecked = checkAnswer(); //make sure they actually answered the question
                    if (answerChecked)
                    {
                        if (mWasCorrect)
                        {
                            //Play correct answer sound
                            mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.correct_answer);
                            mMediaPlayer.start();
                        }
                        else
                        {
                            //Play wrong answer sound
                            mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.wrong_answer);
                            mMediaPlayer.start();
                        }
                        endGame(); //they answered the final question so end it
                    }
                    else
                    {
                        //If they haven't selected an answer, yell at them
                        int messageResId = R.string.nothing_toast;
                        toast(messageResId);
                    }
                }
                else
                {
                    boolean answerChecked = checkAnswer();
                    if (answerChecked)
                    {
                        if (mWasCorrect)
                        {
                            mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.correct_answer);
                            mMediaPlayer.start();
                        }
                        else
                        {
                            mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.wrong_answer);
                            mMediaPlayer.start();
                        }
                        //If they chose an answer, see if it's correct or not and then go to the next question
                        updateQuestion();
                        if (mUsedHint)
                        {
                            TextView hint;
                            hint = (TextView) findViewById(R.id.hint);
                            hint.setVisibility(View.GONE);
                            mHintButton.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        //If they haven't selected an answer, yell at them
                        int messageResId = R.string.nothing_toast;
                        toast(messageResId);
                    }
                }
            }
        });

        //Hint Button
        mHintButton = (Button) findViewById(R.id.hint_button);
        mHintButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView hint;
                hint = (TextView) findViewById(R.id.hint);
                hint.setVisibility(View.VISIBLE); //show the hint text
                mHintButton.setEnabled(false); //disable the hint button
                mUsedHint = true;   //says you used the hint button so the hint won't reappear

                int numAnswers = mQuestions[mCurIndex].getAnswers().size();
                int rand = mQuestions[mCurIndex].getCorrectAnswerIndex();

                //Get a random index that is not a correct answer
                while (rand == mQuestions[mCurIndex].getCorrectAnswerIndex())
                {
                    rand = new Random().nextInt(numAnswers);
                }

                mQuestions[mCurIndex].getAnswers().get(rand).getButton().setEnabled(false); //disable the button at this index

                //If the user had previously checked the answer, uncheck it
                if (mQuestions[mCurIndex].getAnswers().get(rand).getButton().isChecked())
                {
                    mQuestions[mCurIndex].getAnswers().get(rand).getButton().setChecked(false);
                }
            }
        });

    }

    /*
     * Increment the current question index, set the new question text,
     * update the score text, get the index of the correct answer for this new question,
     * and add the answers to the screen
     */
    private void updateQuestion()
    {
        mCurIndex++;
        mQuestionTextView.setText(mQuestions[mCurIndex].getQuestionText());
        mScore.setText(String.valueOf(mScoreValue));
        mQuestions[mCurIndex].setCorrectAnswerIndex(updateAnswerTexts());
        addRadioButtons();
    }

    //check answer
    private boolean checkAnswer()
    {
        int messageResId;
        boolean checked = false;
        mWasCorrect = false;

        //Make sure they actually answered the question
        for (int i = 0; i < mQuestions[mCurIndex].getAnswers().size(); i++)
        {
            //Get which radio button is selected
            if (mQuestions[mCurIndex].getAnswers().get(i).getButton().isChecked())
            {
                checked = true;

                //If the user selected the correct answer
                if (i == mQuestions[mCurIndex].getCorrectAnswerIndex())
                {
                    messageResId = R.string.correct_toast; //set the toast to the correct answer toast
                    mScoreValue += 10; //add points to score
                    mWasCorrect = true; //yea they were right
                }
                else
                {
                    messageResId = R.string.incorrect_toast; //set the toast to the wrong answer toast
                }

                toast(messageResId); //show the toast
            }
        }
        return checked;
    }

    //Do the stuff to end the game
    private void endGame()
    {
        Intent i = GameOverActivity.newIntent(QuizActivity.this, mScoreValue, mCategory);
        startActivity(i);
    }

    //Add all the radio buttons to the radio group
    private void addRadioButtons()
    {
        //Number of answers for this new question
        int numAnswers = mQuestions[mCurIndex].getAnswers().size();

        //Get the radio group and remove any previous radiobuttons from it
        ViewGroup rg = (ViewGroup) findViewById(R.id.group);
        rg.removeAllViews();

        //Go through and add all the answer buttons to the radiogroup
        for (int i = 0; i < numAnswers; i++)
        {
            mQuestions[mCurIndex].getAnswers().get(i).getButton().setSelected(false);
            rg.addView(mQuestions[mCurIndex].getAnswers().get(i).getButton());
        }
    }

    //Shows a toast based off of the String ID you pass in
    private void toast(int messageResId)
    {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    //Update the text of the answers for a question
    //returns the index of the correct answer for this question
    private int updateAnswerTexts()
    {
        int correctAnswerIndex = 0;

        //get the index of the correct answer for a question
        switch (mCategory)
        {
            case 1: correctAnswerIndex = (getResources().getIntArray(R.array.category1_answerIndices)) [mCurIndex];
                break;
            case 2: correctAnswerIndex = (getResources().getIntArray(R.array.category2_answerIndices)) [mCurIndex];
                break;
            case 3: correctAnswerIndex = (getResources().getIntArray(R.array.category3_answerIndices)) [mCurIndex];
                break;
        }

        mAnswers.clear(); //clear the old list of answers

        if (mCurIndex == mImageAnswerIndex) //If this question has image answers
        {
            for (int i = 0; i < mImageAnswers.length(); i++)
            {
                if (correctAnswerIndex == i)
                {
                    mAnswers.add(new Answer(mImageAnswers.getResourceId(i, R.drawable.henry_hudson), true, this));
                }
                else
                {
                    mAnswers.add(new Answer(mImageAnswers.getResourceId(i, R.drawable.henry_hudson), false, this));
                }
            }
        }
        else //if it doesn't
        {
            mAnswerTexts = mAllCategoryAnswers[mCurIndex]; // set the answer texts to the correct category and question

            for (int i = 0; i < mAnswerTexts.length; i++)
            {
                if (correctAnswerIndex == i)
                {
                    mAnswers.add(new Answer(mAnswerTexts[i], true, this));
                }
                else
                {
                    mAnswers.add(new Answer(mAnswerTexts[i], false, this));
                }
            }
        }

        return correctAnswerIndex;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        inflater.inflate(R.menu.high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.home: startActivity(new Intent(this, MenuActivity.class));
                return true;
            case R.id.high_scores: startActivity(new Intent(this, HighScoresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}