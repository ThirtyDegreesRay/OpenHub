/*
 *    Copyright 2017 ThirtyDegressRay
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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
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

    public static String getSizeString(int size){
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

}
