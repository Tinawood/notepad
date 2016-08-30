package com.raisound.asrdemo_en.speekui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Json结果解析类
 */
public class JsonParsor {

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
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
            // 云端和本地结果分情况解析
            if ("cloud".equals(engType)) {
                for (int i = 0; i < words.length(); i++) {
                    JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject obj = items.getJSONObject(j);
                        if (obj.getString("w").contains("nomatch")) {
                            sret.append("没有匹配结果.");
                            return sret.toString();
                        }
                        sret.append("【结果】" + obj.getString("w"));
                        sret.append("【置信度】" + obj.getInt("sc"));
                        sret.append("\n");
                    }
                }
            } else if ("local".equals(engType)) {
                sret.append("【结果】");
                for (int i = 0; i < words.length(); i++) {
                    JSONObject wsItem = words.getJSONObject(i);
                    JSONArray items = wsItem.getJSONArray("cw");
                    if ("<contact>".equals(wsItem.getString("slot"))) {
                        // 可能会有多个联系人供选择，用中括号括起来，这些候选项具有相同的置信度
                        sret.append("【");
                        for (int j = 0; j < items.length(); j++) {
                            JSONObject obj = items.getJSONObject(j);
                            if (obj.getString("w").contains("nomatch")) {
                                sret.append("没有匹配结果.");
                                return sret.toString();
                            }
                            sret.append(obj.getString("w")).append("|");
                        }
                        sret.setCharAt(sret.length() - 1, '】');
                    } else {
                        //本地多候选按照置信度高低排序，一般选取第一个结果即可
                        JSONObject obj = items.getJSONObject(0);
                        if (obj.getString("w").contains("nomatch")) {
                            sret.append("没有匹配结果.");
                            return sret.toString();
                        }
                        sret.append(obj.getString("w"));
                    }
                }
                sret.append("【置信度】" + joResult.getInt("sc"));
                sret.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sret.append("没有匹配结果.");
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
                        ret.append("没有匹配结果.");
                        return ret.toString();
                    }
                    ret.append("【结果】" + obj.getString("w"));
                    ret.append("【置信度】" + obj.getInt("sc"));
                    ret.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.append("没有匹配结果.");
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
//                    text_data = "需要查找应网页";
//                    break;
//                case "SEND":
//                    text_data = "发送短信";
//                    break;
//                case "QUERY":
//                    switch(joResult.get("service").toString()){
//                        case "weather":
//                            JSONObject org = joResult.getJSONObject("data").getJSONObject("result");
//                            text_data = org.getString("city")+org.getString("date")+org.getString("wind")+"风速"+org.getString("windLevel")+"温度"+org.getString("tempRange");
//                            break;
//                    }
//                    break;
//
//                case "PLAY":
//                    switch(joResult.get("service").toString()){
//                        case "music":
//                            JSONObject org = joResult.getJSONObject("data").getJSONObject("result");
//
//                            text_data = "暂时还没有设置"+org.getString("singer").toString() + "的"+org.getString("name");
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

