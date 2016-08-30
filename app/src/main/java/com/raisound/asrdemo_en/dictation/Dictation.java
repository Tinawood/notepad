package com.raisound.asrdemo_en.dictation;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.raisound.asrdemo_en.R;
import com.raisound.asrdemo_en.speekui.JsonParsor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Wz on 2016/8/16.
 */
public class Dictation extends Activity {

    private  EditText etx;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
//    EditText etx = (EditText)findViewById(R.id.eText);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.di_ctation);
        Button vbt = (Button) findViewById(R.id.vbutton);
        SpeechUtility.createUtility(Dictation.this, SpeechConstant.APPID + "=57abdf67");
        //1.����SpeechRecognizer���󣬵ڶ���������������дʱ��InitListener
        final SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(Dictation.this, null);
        //2.������д������������ƴ�Ѷ��MSC API�ֲ�(Android)��SpeechConstant��
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        mIat.setParameter(SpeechConstant.ASR_PTT, "true");

        vbt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIatResults.clear();
                        mIat.startListening(mRecoListener);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mIat.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        mIat.stopListening();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }


    private void printResult(RecognizerResult results) {
        etx = (EditText)findViewById(R.id.eText);
        String text = JsonParsor.parseIatResult(results.getResultString());
        String sn = null;
        // ��ȡjson����е�sn�ֶ�
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etx.setText(resultBuffer.toString());
        etx.setSelection(etx.length());
    }



    private RecognizerListener mRecoListener = new RecognizerListener() {
        //��д����ص��ӿ�(����Json��ʽ������û��ɲμ���¼12.1)��
        //һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
        //���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
        //isLast����trueʱ�Ự������
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("Result:", results.getResultString());
            printResult(results);
            if (isLast) {
                // TODO ���Ľ��
            }
        }

        //�Ự��������ص��ӿ�
        public void onError(SpeechError error) {
//            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
        }

        //��ʼ¼��
            public void onBeginOfSpeech(){
            }
            //����ֵ0~30

        //����¼��
        public void onEndOfSpeech() {
        }

        //��չ�ýӿ�
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };



}

