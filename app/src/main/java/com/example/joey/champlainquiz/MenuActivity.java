package com.example.joey.champlainquiz;

/*
 * Menu Activity shows the buttons for each different category and will start the quiz for the chosen category
 * It is considered the Home Page for the app
 * Created by Joey Gallahan 09/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity
{
    private Button mCategoryButton1, mCategoryButton2, mCategoryButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mCategoryButton1 = (Button) findViewById(R.id.category1);
        mCategoryButton2 = (Button) findViewById(R.id.category2);
        mCategoryButton3 = (Button) findViewById(R.id.category3);

        mCategoryButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = QuizActivity.newIntent(MenuActivity.this, 1);
                startActivity(i);
            }
        });

        mCategoryButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = QuizActivity.newIntent(MenuActivity.this, 2);
                startActivity(i);
            }
        });

        mCategoryButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = QuizActivity.newIntent(MenuActivity.this, 3);
                startActivity(i);
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
            case R.id.high_scores: startActivity(new Intent(this, HighScoresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}