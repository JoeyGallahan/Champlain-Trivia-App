package com.example.joey.champlainquiz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/*
 * Answer stores all the necessary information for an answer
 * Created by Joey Gallahan 09/2016
 * I certify that all this work is mine unless stated otherwise in the comments
 */

public class Answer
{
    private boolean mIsCorrect;
    private String mText;
    RadioButton mButton;
    private int mImageSrc;

    //Text Answer
    Answer(String text, boolean isCorrect, Context activity)
    {
        mText = text;
        mIsCorrect = isCorrect;
        mImageSrc = 0;

        //Create the text button and set params
        mButton = new RadioButton(activity);
        mButton.setText(text);
        mButton.setTextColor(activity.getResources().getColor(R.color.white));
        mButton.setChecked(false);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mButton.setChecked(true);
            }
        });
    }

    //Image Answer
    Answer(int imgSrc, boolean isCorrect, Context activity)
    {
        mIsCorrect = isCorrect;
        mImageSrc = imgSrc;

        //Create the image button and set params
        mButton = new RadioButton(activity);
        mButton.setBackgroundResource(imgSrc);
        mButton.setButtonDrawable(R.color.transparent);
        mButton.setChecked(false);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mButton.setChecked(true);
                mButton.setScaleX(1.2f);
                mButton.setScaleY(1.2f);
            }
        });
    }

    public Boolean getIsCorrect(){return mIsCorrect;}
    public RadioButton getButton(){return mButton;}
}