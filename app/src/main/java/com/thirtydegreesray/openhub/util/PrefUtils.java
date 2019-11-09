

package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppApplication;

import java.util.Set;

/**
 * Created on 2017/10/30 11:59:38
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

public class PrefUtils {

    public final static String LIGHT_INDIGO = "Light indigo";
    public final static String DARK = "Dark";
    public final static String LIGHT_TEAL = "Light teal";
    public final static String AMOLED_DARK = "AMOLED dark";

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

    public final static String FIRST_USE = "firstUse";

    public final static String THEME = "appTheme";
    public final static String ACCENT_COLOR = "accentColor";
    public final static String START_PAGE = "startPage";
    public final static String CACHE_FIRST_ENABLE = "cacheFirstEnable";
    public final static String SYSTEM_DOWNLOADER = "systemDownloader";
    public final static String LANGUAGE = "language";
    public final static String LOGOUT = "logout";
    public final static String CODE_WRAP = "codeWrap";
    public final static String CUSTOM_TABS_ENABLE = "customTabsEnable";


    public final static String POP_TIMES = "popTimes";
    public final static String POP_VERSION_TIME = "popVersionTime";
    public final static String LAST_POP_TIME = "lastPopTime";

    public final static String NEW_YEAR_WISHES_TIP_ENABLE = "newYearWishesTipEnable";
    public final static String STAR_WISHES_TIP_TIMES = "starWishesTipFlag";
    public final static String LAST_STAR_WISHES_TIP_TIME = "lastStarWishesTipTime";

    public final static String DOUBLE_CLICK_TITLE_TIP_ABLE = "doubleClickTitleTipAble";
    public final static String ACTIVITY_LONG_CLICK_TIP_ABLE = "activityLongClickTipAble";
    public final static String RELEASES_LONG_CLICK_TIP_ABLE = "releasesLongClickTipAble";
    public final static String LANGUAGES_EDITOR_TIP_ABLE = "languagesEditorTipAble";
    public final static String COLLECTIONS_TIP_ABLE = "collectionsTipAble";
    public final static String BOOKMARKS_TIP_ABLE = "bookmarksTipAble";
    public final static String CUSTOM_TABS_TIPS_ENABLE = "customTabsTipsEnable";
    public final static String TOPICS_TIP_ABLE = "topicsTipAble";
    public final static String DISABLE_LOADING_IMAGE = "disableLoadingImage";


    public final static String SEARCH_RECORDS = "searchRecords";

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
        } else if(value instanceof Set){
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        edit.apply();
    }

    public static void clearKey(@NonNull String key) {
        getDefaultSp(AppApplication.get()).edit().remove(key).apply();
    }

    public static String getTheme(){
        return getDefaultSp(AppApplication.get()).getString(THEME, LIGHT_TEAL);
    }

    public static String getLanguage(){
        return getDefaultSp(AppApplication.get()).getString(LANGUAGE, "en");
    }

    public static String getStartPage(){
        return getDefaultSp(AppApplication.get()).getString(START_PAGE, "news");
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

    public static boolean isDoubleClickTitleTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(DOUBLE_CLICK_TITLE_TIP_ABLE, true);
    }

    public static boolean isActivityLongClickTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(ACTIVITY_LONG_CLICK_TIP_ABLE, true);
    }

    public static boolean isReleasesLongClickTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(RELEASES_LONG_CLICK_TIP_ABLE, true);
    }

    public static boolean isLanguagesEditorTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(LANGUAGES_EDITOR_TIP_ABLE, true);
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

    public static int getStarWishesTipTimes(){
        return getDefaultSp(AppApplication.get()).getInt(STAR_WISHES_TIP_TIMES, 0);
    }

    public static long getLastStarWishesTipTime(){
        return getDefaultSp(AppApplication.get()).getLong(LAST_STAR_WISHES_TIP_TIME, 0);
    }

    public static boolean isSystemDownloader(){
        return getDefaultSp(AppApplication.get()).getBoolean(SYSTEM_DOWNLOADER, true);
    }

    public static boolean isCustomTabsEnable(){
        return getDefaultSp(AppApplication.get()).getBoolean(CUSTOM_TABS_ENABLE, true);
    }

    public static String getSearchRecords(){
        return getDefaultSp(AppApplication.get()).getString(SEARCH_RECORDS, null);
    }

    public static boolean isFirstUse(){
        return getDefaultSp(AppApplication.get()).getBoolean(FIRST_USE, true);
    }

    public static boolean isCollectionsTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(COLLECTIONS_TIP_ABLE, true);
    }

    public static boolean isBookmarksTipAble(){
        return getDefaultSp(AppApplication.get()).getBoolean(BOOKMARKS_TIP_ABLE, true);
    }

    public static boolean isCustomTabsTipsEnable(){
        return getDefaultSp(AppApplication.get()).getBoolean(CUSTOM_TABS_TIPS_ENABLE, true);
    }

    public static boolean isTopicsTipEnable(){
        return getDefaultSp(AppApplication.get()).getBoolean(TOPICS_TIP_ABLE, true);
    }

    public static boolean isDisableLoadingImage(){
        return getDefaultSp(AppApplication.get()).getBoolean(DISABLE_LOADING_IMAGE, false);
    }

    public static boolean isLoadImageEnable(){
        return NetHelper.INSTANCE.getNetStatus() == NetHelper.TYPE_WIFI || !PrefUtils.isDisableLoadingImage();
    }

    public static boolean isnewYearWishesTipEnable(){
        return getDefaultSp(AppApplication.get()).getBoolean(NEW_YEAR_WISHES_TIP_ENABLE, true);
    }

    public static SharedPreferences getDefaultSp(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getSP(Context context, String spName){
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

}
