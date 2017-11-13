

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IMainContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.model.filter.RepositoriesFilter;
import com.thirtydegreesray.openhub.mvp.presenter.MainPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseDrawerActivity;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseDrawerActivity<MainPresenter>
        implements IMainContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.frame_layout_content) FrameLayout frameLayoutContent;

    private final Map<Integer, String> TAG_MAP = new HashMap<>();

    private final int SETTINGS_REQUEST_CODE = 100;

    private final int DEFAULT_PAGE = R.id.nav_news;
    @AutoAccess int selectedPage = DEFAULT_PAGE;

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

    @Override
    protected void initActivity() {
        super.initActivity();
        TAG_MAP.put(R.id.nav_news, ActivityFragment.class.getSimpleName());
        TAG_MAP.put(R.id.nav_owned, RepositoriesFragment.RepositoriesType.OWNED.name());
        TAG_MAP.put(R.id.nav_starred, RepositoriesFragment.RepositoriesType.STARRED.name());
        if (AppData.INSTANCE.getLoggedUser() != null)
            CrashReport.putUserData(getApplicationContext(),
                    "GitHubId", AppData.INSTANCE.getLoggedUser().getLogin());

        setStartDrawerEnable(true);
        setEndDrawerEnable(true);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        updateStartDrawerContent(R.menu.activity_main_drawer);
        removeEndDrawer();
        navViewStart.setCheckedItem(selectedPage);
        updateTitle(selectedPage);
        loadFragment(selectedPage);
        updateFilter(selectedPage);

        ImageView avatar =  navViewStart.getHeaderView(0).findViewById(R.id.avatar);
        TextView name =  navViewStart.getHeaderView(0).findViewById(R.id.name);
        TextView mail =  navViewStart.getHeaderView(0).findViewById(R.id.mail);

        User loginUser = AppData.INSTANCE.getLoggedUser();
        GlideApp.with(getActivity())
                .load(loginUser.getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(avatar);
        name.setText(StringUtils.isBlank(loginUser.getName()) ? loginUser.getLogin() : loginUser.getName());
        String joinTime = getString(R.string.joined_at).concat(" ")
                .concat(StringUtils.getDateStr(loginUser.getCreatedAt()));
        mail.setText(StringUtils.isBlank(loginUser.getBio()) ? joinTime : loginUser.getBio());

        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        MenuItem menuItem = menu.findItem(R.id.nav_sort);
        menuItem.setVisible(selectedPage == R.id.nav_owned || selectedPage == R.id.nav_starred);
        return true;
    }

    @Override
    protected boolean isEndDrawerMultiSelect() {
        return true;
    }

    @Override
    protected int getEndDrawerToggleMenuItemId() {
        return R.id.nav_sort;
    }

    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        super.onNavItemSelected(item, isStartDrawer);
        if(!isStartDrawer){
            handlerEndDrawerClick(item);
            return;
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                ProfileActivity.show(getActivity(), AppData.INSTANCE.getLoggedUser().getLogin(),
                        AppData.INSTANCE.getLoggedUser().getAvatarUrl());
                break;
            case R.id.nav_issues:
                IssuesActivity.showForUser(getActivity());
                break;
            case R.id.nav_notifications:
                NotificationsActivity.show(getActivity());
                break;
            case R.id.nav_news:
            case R.id.nav_owned:
            case R.id.nav_starred:
                updateTitle(id);
                loadFragment(id);
                updateFilter(id);
                break;
            case R.id.nav_trending:
                TrendingActivity.show(getActivity());
                break;
            case R.id.nav_search:
                SearchActivity.show(getActivity());
                break;
            case R.id.nav_trace:
                TraceActivity.show(getActivity());
                break;
            case R.id.nav_settings:
                SettingsActivity.show(getActivity(), SETTINGS_REQUEST_CODE);
                break;
            case R.id.nav_about:
                AboutActivity.show(getActivity());
                break;
            default:
                break;
        }
    }

    private void updateFilter(int itemId){
        if(itemId == R.id.nav_owned){
            updateEndDrawerContent(R.menu.menu_repositories_filter);
            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.OWNED);
        }else if(itemId == R.id.nav_starred){
            updateEndDrawerContent(R.menu.menu_repositories_filter);
            RepositoriesFilter.initDrawer(navViewEnd, RepositoriesFragment.RepositoriesType.STARRED);
        }else {
            removeEndDrawer();
        }
        invalidateOptionsMenu();
    }

    private void updateTitle(int itemId) {
        switch (itemId) {
            case R.id.nav_news:
                setToolbarTitle(getString(R.string.news));
                break;
            case R.id.nav_owned:
                setToolbarTitle(getString(R.string.my_repos));
                break;
            case R.id.nav_starred:
                setToolbarTitle(getString(R.string.starred_repos));
                break;
            default:
                setToolbarTitle(getString(R.string.app_name));
                break;
        }
    }

    private void loadFragment(int itemId) {
        selectedPage = itemId;
        String fragmentTag = TAG_MAP.get(itemId);
        Fragment showFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        boolean isExist = true;
        if (showFragment == null) {
            isExist = false;
            showFragment = getFragment(itemId);
        }
        if (showFragment.isVisible()) {
            return;
        }

        Fragment visibleFragment = getVisibleFragment();
        if (isExist) {
            showAndHideFragment(showFragment, visibleFragment);
        } else {
            addAndHideFragment(showFragment, visibleFragment, fragmentTag);
        }
    }

    @NonNull
    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.nav_news:
                return ActivityFragment.create(ActivityFragment.ActivityType.News,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_owned:
                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.OWNED,
                        AppData.INSTANCE.getLoggedUser().getLogin());
            case R.id.nav_starred:
                return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED,
                        AppData.INSTANCE.getLoggedUser().getLogin());
        }
        return null;
    }

    private void showAndHideFragment(@NonNull Fragment showFragment, @Nullable Fragment hideFragment) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .hide(hideFragment)
                    .commit();
        }

    }

    private void addAndHideFragment(@NonNull Fragment showFragment,
                                    @Nullable Fragment hideFragment, @NonNull String addTag) {
        if (hideFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_content, showFragment, addTag)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout_content, showFragment, addTag)
                    .hide(hideFragment)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            recreate();
        }
    }

    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        Fragment fragment = getVisibleFragment();
        if(fragment != null && fragment instanceof BaseFragment){
            ((BaseFragment)fragment).scrollToTop();
        }
    }

    private void handlerEndDrawerClick(MenuItem item){
        Fragment fragment = getVisibleFragment();
        if(fragment!= null && fragment instanceof RepositoriesFragment
            &&(selectedPage == R.id.nav_owned || selectedPage == R.id.nav_starred)){
            ((RepositoriesFragment)fragment).onDrawerSelected(navViewEnd, item);
        }
    }

}
