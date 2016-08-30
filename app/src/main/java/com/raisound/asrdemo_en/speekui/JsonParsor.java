package com.raisound.asrdemo_en.speekui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Json���������
 */
public class JsonParsor {

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // תд����ʣ�Ĭ��ʹ�õ�һ�����
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				�����Ҫ���ѡ������������������ֶ�
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String parseGrammarResult(String json, String engType) {
        StringBuffer sret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            // �ƶ˺ͱ��ؽ�����������
            if ("cloud".equals(engType)) {
                for (int i = 0; i < words.length(); i++) {
                    JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject obj = items.getJSONObject(j);
                        if (obj.getString("w").contains("nomatch")) {
                            sret.append("û��ƥ����.");
                            return sret.toString();
                        }
                        sret.append("�������" + obj.getString("w"));
                        sret.append("�����Ŷȡ�" + obj.getInt("sc"));
                        sret.append("\n");
                    }
                }
            } else if ("local".equals(engType)) {
                sret.append("�������");
                for (int i = 0; i < words.length(); i++) {
                    JSONObject wsItem = words.getJSONObject(i);
                    JSONArray items = wsItem.getJSONArray("cw");
                    if ("<contact>".equals(wsItem.getString("slot"))) {
                        // ���ܻ��ж����ϵ�˹�ѡ��������������������Щ��ѡ�������ͬ�����Ŷ�
                        sret.append("��");
                        for (int j = 0; j < items.length(); j++) {
                            JSONObject obj = items.getJSONObject(j);
                            if (obj.getString("w").contains("nomatch")) {
                                sret.append("û��ƥ����.");
                                return sret.toString();
                            }
                            sret.append(obj.getString("w")).append("|");
                        }
                        sret.setCharAt(sret.length() - 1, '��');
                    } else {
                        //���ض��ѡ�������Ŷȸߵ�����һ��ѡȡ��һ���������
                        JSONObject obj = items.getJSONObject(0);
                        if (obj.getString("w").contains("nomatch")) {
                            sret.append("û��ƥ����.");
                            return sret.toString();
                        }
                        sret.append(obj.getString("w"));
                    }
                }
                sret.append("�����Ŷȡ�" + joResult.getInt("sc"));
                sret.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sret.append("û��ƥ����.");
        }
        return sret.toString();
    }

    public static String parseGrammarResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("û��ƥ����.");
                        return ret.toString();
                    }
                    ret.append("�������" + obj.getString("w"));
                    ret.append("�����Ŷȡ�" + obj.getInt("sc"));
                    ret.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append("û��ƥ����.");
        }
        return ret.toString();
    }

    @SuppressWarnings("finally")
    public static String parseUnderstandResult(String json) {
        String text_date = null;
        StringBuffer ret = new StringBuffer();
        JSONTokener tokener = new JSONTokener(json);
        try {
            JSONObject joResult = new JSONObject(tokener);
            switch (joResult.getString("operation").toString()) {
                case "CREATE":
                    switch (joResult.get("service").toString()) {
                        case "schedule":
                            JSONObject org = joResult.getJSONObject("semantic").getJSONObject("slots").getJSONObject("datetime");
                            text_date = org.getString("date");
                            break;
                    }
                    break;
//                case "OPEN":
//                    text_data = "��Ҫ����Ӧ��ҳ";
//                    break;
//                case "SEND":
//                    text_data = "���Ͷ���";
//                    break;
//                case "QUERY":
//                    switch(joResult.get("service").toString()){
//                        case "weather":
//                            JSONObject org = joResult.getJSONObject("data").getJSONObject("result");
//                            text_data = org.getString("city")+org.getString("date")+org.getString("wind")+"����"+org.getString("windLevel")+"�¶�"+org.getString("tempRange");
//                            break;
//                    }
//                    break;
//
//                case "PLAY":
//                    switch(joResult.get("service").toString()){
//                        case "music":
//                            JSONObject org = joResult.getJSONObject("data").getJSONObject("result");
//
//                            text_data = "��ʱ��û������"+org.getString("singer").toString() + "��"+org.getString("name");
//                            break;
//                    }
//                case "ANSWER":
//                    text_data = joResult.getJSONObject("answer").getString("text");
//                    break;
//                case "LAUNCH":
//                    if(joResult.get("operation").equals("app")){
//                        text_data = joResult.getJSONObject("moreResults").getJSONObject("semantic").getJSONObject("slots").getString("name");
//                        break;
//                    }
//                case "CLOSE":
//                    text_data = joResult.getJSONObject("text").toString();
//
//                    break;
//                case "PAUSE":
//                    text_data = joResult.getJSONObject("text").toString();
//                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return text_date;
        }
    }

    public static String parseUnderstandResult2(String json) {
        String text_time = null;
        StringBuffer ret = new StringBuffer();
        JSONTokener tokener = new JSONTokener(json);
        try {
            JSONObject joResult = new JSONObject(tokener);
            switch (joResult.getString("operation").toString()) {
                case "CREATE":
                    switch (joResult.get("service").toString()) {
                        case "schedule":
                            JSONObject org = joResult.getJSONObject("semantic").getJSONObject("slots").getJSONObject("datetime");
                            text_time = org.getString("time");
                            break;
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return text_time;
        }
    }
}

