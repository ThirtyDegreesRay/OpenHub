

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.util.AppOpener;

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
            AppOpener.launchUrl(activity, uri);
        }
    }

}
