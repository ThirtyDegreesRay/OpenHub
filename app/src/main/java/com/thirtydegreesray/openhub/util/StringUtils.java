

package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        }else if(size / 1024 < 1024 * 1024 * 1024){
            float sizeG = size / (1024f * 1024f * 1024f);
            return String.format(Locale.getDefault(), "%.2f GB", sizeG);
        }
        return null;
    }

    private final static Map<Locale, String> DATE_REGEX_MAP = new HashMap<>();
    static {
        DATE_REGEX_MAP.put(Locale.CHINA, "yyyy-MM-dd");
        DATE_REGEX_MAP.put(Locale.TAIWAN, "yyyy-MM-dd");
        DATE_REGEX_MAP.put(Locale.ENGLISH, "MMM d, yyyy");
        DATE_REGEX_MAP.put(Locale.GERMAN, "dd.MM.yyyy");
        DATE_REGEX_MAP.put(Locale.GERMANY, "dd.MM.yyyy");
    }

    public static String getDateStr(@NonNull Date date){
        Locale locale = AppUtils.getLocale(PrefUtils.getLanguage());
        String regex = DATE_REGEX_MAP.containsKey(locale) ? DATE_REGEX_MAP.get(locale) : "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(regex, locale);
        return format.format(date);
    }

    public static String getNewsTimeStr(@NonNull Context context, @NonNull Date date){
        long subTime = System.currentTimeMillis() - date.getTime();
        final double MILLIS_LIMIT = 1000.0f;
        final double SECONDS_LIMIT = 60 * MILLIS_LIMIT;
        final double MINUTES_LIMIT = 60 * SECONDS_LIMIT;
        final double HOURS_LIMIT = 24 * MINUTES_LIMIT;
        final double DAYS_LIMIT = 30 * HOURS_LIMIT;
        if(subTime < MILLIS_LIMIT){
            return context.getString(R.string.just_now);
        } else if(subTime < SECONDS_LIMIT){
            return Math.round(subTime / MILLIS_LIMIT) + " " + context.getString(R.string.seconds_ago);
        } else if(subTime < MINUTES_LIMIT){
            return Math.round(subTime / SECONDS_LIMIT) + " " + context.getString(R.string.minutes_ago);
        } else if(subTime < HOURS_LIMIT){
            return Math.round(subTime / MINUTES_LIMIT) + " " + context.getString(R.string.hours_ago);
        } else if(subTime < DAYS_LIMIT){
            return Math.round(subTime / HOURS_LIMIT) + " " + context.getString(R.string.days_ago);
        } else
            return getDateStr(date);
    }

    public static String upCaseFirstChar(String str){
        if(isBlank(str)) return null;
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }

    @NonNull public static Date getDateByTime(@NonNull Date time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @NonNull public static Date getTodayDate(){
        return getDateByTime(new Date());
    }

}
