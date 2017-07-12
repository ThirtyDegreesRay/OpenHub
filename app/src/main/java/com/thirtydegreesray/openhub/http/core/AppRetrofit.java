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

package com.thirtydegreesray.openhub.http.core;

import android.util.Log;

import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.http.HttpConfig;
import com.thirtydegreesray.openhub.util.FileUtil;
import com.thirtydegreesray.openhub.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 网络请求
 * Created by YuYunHao on 2016/7/15 11:39
 */
public class AppRetrofit {

    private static final String TAG = "AppRetrofit";

    private AppRetrofit() {
        retrofitMap = new HashMap<>();
    }

    public static class SingletonHolder {
        private final static AppRetrofit instance = new AppRetrofit();
    }

    public static AppRetrofit getInstance() {
        return SingletonHolder.instance;
    }

    private HashMap<String, Retrofit> retrofitMap;

    /**
     * 初始化
     */
    private void init() {
//        Cache cache = new Cache(getCacheFile(), MyConfiguration.MAX_CACHE_SIZE);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(MyConfiguration.OUTER_NET_TIME_OUT, TimeUnit.MILLISECONDS)
//                .cache(cache)
//                .addInterceptor(new BaseInterceptor())
//                .addNetworkInterceptor(new NetworkBaseInterceptor())
//                .build();
//
//        String baseUrl = "http://" + MyConfiguration.wIp + ":" + MyConfiguration.wPort;
//        retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(okHttpClient)
//                .build();
    }

    private void createRetrofit(String baseUrl) {
        int timeOut = HttpConfig.HTTP_TIME_OUT;
        Cache cache = new Cache(FileUtil.getHttpImageCacheDir(AppApplication.get()),
                HttpConfig.MAX_CACHE_SIZE);

        SSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = getSLLSocketFactoryByCer(AppApplication.get().getAssets().open("cer/test.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .addInterceptor(new BaseInterceptor())
                .addNetworkInterceptor(new NetworkBaseInterceptor())
                .cache(cache)
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier(new UnSafeHostnameVerifier())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitMap.put(baseUrl, retrofit);
    }

    public Retrofit getRetrofit(String baseUrl) {
        if (!retrofitMap.containsKey(baseUrl)) {
            createRetrofit(baseUrl);
        }
        return retrofitMap.get(baseUrl);
    }

    /**
     * 拦截器
     */
    static class BaseInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d(TAG, request.url().toString());

//            String requestCacheControl = request.cacheControl().toString();
            //可缓存且无网络状态下取从缓存中取
//            if (!StringUtil.isBlank(requestCacheControl) &&
//                    !CheckNet.getNetEnabled(BaseActivity.getCurActivity())) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//            }

//            request = request.newBuilder()
//                    .addHeader("JSESSIONID", HttpConfig.SESSION_ID)
//                    .build();

            return chain.proceed(request);
        }
    }

    /**
     * 网络请求拦截器
     */
    static class NetworkBaseInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

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

            if (StringUtil.isBlank(requestCacheControl)) {
                return originalResponse;
            }
            //设置缓存策略
            else {

                Response res = originalResponse.newBuilder()
                        .header("Cache-Control", requestCacheControl)
                        //纠正服务器时间，服务器时间出错时可能会导致缓存处理出错
                        .header("Date", getGMTTime())
                        .removeHeader("Pragma")
                        .build();
                return res;
            }

        }
    }

    private static String getGMTTime(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime = format.format(date);
        return gmtTime;
    }

    private class UnSafeHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private SSLSocketFactory getSLLSocketFactoryByCer(InputStream... certificates) {
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)
                {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            return sslContext.getSocketFactory();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
