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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IMainContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.MainPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.fragment.ProfileFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.TrendingFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, IMainContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.frame_layout_content) FrameLayout frameLayoutContent;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.nav_view) NavigationView navView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        navView.setCheckedItem(R.id.nav_profile);
        loadFragment("nav_profile");


        ImageView avatar = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
        TextView name = (TextView) navView.getHeaderView(0).findViewById(R.id.name);
        TextView mail = (TextView) navView.getHeaderView(0).findViewById(R.id.mail);

        User loginUser = AppData.INSTANCE.getLoggedUser();
        Picasso.with(this)
                .load(loginUser.getAvatarUrl())
                .into(avatar);
        name.setText(loginUser.getName());
        mail.setText(loginUser.getBio());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavItemSelected(item);
            }
        }, 250);
        return true;
    }

    private void onNavItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                loadFragment("nav_profile");
                break;
            case R.id.nav_news:
                loadFragment("nav_news");
                break;

            case R.id.nav_owned:
                loadRepositoriesFragment(RepositoriesFragment.RepositoriesType.OWNED);
                break;
            case R.id.nav_starred:
                loadRepositoriesFragment(RepositoriesFragment.RepositoriesType.STARRED);
                break;
            case R.id.nav_trending:
                TrendingFragment fragment = new TrendingFragment();
                fragment.setTabLayout(tabLayout);
                loadFragment(fragment);
                break;

            case R.id.nav_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
//                SettingsFragment settingsFragment = new SettingsFragment();
//                loadFragment(settingsFragment);
                break;
            case R.id.nav_about:
                loadFragment("nav_about");
                break;
            default:
                break;
        }
    }

    private void loadFragment(String name) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setName(name);
        loadFragment(fragment);
    }

    private void loadRepositoriesFragment(RepositoriesFragment.RepositoriesType repositoriesType) {
        RepositoriesFragment repositoriesFragment = new RepositoriesFragment();
        repositoriesFragment.setRepositoriesType(repositoriesType);
        loadFragment(repositoriesFragment);
    }


    private void loadFragment(Fragment fragment) {
        if (fragment instanceof TrendingFragment) {
            setToolbarScrollAble(true);
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            setToolbarScrollAble(false);
            tabLayout.setVisibility(View.GONE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_content, fragment)
                .commit();
    }

    private void setToolbarScrollAble(boolean scrollAble) {
        int flags = scrollAble ? (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) : 0;
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setScrollFlags(flags);
        toolbar.setLayoutParams(layoutParams);
    }

}
