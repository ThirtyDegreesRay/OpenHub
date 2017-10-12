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
