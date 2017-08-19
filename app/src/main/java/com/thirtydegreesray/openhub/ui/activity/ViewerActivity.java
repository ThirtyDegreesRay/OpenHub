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

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.fragment.ViewerFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:05:44
 */

public class ViewerActivity extends BaseActivity {

    public static void show(@NonNull Context context, String url, String htmlUrl){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleBuilder.builder().put("url", url).put("htmlUrl", htmlUrl).build());
        context.startActivity(intent);
    }

    @AutoAccess String url;
    @AutoAccess String htmlUrl;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        String title = htmlUrl.substring(htmlUrl.lastIndexOf("/") + 1 , htmlUrl.length());
        setToolbarTiltle(title);

        ViewerFragment fragment = ViewerFragment.create(getActivity(), url, htmlUrl);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(StringUtils.isBlank(htmlUrl)) return true;
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                AppHelper.openInBrowser(getActivity(), htmlUrl);
                return true;
            case R.id.action_share:
                AppHelper.shareText(getActivity(), htmlUrl);
                return true;
            case R.id.action_copy_url:
                AppHelper.copyToClipboard(getActivity(), htmlUrl);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
