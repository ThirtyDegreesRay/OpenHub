/*
 *   Copyright (C) 2017 Kosh.
 *   Licensed under the GPL-3.0 license.
 *   (See the LICENSE(https://github.com/k0shk0sh/FastHub/blob/master/LICENSE) file for the whole license text.)
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
