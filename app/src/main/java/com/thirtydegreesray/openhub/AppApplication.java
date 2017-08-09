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

import android.app.Application;
import android.util.Log;

import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerAppComponent;
import com.thirtydegreesray.openhub.inject.module.AppModule;
import com.thirtydegreesray.openhub.util.NetHelper;

/**
 * AppApplication
 * Created by ThirtyDegreesRay on 2016/7/13 14:01
 */
public class AppApplication extends Application {

    private final String TAG = "AppApplication";

    private static AppApplication application;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //init application
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime:" + startTime);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        startTime = System.currentTimeMillis();
        NetHelper.getInstance().init(this);
        Log.i(TAG, "application ok:" + (System.currentTimeMillis() - startTime));
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

}
