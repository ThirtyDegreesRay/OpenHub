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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 检测实时网络状态  <p>
 *
 * @author Administrator
 */
public class CheckNet {

    public static final int TYPE_DISCONNECT = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;

    /**
     * 获取当前网络状态
     *
     * @param context
     * @return
     */
    public static int getNetStatus(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI)
                            return TYPE_WIFI;
                        if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                            return TYPE_MOBILE;
                    }
                } else {
                    return TYPE_DISCONNECT;
                }
            }
        } catch (Exception e) {
            Log.v("error", e.toString());
            e.printStackTrace();
        }
        return TYPE_DISCONNECT;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static Boolean getNetEnabled(Context context) {
        int netStatus = getNetStatus(context);
        return netStatus == TYPE_MOBILE || netStatus == TYPE_WIFI;
    }

    /**
     * 是否处于移动网络状态
     *
     * @param context
     * @return
     */
    public static Boolean isMobileStatus(Context context) {
        int netStatus = getNetStatus(context);
        return netStatus == TYPE_MOBILE;
    }

}
