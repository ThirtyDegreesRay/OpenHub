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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.ISplashContract;
import com.thirtydegreesray.openhub.mvp.presenter.SplashPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */
//TODO detection clipboard content
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    private final String TAG = "SplashActivity";

//    private final int REQUEST_ACCESS_TOKEN = 1;

    @Override
    public void showMainPage() {
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
        }
    }

    @Override
    public void showLoginPage() {
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return 0;
    }

    /**
     * 初始化activity
     */
    @Override
    protected void initActivity() {
        super.initActivity();
        mPresenter.getUser();
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
//            case REQUEST_ACCESS_TOKEN:
//                if(resultCode == RESULT_OK){
//                    showMainPage();
//                }
//                break;
            default:
                break;
        }
    }

}
