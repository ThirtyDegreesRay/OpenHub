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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.ISettingsContract;
import com.thirtydegreesray.openhub.mvp.presenter.SettingsPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public class SettingsActivity extends BaseActivity<SettingsPresenter>
        implements ISettingsContract.View{

    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarIcon(R.drawable.ic_arrow_back);
    }
}
