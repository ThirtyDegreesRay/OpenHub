package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.thirtydegreesray.openhub.mvp.presenter.IssuesActPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.ui.fragment.IssuesFragment;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.ui.widget.ZoomAbleFloatingActionButton;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:42:31
 */

public class IssuesActivity extends PagerActivity<IssuesActPresenter>
    implements ListFragment.ListScrollListener{

    public static void showForRepo(@NonNull Activity activity, @NonNull String userId,
                                   @NonNull String repoName) {
        Intent intent = createIntentForRepo(activity, userId, repoName);
        activity.startActivity(intent);
    }

    public static void showForUser(@NonNull Activity activity) {
        Intent intent = new Intent(activity, IssuesActivity.class);
        intent.putExtras(BundleHelper.builder()
                .put("issuesType", IssuesFilter.Type.User).build());
        activity.startActivity(intent);
    }

    public static Intent createIntentForRepo(@NonNull Activity activity, @NonNull String userId,
                                             @NonNull String repoName) {
        return new Intent(activity, IssuesActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("issuesType", IssuesFilter.Type.Repo)
                        .put("userId", userId)
                        .put("repoName", repoName).build());
    }

    private final static int ADD_ISSUE_REQUEST_CODE = 100;

    @AutoAccess String userId;
    @AutoAccess String repoName;
    @AutoAccess IssuesFilter.Type issuesType;
    @BindView(R.id.add_issue_bn) ZoomAbleFloatingActionButton addBn;

    private ArrayList<IssuesListListener> listeners;

    @Override
    protected void initActivity() {
        super.initActivity();
        setEndDrawerEnable(true);
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
        return R.layout.activity_view_pager_with_drawer;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        updateEndDrawerContent(R.menu.drawer_menu_issues);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        if (IssuesFilter.Type.Repo.equals(issuesType)) {
            setToolbarTitle(getString(R.string.issues), userId.concat("/").concat(repoName));
        } else {
            setToolbarTitle(getString(R.string.issues));
        }
        addBn.setVisibility(IssuesFilter.Type.Repo.equals(issuesType) ? View.VISIBLE : View.GONE);

        if (IssuesFilter.Type.User.equals(issuesType)) {
            pagerAdapter.setPagerList(FragmentPagerModel
                    .createUserIssuesPagerList(getActivity(), getFragments()));
        } else {
            List<FragmentPagerModel> fragmentPagerModels = FragmentPagerModel
                    .createRepoIssuesPagerList(getActivity(), userId, repoName, getFragments());
            pagerAdapter.setPagerList(fragmentPagerModels);
            navViewEnd.getMenu().findItem(R.id.nav_type_chooser).setVisible(false);
            ((ListFragment)fragmentPagerModels.get(0).getFragment()).setListScrollListener(this);
        }
        listeners = new ArrayList<>();
        for (FragmentPagerModel pagerModel : pagerAdapter.getPagerList()) {
            listeners.add((IssuesListListener) pagerModel.getFragment());
        }
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }

    @Override
    protected boolean isEndDrawerMultiSelect() {
        return true;
    }

    @Override
    protected int getEndDrawerToggleMenuItemId() {
        return R.id.nav_sort;
    }

    @Override
    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        listeners.get(0).onIssuesFilterChanged(getIssuesFilter(true));
        listeners.get(1).onIssuesFilterChanged(getIssuesFilter(false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @OnClick(R.id.add_issue_bn)
    public void onAddIssueClick() {
        EditIssueActivity.showForAdd(getActivity(), userId, repoName, ADD_ISSUE_REQUEST_CODE);
    }

    private IssuesFilter getIssuesFilter(boolean open) {
        IssuesFilter issuesFilter = new IssuesFilter(issuesType, open ? Issue.IssueState.open
                : Issue.IssueState.closed);
        if (IssuesFilter.Type.User.equals(issuesType)) {
            MenuItem selectedUserFilterMenu = ViewUtils.getSelectedMenu(
                    navViewEnd.getMenu().findItem(R.id.nav_type_chooser));
            if (selectedUserFilterMenu != null) {
                IssuesFilter.UserIssuesFilterType userFilterType =
                        getUserIssuesFilterType(selectedUserFilterMenu.getItemId());
                issuesFilter.setUserIssuesFilterType(userFilterType);
            }
        }
        MenuItem sortMenu = ViewUtils.getSelectedMenu(navViewEnd.getMenu().findItem(R.id.nav_sort));
        IssuesFilter.SortType sortType = IssuesFilter.SortType.Created;
        SortDirection sortDirection = SortDirection.Desc;
        if (sortMenu != null) {
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

    private IssuesFilter.UserIssuesFilterType getUserIssuesFilterType(int itemId) {
        switch (itemId) {
            case R.id.nav_all:
                return IssuesFilter.UserIssuesFilterType.All;
            case R.id.nav_created:
                return IssuesFilter.UserIssuesFilterType.Created;
            case R.id.nav_assigned:
                return IssuesFilter.UserIssuesFilterType.Assigned;
            case R.id.nav_mentioned:
                return IssuesFilter.UserIssuesFilterType.Mentioned;
            default:
                return IssuesFilter.UserIssuesFilterType.All;
        }
    }

    @Override
    public void onScrollUp() {
        addBn.zoomIn();
    }

    @Override
    public void onScrollDown() {
        addBn.zoomOut();
    }

    public interface IssuesListListener {
        void onIssuesFilterChanged(@NonNull IssuesFilter issuesFilter);

        void onCreateIssue(@NonNull Issue issue);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if(IssuesFilter.Type.Repo.equals(issuesType)){
            if(position == 0){
                addBn.zoomOut();
            } else {
                addBn.zoomIn();
            }
        }
    }

    @Override
    public int getPagerSize() {
        return 2;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if (fragment instanceof IssuesFragment) {
            IssuesFilter issuesFilter = fragment.getArguments().getParcelable("issuesFilter");
            if (issuesFilter == null) {
                return -1;
            } else {
                return Issue.IssueState.open.equals(issuesFilter.getIssueState()) ? 0 : 1;
            }
        } else
            return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == ADD_ISSUE_REQUEST_CODE) {
            Issue issue = data.getParcelableExtra("issue");
            listeners.get(0).onCreateIssue(issue);
        }
    }
}
