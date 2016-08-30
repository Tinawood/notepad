package com.raisound.asrdemo_en.tings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.raisound.asrdemo_en.R;
import com.raisound.asrdemo_en.camera.MainCamera;
import com.raisound.asrdemo_en.dictation.Dictation;
import com.raisound.asrdemo_en.speekui.MaActivity;
import com.raisound.asrdemo_en.time.MainActivity;

/**
 * Created by Wz on 2016/8/10.
 */
public class Mactivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mactivity);

        Button Bq = (Button) findViewById(R.id.btn_Bq);
        Bq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mactivity.this, TMainActivity.class);
                startActivity(intent);
            }
        });

        Button Bw = (Button) findViewById(R.id.btn_Bw);
        Bw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mactivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button Tx = (Button) findViewById(R.id.btn_Tx);
        Tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mactivity.this, MainCamera.class);
                startActivity(intent);
            }
        });

    }
}
