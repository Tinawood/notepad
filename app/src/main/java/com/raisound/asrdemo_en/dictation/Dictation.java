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
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        final SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(Dictation.this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
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
        // 读取json结果中的sn字段
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
        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("Result:", results.getResultString());
            printResult(results);
            if (isLast) {
                // TODO 最后的结果
            }
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
//            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
        }

        //开始录音
            public void onBeginOfSpeech(){
            }
            //音量值0~30

        //结束录音
        public void onEndOfSpeech() {
        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };



}

