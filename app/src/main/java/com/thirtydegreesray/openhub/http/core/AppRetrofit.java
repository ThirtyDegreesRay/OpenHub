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

package com.thirtydegreesray.openhub.http.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.util.FileUtil;
import com.thirtydegreesray.openhub.util.NetHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 网络请求
 * Created by ThirtyDegreesRay on 2016/7/15 11:39
 */
public enum  AppRetrofit {
    INSTANCE;

    private final String TAG = "AppRetrofit";

    private HashMap<String, Retrofit> retrofitMap = new HashMap<>();
    private String token;

    private void createRetrofit(@NonNull String baseUrl) {
        int timeOut = AppConfig.HTTP_TIME_OUT;
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(AppApplication.get()),
                AppConfig.HTTP_MAX_CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .addNetworkInterceptor(new NetworkBaseInterceptor())
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitMap.put(baseUrl, retrofit);
    }

    public Retrofit getRetrofit(@NonNull String baseUrl, @Nullable String token) {
        this.token = token;
        if (!retrofitMap.containsKey(baseUrl)) {
            createRetrofit(baseUrl);
        }
        return retrofitMap.get(baseUrl);
    }

    /**
     * 拦截器
     */
    private class BaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            //add access token
            String url = request.url().toString();
            if(!StringUtils.isBlank(token)){
                String auth = token.startsWith("Basic") ? token : "token " + token;
                request = request.newBuilder()
                        .addHeader("Authorization", auth)
                        .url(url)
                        .build();
            }
            Log.d(TAG, request.url().toString());

            //第二次请求，强制使用网络请求
            String forceNetWork = request.header("forceNetWork");
            //有forceNetWork且无网络状态下取从缓存中取
            if (!StringUtils.isBlank(forceNetWork) &&
                    !NetHelper.INSTANCE.getNetEnabled()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            } else if("true".equals(forceNetWork)){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }

            return chain.proceed(request);
        }
    }

    /**
     * 网络请求拦截器
     */
    private class NetworkBaseInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request request = chain.request();
            Response originalResponse = chain.proceed(request);

//            String serverCacheControl = originalResponse.header("Cache-Control");
            String requestCacheControl = request.cacheControl().toString();

//            //若服务器端有缓存策略，则无需修改
//            if (StringUtil.isBlank(serverCacheControl)) {
//                return originalResponse;
//            }
//            //不设置缓存策略
//            else

            //有forceNetWork时，强制更改缓存策略
            String forceNetWork = request.header("forceNetWork");
            if(!StringUtils.isBlank(forceNetWork)){
                requestCacheControl = getCacheString();
            }

            if (StringUtils.isBlank(requestCacheControl)) {
                return originalResponse;
            }
            //设置缓存策略
            else {
                Response res = originalResponse.newBuilder()
                        .header("Cache-Control", requestCacheControl)
                        //纠正服务器时间，服务器时间出错时可能会导致缓存处理出错
//                        .header("Date", getGMTTime())
                        .removeHeader("Pragma")
                        .build();
                return res;
            }

        }
    }

    private static String getGMTTime(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String gmtTime = format.format(date);
        return gmtTime;
    }

    public static String getCacheString(){
        return "public, max-age=" + AppConfig.CACHE_MAX_AGE;
    }

}
