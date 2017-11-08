

package com.thirtydegreesray.openhub.http.error;

import android.support.annotation.StringRes;

import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/10/19.
 *
 * @author ThirtyDegreesRay
 */

public class HttpErrorCode {

    /**
     * No cache and network available.
     */
    public final static int NO_CACHE_AND_NETWORK = 0;
    public final static int PAGE_NOT_FOUND = 1;
    public final static int UNAUTHORIZED = 2;

    private final static Map<Integer, String> ERROR_MSG_MAP = new HashMap<>();

    static{
        ERROR_MSG_MAP.put(NO_CACHE_AND_NETWORK, getString(R.string.no_cache_and_network));
        ERROR_MSG_MAP.put(PAGE_NOT_FOUND, getString(R.string.page_no_found));
        ERROR_MSG_MAP.put(UNAUTHORIZED, getString(R.string.unauthorized));
    }

    public static String getErrorMsg(int errorCode){
        return ERROR_MSG_MAP.get(errorCode);
    }

    private static String getString(@StringRes int resId){
        return AppApplication.get().getResources().getString(resId);
    }

}
