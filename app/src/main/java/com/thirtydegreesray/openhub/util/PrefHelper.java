/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.AppApplication;

/**
 * Created on 2017/8/3.
 *
 * @author ThirtyDegreesRay
 */

public class PrefHelper {

    public final static int LIGHT = 0;
    public final static int DARK = 1;

    public final static int LIGHT_BLUE = 0;
    public final static int BLUE = 1;
    public final static int INDIGO = 2;
    public final static int ORANGE = 3;

    public final static int YELLOW = 4;
    public final static int AMBER = 5;
    public final static int GREY = 6;
    public final static int BROWN = 7;

    public final static int CYAN = 8;
    public final static int TEAL = 9;
    public final static int LIME = 10;
    public final static int GREEN = 11;

    public final static int PINK = 12;
    public final static int RED = 13;
    public final static int PURPLE = 14;
    public final static int DEEP_PURPLE = 15;


    public final static String THEME = "theme";
    public final static String ACCENT_COLOR = "accentColor";
    public final static String CACHE_FIRST_ENABLE = "cacheFirstEnable";
    public final static String LANGUAGE = "language";
    public final static String LOGOUT = "logout";
    public final static String CODE_WRAP = "codeWrap";

    public final static String POP_TIMES = "popTimes";
    public final static String POP_VERSION_TIME = "popVersionTime";
    public final static String LAST_POP_TIME = "lastPopTime";

    @SuppressLint("ApplySharedPref") public static <T> void set(@NonNull String key, @Nullable T value) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("Key must not be null! (key = " + key + "), (value = " + value + ")");
        }
        SharedPreferences.Editor edit
                = PreferenceManager.getDefaultSharedPreferences(AppApplication.get()).edit();
//        if (StringUtils.isBlank(key)) {
//            clearKey(key);
//            return;
//        }
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else {
            edit.putString(key, value.toString());
        }
//        edit.commit();//apply on UI
        edit.apply();
    }

    public static void clearKey(@NonNull String key) {
        getDefaultSp(AppApplication.get()).edit().remove(key).apply();
    }

    public static int getTheme(){
        int theme = getDefaultSp(AppApplication.get()).getInt(THEME, 0);
        return theme;
    }

    public static String getLanguage(){
        String language = getDefaultSp(AppApplication.get()).getString(LANGUAGE, "en");
        return language;
    }

    public static int getAccentColor(){
        int accentColor = getDefaultSp(AppApplication.get()).getInt(ACCENT_COLOR, 11);
        return accentColor;
    }

    public static boolean isCacheFirstEnable(){
        return getDefaultSp(AppApplication.get()).getBoolean(CACHE_FIRST_ENABLE, true);
    }

    public static boolean isCodeWrap(){
        return getDefaultSp(AppApplication.get()).getBoolean(CODE_WRAP, false);
    }

    public static int getPopTimes(){
        return getDefaultSp(AppApplication.get()).getInt(POP_TIMES, 0);
    }

    public static long getPopVersionTime(){
        return getDefaultSp(AppApplication.get()).getLong(POP_VERSION_TIME, 1);
    }

    public static long getLastPopTime(){
        return getDefaultSp(AppApplication.get()).getLong(LAST_POP_TIME, 0);
    }

    public static SharedPreferences getDefaultSp(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getSP(Context context, String spName){
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

}
