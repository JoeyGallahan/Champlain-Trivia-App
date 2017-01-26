package com.example.joey.champlainquiz;

/*
 * GameOverActivity is where the player is sent after they answer all 10 questions.
 * It displays their final score and previous high scores, and allows them to add their score to the list
 * Created by Joey Gallahan 10/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity
{

    private static final String FINAL_SCORE = "FINAL_SCORE";
    private static final String CATEGORY    = "CATEGORY";

    private int mFinalScore;                //The user's score this round
    private EditText mTextBox;              //Where they type their initials
    private Button mSubmitScore;            //Submit button
    private LinearLayout mHighScoreBox;     //Where the highscores are displayed
    private String mFileName;               //Where the scores load/save to
    private int mCategory;
    private HighScore mHighScore;

    public static Intent newIntent(Context packageContext, int score, int initials)
    {
        Intent i = new Intent(packageContext, GameOverActivity.class);
        i.putExtra(FINAL_SCORE, score);
        i.putExtra(CATEGORY, initials);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mFinalScore = getIntent().getIntExtra(FINAL_SCORE, 0); //set final score
        mCategory = getIntent().getIntExtra(CATEGORY, 1);      //set category

        //Show the final score at the top of the page
        TextView view = (TextView) findViewById(R.id.final_score_value);
        view.setText(String.valueOf(mFinalScore));

        //Get the edittext box and the place to put all the highscores
        mTextBox = (EditText) findViewById(R.id.initials);
        mHighScoreBox = (LinearLayout) findViewById(R.id.add_scores_here);

        switch(mCategory)
        {
            case 1: mFileName = getResources().getString(R.string.category1_scores);
                break;
            case 2: mFileName = getResources().getString(R.string.category2_scores);
                break;
            case 3: mFileName = getResources().getString(R.string.category3_scores);
                break;
        }

        mHighScore = new HighScore(this, mHighScoreBox.getId(), mFileName);

        mHighScore.parseScores(mHighScore.loadScores()); //Load in previous highscores and put the initials and scores into the appropriate places

        //Add all the previous highscores to the screen
        if (mHighScore.getNumScores() > 0)
        {
            for (int i = 0; i < mHighScore.getNumScores(); i++)
            {
                mHighScore.addScoreToScreen(mHighScore.getInitial(i), mHighScore.getScore(i));
                Log.d("idk", mHighScore.getInitial(i) + mHighScore.getScore(i));
            }
        }

        //Submit your highscore
        mSubmitScore = (Button) findViewById(R.id.submit_score);
        mSubmitScore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //If saving your score worked, alert the user and then don't let them submit it again
                if (mHighScore.saveScore(mTextBox.getText().toString(), mFinalScore))
                {
                    Toast.makeText(getBaseContext(), R.string.score_saved, Toast.LENGTH_SHORT).show();
                    mHighScore.addScoreToScreen(mTextBox.getText().toString(), mFinalScore);
                    mSubmitScore.setEnabled(false);
                    mHighScore.sortScores();
                }
            }
        });
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