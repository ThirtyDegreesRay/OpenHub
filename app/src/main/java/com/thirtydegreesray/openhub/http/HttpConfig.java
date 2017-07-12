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

package com.thirtydegreesray.openhub.http;

/**
 * Created on 2016/11/23.
 *
 * @author YuYunhao
 */

public class HttpConfig {

    public final static String SERVER_IP = "192.168.3.56";

    //    public final static String WEB_PAGE_BASE_URL = "http://" + SERVER_IP + ":8080/nhvideo/";
//    public static String SERVER_BASE_URL = "http://10.1.31.33:8082/jttmobilemix/";
//    public static String SERVER_BASE_URL = "http://192.168.3.34:8080/jttmobilemix/";
    public static String SERVER_BASE_URL = "https://218.2.208.154:8093/jttmobilemix/";
//    http://10.1.31.33:8081/jttmobile/schedule/querySchedule?startTime=2016-01-01&endTime=2017-01-31

    public final static int HTTP_TIME_OUT = 10 * 1000;

    public static String WEB_PAGE_BASE_URL = "https://218.2.208.154:8093";

    public static String LOGIN_PAGE_URL = WEB_PAGE_BASE_URL + "/jttmobilemix";

    public static String[] TAB_PAGE_URLS = new String[4];


    public static int MAX_CACHE_SIZE = 3 * 1024 * 1024;

//    public static String SESSION_ID = "2F3E3CBD81B146BDBDA6FE7968EB8D14";
//    public static String USER_NAME = "管理员";
    public static String SESSION_ID = "";
    public static String USER_NAME = "";

    public final static String HOME_PAGE_URL = "http://218.2.208.154:8090/jttmobile/main/gotoHomepage";

    public static boolean isLocalMode = false;

}
