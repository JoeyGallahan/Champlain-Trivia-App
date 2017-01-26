package com.example.joey.champlainquiz;

/*
 * HighScoresActivity loads and displays all of the saved highscores for each category
 * Created by Joey Gallahan 10/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity
{
    private HighScore mCat1HighScores, mCat2HighScores, mCat3HighScores;
    private TextView mCat1Show, mCat2Show, mCat3Show;

    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, HighScoresActivity.class);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mCat1HighScores = new HighScore(this, R.id.cat1_scores_here, getResources().getString(R.string.category1_scores));
        mCat1HighScores.parseScores(mCat1HighScores.loadScores());

        mCat2HighScores = new HighScore(this, R.id.cat2_scores_here, getResources().getString(R.string.category2_scores));
        mCat2HighScores.parseScores(mCat2HighScores.loadScores());

        mCat3HighScores = new HighScore(this, R.id.cat3_scores_here, getResources().getString(R.string.category3_scores));
        mCat3HighScores.parseScores(mCat3HighScores.loadScores());

        mCat1Show = (TextView) findViewById(R.id.cat1_showScores);
        mCat2Show = (TextView) findViewById(R.id.cat2_showScores);
        mCat3Show = (TextView) findViewById(R.id.cat3_showScores);

        for (int i = 0; i < mCat1HighScores.getNumScores(); i++)
        {
            mCat1HighScores.addScoreToScreen(mCat1HighScores.getInitial(i), mCat1HighScores.getScore(i));
        }
        for (int i = 0; i < mCat2HighScores.getNumScores(); i++)
        {
            mCat2HighScores.addScoreToScreen(mCat2HighScores.getInitial(i), mCat2HighScores.getScore(i));
        }
        for (int i = 0; i < mCat3HighScores.getNumScores(); i++)
        {
            mCat3HighScores.addScoreToScreen(mCat3HighScores.getInitial(i), mCat3HighScores.getScore(i));
        }

        mCat1Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ScrollView sv = (ScrollView) findViewById(R.id.cat1_scores);
                if (sv.getVisibility() == View.VISIBLE)
                {
                    sv.setVisibility(View.GONE);
                    mCat1Show.setText(R.string.show_scores);
                }
                else
                {
                    sv.setVisibility(View.VISIBLE);
                    mCat1Show.setText(R.string.hide_scores);
                }
            }
        });
        mCat2Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView sv = (ScrollView) findViewById(R.id.cat2_scores);
                if (sv.getVisibility() == View.VISIBLE)
                {
                    sv.setVisibility(View.GONE);
                    mCat2Show.setText(R.string.show_scores);
                }
                else
                {
                    sv.setVisibility(View.VISIBLE);
                    mCat2Show.setText(R.string.hide_scores);
                }
            }
        });

        mCat3Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView sv = (ScrollView) findViewById(R.id.cat3_scores);
                if (sv.getVisibility() == View.VISIBLE)
                {
                    sv.setVisibility(View.GONE);
                    mCat3Show.setText(R.string.show_scores);
                }
                else
                {
                    sv.setVisibility(View.VISIBLE);
                    mCat3Show.setText(R.string.hide_scores);
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
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
