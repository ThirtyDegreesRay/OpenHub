

package com.thirtydegreesray.openhub.util;

import androidx.annotation.NonNull;

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
