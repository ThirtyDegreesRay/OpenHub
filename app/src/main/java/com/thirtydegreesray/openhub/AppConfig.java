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

package com.thirtydegreesray.openhub;

/**
 * Created on 2016/11/23.
 *
 * @author YuYunhao
 */

public class AppConfig {

    public final static String SERVER_BASE_URL = "https://www.github.com/";

    public final static int HTTP_TIME_OUT = 10 * 1000;

    public final static int MAX_CACHE_SIZE = 3 * 1024 * 1024;


    public final static String OPENHUB_CLIENT_ID = "2a2f29517239a22ad850";

    public final static String OPENHUB_CLIENT_SECRET = "d16d28be9f7da6a92112a7c1e9671aef421eb8bf";

    public final static String AUTH_SCOPE = "user,repo,gist,notifications";

    public final static String OAUTH2_URL = SERVER_BASE_URL.concat("login/oauth/authorize");

    public final static String OPENHUB_HOME = "https://github.com/ThirtyDegreesRay/OpenHub";

}
