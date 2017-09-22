package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IIssuesContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.presenter.IssuePresenter;
import com.thirtydegreesray.openhub.ui.adapter.IssuesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 15:00:12
 */

public class IssuesFragment extends ListFragment<IssuePresenter, IssuesAdapter>
        implements IIssuesContract.View{

    public enum IssueFragmentType{
        RepoOpen, RepoClosed,
        UserAssigned, UserCreated, UserMentioned, UserSubscribed
    }

    public static IssuesFragment create(@NonNull IssueFragmentType type,
                                        @NonNull String userId, @NonNull String repoName){
        IssuesFragment fragment = new IssuesFragment();
        fragment.setArguments(BundleBuilder.builder().put("userId", userId)
                .put("type", type).put("repoName", repoName).build());
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadIssues(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_issues);
    }

    @Override
    public void showIssues(ArrayList<Issue> issues) {
        adapter.setData(issues);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadIssues(page, false);
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

}
