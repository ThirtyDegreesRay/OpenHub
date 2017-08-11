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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.R;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created on 2017/8/3.
 *
 * @author ThirtyDegreesRay
 */

public class AppHelper {

    public static void updateAppLanguage(@NonNull Context context) {
        String lang = PrefHelper.getLanguage();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, lang);
        }
        updateResourcesLegacy(context, lang);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static void updateResourcesLegacy(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @NonNull private static Locale getLocale(String language) {
        Locale locale = null;
        if (language.equalsIgnoreCase("zh-rCN")) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equalsIgnoreCase("zh-rTW")) {
            locale = Locale.TRADITIONAL_CHINESE;
        }
        if (locale != null) return locale;
        String[] split = language.split("-");
        if (split.length > 1) {
            locale = new Locale(split[0], split[1]);
        } else {
            locale = new Locale(language);
        }
        return locale;
    }

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), uri);
        clipboard.setPrimaryClip(clip);
        Toasty.success(AppApplication.get(), context.getString(R.string.success_copied)).show();
    }

    public static boolean isNightMode(){
        int theme = PrefHelper.getTheme();
        return theme == PrefHelper.DARK ;
    }

}
