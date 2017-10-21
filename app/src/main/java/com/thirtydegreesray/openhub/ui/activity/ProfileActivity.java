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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IProfileContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.ProfilePresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProfileInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 11:39:13
 */

public class ProfileActivity extends PagerActivity<ProfilePresenter>
        implements IProfileContract.View{

    public static void show(@NonNull Activity activity, @NonNull String loginId){
        show(activity, loginId, null);
    }

    public static void show(@NonNull Activity activity,
                            @NonNull String loginId, @Nullable String userAvatar){
        show(activity, null, loginId, userAvatar);
    }

    public static void show(@NonNull Activity activity, @Nullable View userAvatarView,
                            @NonNull String loginId, @Nullable String userAvatar){
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtras(BundleBuilder.builder().put("loginId", loginId).put("userAvatar", userAvatar).build());
        if(userAvatarView != null){
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, userAvatarView, "userAvatar");
            activity.startActivity(intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
        }

    }

    private boolean isAvatarSetted = false;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @BindView(R.id.user_avatar_bg) ImageView userImageViewBg;
    @BindView(R.id.user_avatar) ImageView userImageView;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.joined_time) TextView joinedTime;
    @BindView(R.id.location) TextView location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_profile;
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
        setTransparentStatusBar();
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getLoginId());
        setUserAvatar();
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
        setUserAvatar();
        joinedTime.setText(getString(R.string.joined_at).concat(" ")
                .concat(StringUtils.getDateStr(user.getCreatedAt())));
        location.setText(user.getLocation());

        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel.createProfilePagerList(getActivity(), user, getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();
        } else {
//            AppEventBus.INSTANCE.getEventBus().post(new Event.RepoInfoUpdatedEvent(repo));
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loader.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        supportFinishAfterTransition();
    }

    private void setUserAvatar(){
        if(isAvatarSetted || StringUtils.isBlank(mPresenter.getUserAvatar())) return;
        isAvatarSetted = true;
        GlideApp.with(getActivity())
                .load(mPresenter.getUserAvatar())
                .centerCrop()
                .into(userImageViewBg);
        GlideApp.with(getActivity())
                .load(mPresenter.getUserAvatar())
                .placeholder(R.mipmap.logo)
                .into(userImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO Don't know why loader show automatic when resume from other page, conflict with screen transition
//        loader.setVisibility(View.GONE);
    }

    @Override
    public int getPagerSize() {
        return 3;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof ProfileInfoFragment){
            return 0;
        }else if(fragment instanceof ActivityFragment){
            return 1;
        }else if(fragment instanceof RepositoriesFragment){
            return 2;
        }else
            return -1;
    }

}
