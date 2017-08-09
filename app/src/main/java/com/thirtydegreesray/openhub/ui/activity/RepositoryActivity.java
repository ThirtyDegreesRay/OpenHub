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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.presenter.RepositoryPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.adapter.RepoPagerAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:39:20
 */

public class RepositoryActivity extends BaseActivity<RepositoryPresenter> {

    public enum RepositoryPage {
        INFO,
        FILES,
        COMMITS
    }

    private List<RepositoryPage> pageList = Arrays.asList(
            RepositoryPage.INFO,
            RepositoryPage.FILES,
            RepositoryPage.COMMITS
    );
    private List<String> pageTitleList;

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout)  TabLayout tabLayout;

    @Inject RepoPagerAdapter pagerAdapter;

    @Override
    protected void initActivity() {
        super.initActivity();
        pageTitleList = Arrays.asList(
                getString(R.string.info),
                getString(R.string.files),
                getString(R.string.commits)
        );
        pagerAdapter.setPagerList(pageList);
        pagerAdapter.setTitleList(pageTitleList);
    }

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
        return R.layout.activity_repository;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        toolbar.setTitle("Repository");
        setToolbarBackEnable();

    }

}
