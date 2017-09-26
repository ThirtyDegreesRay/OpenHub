package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.filter.IssuesFilter;
import com.thirtydegreesray.openhub.mvp.model.filter.SortDirection;
import com.thirtydegreesray.openhub.mvp.presenter.RepoIssuesPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerWithDrawerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.ViewHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:42:31
 */

public class IssuesActivity extends PagerWithDrawerActivity<RepoIssuesPresenter> {

    public static void showForRepo(@NonNull Activity activity, @NonNull String userId,
                                   @NonNull String repoName){
        Intent intent = new Intent(activity, IssuesActivity.class);
        intent.putExtras(BundleBuilder.builder()
                .put("issuesType", IssuesFilter.Type.Repo)
                .put("userId", userId)
                .put("repoName", repoName).build());
        activity.startActivity(intent);
    }

    public static void showForUser(@NonNull Activity activity){
        Intent intent = new Intent(activity, IssuesActivity.class);
        intent.putExtras(BundleBuilder.builder()
                .put("issuesType", IssuesFilter.Type.User).build());
        activity.startActivity(intent);
    }

    @AutoAccess String userId;
    @AutoAccess String repoName;
    @AutoAccess IssuesFilter.Type issuesType;

    private ArrayList<IssuesFilterListener> listeners ;

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
        return R.layout.activity_view_pager_with_drawer;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.issues));

        if(IssuesFilter.Type.User.equals(issuesType)){
            pagerAdapter.setPagerList(FragmentPagerModel
                    .createUserIssuesPagerList(getActivity()));
        } else {
            pagerAdapter.setPagerList(FragmentPagerModel
                    .createRepoIssuesPagerList(getActivity(), userId, repoName));
            navView.getMenu().findItem(R.id.nav_type_chooser).setVisible(false);
        }
        listeners = new ArrayList<>();
        for(FragmentPagerModel pagerModel : pagerAdapter.getPagerList()){
            listeners.add((IssuesFilterListener) pagerModel.getFragment());
        }
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }

    @Override
    protected int getDrawerMenuId() {
        return R.menu.menu_issues;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ViewHelper.selectMenuItem(navView.getMenu(), item.getItemId(), true);
        return super.onNavigationItemSelected(item);
    }

    @Override
    protected void onNavItemSelected(@NonNull MenuItem item) {
        listeners.get(0).onIssuesFilterChanged(getIssuesFilter(true));
        listeners.get(1).onIssuesFilterChanged(getIssuesFilter(false));
    }

    @Override
    protected boolean isMultiGroup() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    private IssuesFilter getIssuesFilter(boolean open){
        IssuesFilter issuesFilter = new IssuesFilter(issuesType, open ? Issue.IssueState.open
                : Issue.IssueState.closed);
        if(IssuesFilter.Type.User.equals(issuesType)){
            MenuItem selectedUserFilterMenu = ViewHelper.getSelectedMenu(
                    navView.getMenu().findItem(R.id.nav_type_chooser));
            if(selectedUserFilterMenu != null) {
                IssuesFilter.UserIssuesFilterType userFilterType = Enum.valueOf(
                        IssuesFilter.UserIssuesFilterType.class, selectedUserFilterMenu.getTitle().toString());
                issuesFilter.setUserIssuesFilterType(userFilterType);
            }
        }
        MenuItem sortMenu = ViewHelper.getSelectedMenu(navView.getMenu().findItem(R.id.nav_sort));
        IssuesFilter.SortType sortType = IssuesFilter.SortType.Created;
        SortDirection sortDirection = SortDirection.Desc ;
        if(sortMenu != null) {
            switch (sortMenu.getItemId()) {
                case R.id.nav_recently_created:
                    sortType = IssuesFilter.SortType.Created;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_previously_created:
                    sortType = IssuesFilter.SortType.Created;
                    sortDirection = SortDirection.Asc;
                    break;
                case R.id.nav_recently_updated:
                    sortType = IssuesFilter.SortType.Updated;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_least_recently_updated:
                    sortType = IssuesFilter.SortType.Updated;
                    sortDirection = SortDirection.Asc;
                    break;
                case R.id.nav_most_comments:
                    sortType = IssuesFilter.SortType.Comments;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_fewest_comments:
                    sortType = IssuesFilter.SortType.Comments;
                    sortDirection = SortDirection.Asc;
                    break;
            }
        }
        issuesFilter.setSortType(sortType);
        issuesFilter.setSortDirection(sortDirection);
        return issuesFilter;
    }

    public interface IssuesFilterListener{
        void onIssuesFilterChanged(@NonNull IssuesFilter issuesFilter);
    }

}
