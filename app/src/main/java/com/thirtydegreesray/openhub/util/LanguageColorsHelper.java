package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ThirtyDegreesRay on 2018/1/6 11:40:44
 */

public enum  LanguageColorsHelper {
    INSTANCE;

    private Map<String, Integer> colorMap;
    private int defaultColor;

    public int getColor(@NonNull Context context, @NonNull String languageName){
        if(colorMap == null){
            initColorMap(context);
            defaultColor = Color.parseColor("#CCCCCC");
        }
        return colorMap.containsKey(languageName) ? colorMap.get(languageName) : defaultColor;
    }

    private void initColorMap(@NonNull Context context){
        colorMap = new HashMap<>();
        try {
            InputStream inputStream = context.getAssets().open("language_colors.json");
            String content = convertStreamToString(inputStream);
            JSONObject jsonObject = new JSONObject(content);
            Iterator<String> iterator = jsonObject.keys();
            for(; iterator.hasNext(); ){
                String name = iterator.next();
                JSONObject language = jsonObject.getJSONObject(name);
                String colorStr = language.getString("color");
                if(colorStr == null || colorStr.equals("null")){
                    continue;
                }
                int color = Color.parseColor(colorStr);
                colorMap.put(name, color);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
