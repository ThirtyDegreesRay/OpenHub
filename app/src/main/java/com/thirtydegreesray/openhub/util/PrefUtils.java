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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppApplication;

import java.util.Set;

/**
 * Created by ThirtyDegreesRay on 2017/10/30 11:59:38
 */

public class PrefUtils {

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

    public static SharedPreferences getDefaultSp(){
        return PreferenceManager.getDefaultSharedPreferences(AppApplication.get());
    }

    public static void set(@NonNull String key, @NonNull Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            throw new NullPointerException(String.format("Key and value not be null key=%s, value=%s", key, value));
        }
        SharedPreferences.Editor edit = getDefaultSp().edit();
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
        } else if(value instanceof Set && ((Set) value).toArray() instanceof String[]){
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        edit.apply();
    }

    public static void clearKey(@NonNull String key) {
        getDefaultSp(AppApplication.get()).edit().remove(key).apply();
    }

    public static int getTheme(){
        return getDefaultSp(AppApplication.get()).getInt(THEME, 0);
    }

    public static String getLanguage(){
        return getDefaultSp(AppApplication.get()).getString(LANGUAGE, "en");
    }

    public static int getAccentColor(){
        return getDefaultSp(AppApplication.get()).getInt(ACCENT_COLOR, 11);
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
