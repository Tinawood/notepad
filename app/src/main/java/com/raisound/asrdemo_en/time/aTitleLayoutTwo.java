package com.raisound.asrdemo_en.time;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.raisound.asrdemo_en.R;

/**
 * Created by Wz on 2016/8/1.
 */
public class aTitleLayoutTwo extends LinearLayout {

    public aTitleLayoutTwo(final Context context, AttributeSet attrs) {

        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.atitlestyletwo, this);

    }
}

