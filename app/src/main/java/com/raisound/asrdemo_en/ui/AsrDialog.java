package com.raisound.asrdemo_en.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.raisound.asrdemo_en.R;


/**
 * Created by maolin on 2016/4/11.
 */
public class AsrDialog extends Dialog {
    private ImageView mMicIv;
    private TextView mTipTv;

    public AsrDialog(Context context) {
        super(context, R.style.CustomAsrDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asr_dialog);
        //getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); //需要添加的语句
        mMicIv = (ImageView) findViewById(R.id.asr_mic_iv);
        mTipTv = (TextView) findViewById(R.id.asr_tip_tv);
    }

    public void setTipText(String msg){
        mTipTv.setText(msg);
    }

    public void setEnergy(int id){
        if (mMicIv != null){
            mMicIv.setImageResource(id);
        }
    }

    public void setError(){
        if (mMicIv != null){
            mMicIv.setBackgroundResource(R.mipmap.no_result);
        }
    }
}
