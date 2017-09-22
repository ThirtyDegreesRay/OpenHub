package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.presenter.RepoIssuesPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.util.BundleBuilder;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:42:31
 */

public class RepoIssuesActivity extends PagerActivity<RepoIssuesPresenter> {

    public static void show(@NonNull Activity activity, @NonNull String userId,
                            @NonNull String repoName){
        Intent intent = new Intent(activity, RepoIssuesActivity.class);
        intent.putExtras(BundleBuilder.builder().put("userId", userId)
                .put("repoName", repoName).build());
        activity.startActivity(intent);
    }

    @AutoAccess String userId;
    @AutoAccess String repoName;

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
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.issues));

        pagerAdapter.setPagerList(FragmentPagerModel
                .createRepoIssuesPagerList(getActivity(), userId, repoName));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }
}
