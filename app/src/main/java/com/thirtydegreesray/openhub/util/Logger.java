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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thirtydegreesray.openhub.BuildConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2017/8/9.
 *
 * @author ThirtyDegreesRay
 */

public class Logger {

    private final static String TAG = Logger.class.getSimpleName();

    private final static boolean DEBUG = BuildConfig.DEBUG;

    public static void d(@NonNull String tag, @Nullable Object msg){
        if(!DEBUG) return;
        Log.d(tag, msg != null ? msg.toString() : "LOG MSG IS NULL!");
    }

    public static void i(@NonNull String tag, @Nullable Object msg){
        if(!DEBUG) return;
        Log.i(tag, msg != null ? msg.toString() : "LOG MSG IS NULL!");
    }

    public static void w(@NonNull String tag, @Nullable Object msg){
        if(!DEBUG) return;
        Log.w(tag, msg != null ? msg.toString() : "LOG MSG IS NULL!");
    }

    public static void e(@NonNull String tag, @Nullable Object msg){
        if(!DEBUG) return;
        Log.e(tag, msg != null ? msg.toString() : "LOG MSG IS NULL!");
    }

    public static void d(@Nullable Object text) {
        d(getCurrentClassName() + " || " + getCurrentMethodName(), text);//avoid null
    }

    public static void i(@Nullable Object text) {
        i(getCurrentClassName() + " || " + getCurrentMethodName(), text);//avoid null
    }

    public static void e(Object... objects) {
        if (objects != null && objects.length > 0) {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), Arrays.toString(objects));
        } else {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), getCurrentMethodName());
        }
    }

    public static void e(List<Object> objects) {
        if (objects != null) {
            e(getCurrentClassName() + " || " + getCurrentMethodName(), Arrays.toString(objects.toArray()));
        } else {
            e(TAG, null);
        }
    }

    private static String getCurrentMethodName() {
        try {
            return Thread.currentThread().getStackTrace()[4].getMethodName() + "()";
        } catch (Exception ignored) {}
        return TAG;
    }

    private static String getCurrentClassName() {
        try {
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            String[] temp = className.split("[.]");
            className = temp[temp.length - 1];
            return className;
        } catch (Exception ignored) {}
        return TAG;
    }

}
