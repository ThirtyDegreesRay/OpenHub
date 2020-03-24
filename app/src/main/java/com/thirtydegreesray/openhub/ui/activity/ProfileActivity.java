

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
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
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 11:39:13
 */

public class ProfileActivity extends PagerActivity<ProfilePresenter>
        implements IProfileContract.View {

    public static void show(@NonNull Activity activity, @NonNull String loginId) {
        show(activity, loginId, null);
    }

    public static void show(@NonNull Activity activity,
                            @NonNull String loginId, @Nullable String userAvatar) {
        show(activity, null, loginId, userAvatar);
    }

    public static void show(@NonNull Activity activity, @Nullable View userAvatarView,
                            @NonNull String loginId, @Nullable String userAvatar) {
        Intent intent = createIntent(activity, loginId, userAvatar);
        if (userAvatarView != null) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, userAvatarView, "userAvatar");
            activity.startActivity(intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
        }

    }

    public static Intent createIntent(@NonNull Activity activity, @NonNull String loginId) {
        return createIntent(activity, loginId, null);
    }

    public static Intent createIntent(@NonNull Activity activity, @NonNull String loginId,
                                      @Nullable String userAvatar) {
        return new Intent(activity, ProfileActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("loginId", loginId)
                        .put("userAvatar", userAvatar)
                        .build());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mPresenter.getUser() != null) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            MenuItem followItem = menu.findItem(R.id.action_follow);
            MenuItem bookmark = menu.findItem(R.id.action_bookmark);
            followItem.setVisible(mPresenter.isUser() && !mPresenter.isMe());
            followItem.setTitle(mPresenter.isFollowing() ? R.string.unfollow : R.string.follow);
            bookmark.setTitle(mPresenter.isBookmarked() ?
                    getString(R.string.remove_bookmark) : getString(R.string.bookmark));
        }
        return true;
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
        switch (item.getItemId()) {
            case R.id.action_follow:
                mPresenter.followUser(!mPresenter.isFollowing());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isFollowing() ?
                        getString(R.string.followed) : getString(R.string.unfollowed));
                break;
            case R.id.action_bookmark:
                mPresenter.bookmark(!mPresenter.isBookmarked());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isBookmarked() ?
                        getString(R.string.bookmark_saved) : getString(R.string.bookmark_removed));
                break;
            case R.id.action_share:
                AppOpener.shareText(getActivity(), mPresenter.getUser().getHtmlUrl());
                break;
            case R.id.action_open_in_browser:
                AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getUser().getHtmlUrl());
                break;
            case R.id.action_copy_url:
                AppUtils.copyToClipboard(getActivity(), mPresenter.getUser().getHtmlUrl());
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
            notifyUserInfoUpdated(user);
        }
    }

    private void notifyUserInfoUpdated(User user){
        for(Fragment fragment : getFragments()){
            if(fragment != null && fragment instanceof ProfileInfoFragment){
                ((ProfileInfoFragment)fragment).updateProfileInfo(user);
            }
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

    private void setUserAvatar() {
        if (isAvatarSetted || StringUtils.isBlank(mPresenter.getUserAvatar())) return;
        isAvatarSetted = true;
        GlideApp.with(getActivity())
                .load(mPresenter.getUserAvatar())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userImageViewBg);
        GlideApp.with(getActivity())
                .load(mPresenter.getUserAvatar())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO Don't know why loader showImage automatic when resume from other page, conflict with screen transition
//        loader.setVisibility(View.GONE);
    }

    @Override
    public int getPagerSize() {
        return 3;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if (fragment instanceof ProfileInfoFragment) {
            return 0;
        } else if (fragment instanceof ActivityFragment) {
            return 1;
        } else if (fragment instanceof RepositoriesFragment) {
            return 2;
        } else
            return -1;
    }

    @OnClick(R.id.user_avatar)
    public void onUserAvatarClick() {
        if (!StringUtils.isBlank(mPresenter.getUserAvatar())) {
            ViewerActivity.showImage(getActivity(), mPresenter.getLoginId(),
                    mPresenter.getUserAvatar());
        }
    }

}
