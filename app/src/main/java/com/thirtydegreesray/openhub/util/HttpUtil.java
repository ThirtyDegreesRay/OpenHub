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

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/7/13.
 *
 * @author ThirtyDegreesRay
 */

public class HttpUtil {

    @NonNull
    public static Map<String, String> getParams(@NonNull String url){
        Map<String, String> map = new HashMap<>();
        if(!StringUtils.isBlank(url) && url.contains("?")){
            String paramsStr = url.substring(url.indexOf("?") + 1);
            String[] params = paramsStr.split("&");
            for(int i = 0; i < params.length; i++){
                String param = params[i];
                String key = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(key, value);
            }
        }
        return map;
    }

}
