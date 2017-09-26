package com.thirtydegreesray.openhub.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 9:50:06
 */

public abstract class PagerWithDrawerActivity<P extends BasePresenter> extends PagerActivity<P>
        implements IBaseContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view) protected NavigationView navView;
    @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        navView.inflateMenu(getDrawerMenuId());
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_sort) {
            openDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavItemSelected(item);
            }
        }, 250);
        return !isMultiGroup();
    }

    protected final void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    protected final void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @MenuRes
    protected abstract int getDrawerMenuId();

    protected abstract void onNavItemSelected(@NonNull MenuItem item);

    protected abstract boolean isMultiGroup();

}
