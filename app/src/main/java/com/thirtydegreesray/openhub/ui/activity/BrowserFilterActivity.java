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

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.util.AppHelper;

/**
 * Created by ThirtyDegreesRay on 2017/10/19 17:31:18
 */

public class BrowserFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppData.INSTANCE.getLoggedUser() != null){
            handleBrowserUri(this, getIntent().getData());
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setData(getIntent().getData());
            startActivity(intent);
        }
        finish();
    }

    public static void handleBrowserUri(@NonNull Activity activity, @NonNull Uri uri){
        //handle shortcuts redirection
        if (uri.toString().equals("trending")){
            activity.startActivity(new Intent(activity, TrendingActivity.class));
        } else if (uri.toString().equals("search")){
            activity.startActivity(new Intent(activity, SearchActivity.class));
        } else {
            AppHelper.launchUrl(activity, uri);
        }
    }

}
