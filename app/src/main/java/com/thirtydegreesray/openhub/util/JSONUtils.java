package com.thirtydegreesray.openhub.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 13:55:10
 */

public class JSONUtils {

    public static <T> ArrayList<T> jsonToArrayList(String jsonArrayStr, Class<T> tClass){
        Gson gson = new Gson();
        ArrayList<T> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayStr);
            for( int i = 0; i < jsonArray.length(); i++){
                arrayList.add(gson.fromJson(jsonArray.get(i).toString(), tClass));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
