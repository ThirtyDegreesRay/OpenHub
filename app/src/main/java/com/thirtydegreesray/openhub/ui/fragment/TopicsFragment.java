package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.ITopicsContract;
import com.thirtydegreesray.openhub.mvp.model.Topic;
import com.thirtydegreesray.openhub.mvp.presenter.TopicsPresenter;
import com.thirtydegreesray.openhub.ui.activity.RepoListActivity;
import com.thirtydegreesray.openhub.ui.adapter.TopicsAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/29 11:12:41
 */

public class TopicsFragment extends ListFragment<TopicsPresenter, TopicsAdapter>
        implements ITopicsContract.View {

    public static Fragment create(){
        return new TopicsFragment();
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(false);
    }

    @Override
    public void showTopics(ArrayList<Topic> topics) {
        adapter.setData(topics);
        postNotifyDataSetChanged();
        if(topics != null && topics.size() > 0 && PrefUtils.isTopicsTipEnable()){
            showOperationTip(R.string.topics_tip);
            PrefUtils.set(PrefUtils.TOPICS_TIP_ABLE, false);
        }
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
    protected void onReLoadData() {
        mPresenter.loadTopics(true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_topics);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        RepoListActivity.showTopic(getActivity(), adapter.getData().get(position));
    }
}
