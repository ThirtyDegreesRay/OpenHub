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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * StringUtil
 * Created by ThirtyDegreesRay on 2016/7/14 16:18
 */
public class StringUtils {

    public static boolean isBlank(@Nullable String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean isBlankList(@Nullable List list) {
        return list == null || list.size() == 0;
    }

    public static List<String> stringToList(@NonNull String str, @NonNull String separator){
        List<String> list = null;
        if(!str.contains(separator)){
            return list;
        }
        String[] strs = str.split(separator);
        list = Arrays.asList(strs);
        return list;
    }

    public static String listToString(@NonNull List<String> list, @NonNull String separator){
        StringBuilder stringBuilder = new StringBuilder("");
        if(list.size() == 0 || isBlank(separator)){
            return stringBuilder.toString();
        }
        for(int i = 0; i < list.size(); i++){
            stringBuilder.append(list.get(i));
            if(i != list.size() - 1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    public static String getSizeString(long size){
        if(size < 1024){
            return String.format(Locale.getDefault(), "%d B", size);
        }else if(size < 1024 * 1024){
            float sizeK = size / 1024f;
            return String.format(Locale.getDefault(), "%.2f KB", sizeK);
        }else if(size < 1024 * 1024 * 1024){
            float sizeM = size / (1024f * 1024f);
            return String.format(Locale.getDefault(), "%.2f MB", sizeM);
        }
        return null;
    }

    public static String getDateStr(@NonNull Date date){
        Locale locale = AppHelper.getLocale(PrefHelper.getLanguage());
        String regex ;
        regex = "yyyy-MM-dd";
//        if(locale.equals(Locale.CHINA)){
//            regex = "yyyy-MM-dd";
//        }else{
//            regex = "dd-MM-yyyy";
//        }
        SimpleDateFormat format = new SimpleDateFormat(regex, locale);
        return format.format(date);
    }

    public static String getNewsTimeStr(@NonNull Context context, @NonNull Date date){
        long subTime = System.currentTimeMillis() - date.getTime();
        final long SECONDS_LIMIT = 60 * 1000;
        final long MINUTES_LIMIT = 60 * SECONDS_LIMIT;
        final long HOURS_LIMIT = 24 * MINUTES_LIMIT;
        final long DAYS_LIMIT = 30 * HOURS_LIMIT;
        if(subTime < 1000){
            return context.getString(R.string.just_now);
        } else if(subTime < SECONDS_LIMIT){
            return subTime / 1000 + " " + context.getString(R.string.seconds_ago);
        } else if(subTime < MINUTES_LIMIT){
            return subTime / SECONDS_LIMIT + " " + context.getString(R.string.minutes_ago);
        } else if(subTime < HOURS_LIMIT){
            return subTime / MINUTES_LIMIT + " " + context.getString(R.string.hours_ago);
        } else if(subTime < DAYS_LIMIT){
            return subTime / HOURS_LIMIT + " " + context.getString(R.string.days_ago);
        } else
            return getDateStr(date);
    }

    public static String upCaseFisrtChar(String str){
        if(isBlank(str)) return null;
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }

}
