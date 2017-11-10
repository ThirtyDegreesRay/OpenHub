package com.thirtydegreesray.openhub.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.util.ViewUtils;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/11/9 12:50:41
 */

public abstract class BaseDrawerActivity<P extends IBaseContract.Presenter> extends BaseActivity<P>
        implements IBaseContract.View {

    @BindView(R.id.nav_view_start) @Nullable protected NavigationView navViewStart;
    @BindView(R.id.nav_view_end) @Nullable protected NavigationView navViewEnd;
    @BindView(R.id.drawer_layout) @Nullable protected DrawerLayout drawerLayout;

    private boolean startDrawerEnable = false;
    private boolean endDrawerEnable = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (drawerLayout == null) return;
        initStartDrawerView();
        initEndDrawerView();
    }

    private void initEndDrawerView() {
        if (navViewEnd == null) return;
        if (endDrawerEnable) {
            navViewEnd.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return BaseDrawerActivity.this.onNavigationItemSelected(item, false);
                }
            });
        } else {
            drawerLayout.removeView(navViewEnd);
        }
    }

    private void initStartDrawerView() {
        if (navViewStart == null) return;
        if (startDrawerEnable) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.syncState();
            navViewStart.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return BaseDrawerActivity.this.onNavigationItemSelected(item, true);
                }
            });
        } else {
            drawerLayout.removeView(navViewStart);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && (drawerLayout.isDrawerOpen(GravityCompat.START)
                || drawerLayout.isDrawerOpen(GravityCompat.END))) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout != null && item.getItemId() == getEndDrawerToggleMenuItemId()) {
            openDrawer(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onNavigationItemSelected(@NonNull final MenuItem item, final boolean isStartDrawer) {
        NavigationView navView = getNavigationView(isStartDrawer);
        boolean isMultiSelect = isStartDrawer ? isStartDrawerMultiSelect() : isEndDrawerMultiSelect();
        if (navView == null) return true;
        if (isMultiSelect) {
            ViewUtils.selectMenuItem(navView.getMenu(), item.getItemId(), true);
        }
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavItemSelected(item, isStartDrawer);
            }
        }, 250);
        return !isMultiSelect;
    }

    protected final void openDrawer(boolean isStartDrawer) {
        if (drawerLayout != null)
            drawerLayout.openDrawer(isStartDrawer ? GravityCompat.START : GravityCompat.END);
    }

    protected final void closeDrawer() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            if (drawerLayout.isDrawerOpen(GravityCompat.END))
                drawerLayout.closeDrawer(GravityCompat.END);
        }

    }

    private NavigationView getNavigationView(boolean isStartDrawer) {
        return isStartDrawer ? navViewStart : navViewEnd;
    }

    protected void removeStartDrawer() {
        removeDrawer(navViewStart);
    }

    protected void removeEndDrawer() {
        removeDrawer(navViewEnd);
    }

    protected void updateStartDrawerContent(int menuId) {
        updateDrawerContent(navViewStart, menuId);
    }

    protected void updateEndDrawerContent(int menuId) {
        updateDrawerContent(navViewEnd, menuId);
    }

    private void removeDrawer(NavigationView navView) {
        if (drawerLayout != null && navView != null) drawerLayout.removeView(navView);
    }

    private void updateDrawerContent(NavigationView navView, int menuId) {
        if (drawerLayout != null && navView != null) {
            navView.getMenu().clear();
            navView.inflateMenu(menuId);
            if (drawerLayout.indexOfChild(navView) == -1) drawerLayout.addView(navView);
        }
    }

    public void setStartDrawerEnable(boolean startDrawerEnable) {
        this.startDrawerEnable = startDrawerEnable;
    }

    public void setEndDrawerEnable(boolean endDrawerEnable) {
        this.endDrawerEnable = endDrawerEnable;
    }

    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {

    }

    protected boolean isStartDrawerMultiSelect() {
        return false;
    }

    protected boolean isEndDrawerMultiSelect() {
        return false;
    }

    protected int getEndDrawerToggleMenuItemId() {
        return -1;
    }

}
