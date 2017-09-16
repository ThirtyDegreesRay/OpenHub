package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IReleasesContract;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.presenter.ReleasesPresenter;
import com.thirtydegreesray.openhub.ui.activity.ReleaseInfoActivity;
import com.thirtydegreesray.openhub.ui.adapter.ReleasesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 11:33:07
 */

public class ReleasesFragment extends ListFragment<ReleasesPresenter, ReleasesAdapter>
        implements IReleasesContract.View{

    public static ReleasesFragment create(String owner, String repo){
        ReleasesFragment fragment = new ReleasesFragment();
        fragment.setArguments(BundleBuilder.builder().put("owner", owner).put("repo", repo).build());
        return fragment;
    }

    @Override
    public void showReleases(ArrayList<Release> releases) {
        adapter.setData(releases);
        adapter.notifyDataSetChanged();
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
        mPresenter.loadReleases(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_releases);
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        ReleaseInfoActivity.show(getActivity(), mPresenter.getRepoName(),
                adapter.getData().get(position));
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadReleases(page, false);
    }
}
