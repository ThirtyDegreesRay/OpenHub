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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IProfileContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.ProfilePresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 11:39:13
 */

public class ProfileActivity extends PagerActivity<ProfilePresenter>
        implements IProfileContract.View{

    public static void show(@NonNull Context context, @NonNull String loginId){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtras(BundleBuilder.builder().put("loginId", loginId).build());
        context.startActivity(intent);
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
        return R.layout.activity_view_pager;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem followItem = menu.findItem(R.id.action_follow);
        followItem.setVisible(mPresenter.isUser() && !mPresenter.isMe());
        followItem.setTitle(mPresenter.isFollowing() ? R.string.unfollow : R.string.follow);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getLoginId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mPresenter.getUser() == null && item.getItemId() != android.R.id.home){
            showWarningToast(getString(R.string.no_data));
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()){
            case R.id.action_follow:
                mPresenter.followUser(!mPresenter.isFollowing());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isFollowing() ?
                        getString(R.string.followed) : getString(R.string.unfollowed));
                break;
            case R.id.action_share:
                AppHelper.shareText(getActivity(), mPresenter.getUser().getHtmlUrl());
                break;
            case R.id.action_open_in_browser:
                AppHelper.openInBrowser(getActivity(), mPresenter.getUser().getHtmlUrl());
                break;
            case R.id.action_copy_url:
                AppHelper.copyToClipboard(getActivity(), mPresenter.getUser().getHtmlUrl());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProfileInfo(User user) {
        invalidateOptionsMenu();
        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel.createProfilePagerList(getActivity(), user));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
        } else {
//            AppEventBus.INSTANCE.getEventBus().post(new Event.RepoInfoUpdatedEvent(repo));
        }
    }

}
