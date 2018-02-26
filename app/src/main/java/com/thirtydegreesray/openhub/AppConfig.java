

package com.thirtydegreesray.openhub;

import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/11/23.
 *
 * @author ThirtyDegreesRay
 */

public class AppConfig {

    public final static String GITHUB_BASE_URL = "https://github.com/";

    public final static String GITHUB_API_BASE_URL = "https://api.github.com/";

    public final static String GITHUB_CONTENT_BASE_URL = "https://raw.githubusercontent.com/";

    /**
     * This link are for OpenHub only. Please do not use this endpoint in your applications.
     * If you want to get trending repositories, you may stand up your own instance.
     * https://github.com/thedillonb/GitHub-Trending
     */
    public final static String OPENHUB_BASE_URL = "http://openhub.raysus.win:3000/";

    public final static int HTTP_TIME_OUT = 32 * 1000;

    public final static int HTTP_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int IMAGE_MAX_CACHE_SIZE = 32 * 1024 * 1024;

    public final static int CACHE_MAX_AGE = 4 * 7 * 24 * 60 * 60;

    public final static String DB_NAME = "OpenHub.db";



    public final static String OPENHUB_CLIENT_ID = BuildConfig.OPENHUB_CLIENT_ID;

    public final static String OPENHUB_CLIENT_SECRET = BuildConfig.OPENHUB_CLIENT_SECRET;

    public final static String OAUTH2_SCOPE = "user,repo,gist,notifications";


    public final static String OAUTH2_URL = GITHUB_BASE_URL + "login/oauth/authorize";

    public final static String AUTH_HOME = GITHUB_BASE_URL + "ThirtyDegreesRay";

    public final static String OPENHUB_HOME = AUTH_HOME + "/OpenHub";

    public final static String REDIRECT_URL = "https://github.com/ThirtyDegreesRay/OpenHub/CallBack";

    public final static String BUGLY_APPID = BuildConfig.DEBUG ? BuildConfig.DEBUG_BUGLY_ID : BuildConfig.BUGLY_ID;

    public final static String OPENHUB_RELEASE_SIGN_MD5 = "C0:99:37:D9:6A:36:FB:B7:AB:4C:5E:3D:42:96:FA:AF";

    public final static List<String> COMMON_PAGE_URL_LIST = Arrays.asList(
            "https://github.com/trending"
    );

    public final static boolean isCommonPageUrl(String url){
        if(StringUtils.isBlank(url)){
            return false;
        }
        for(String commonUrl : COMMON_PAGE_URL_LIST){
            if(url.contains(commonUrl)){
                return true;
            }
        }
        return false;
    }

}
