/*
 *    Copyright 2017 ThirtyDegreesRay
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

package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IActivityContract;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.presenter.ActivityPresenter;
import com.thirtydegreesray.openhub.ui.activity.ReleaseInfoActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.ActivitiesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:57:23
 */

public class ActivityFragment extends ListFragment<ActivityPresenter, ActivitiesAdapter>
        implements IActivityContract.View{

    public enum ActivityType {
        News, User, Repository
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user){
        return create(type, user, null);
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user,
                                          @NonNull String repo){
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(BundleBuilder.builder().put("type", type)
                .put("user", user).put("repo", repo).build());
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
        mPresenter.loadEvents(true, 1);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_activity);
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        Event event = adapter.getData().get(position);
        String owner = event.getRepo().getFullName().split("/")[0];
        switch (event.getType()){
            case ForkEvent:
                String actorId = event.getActor().getLogin();
                RepositoryActivity.show(getContext(), actorId, event.getRepo().getName());
                break;
            case ReleaseEvent:
                String repoName = event.getRepo().getFullName();
                repoName = repoName.substring(repoName.lastIndexOf("/") + 1);
                ReleaseInfoActivity.show(getActivity(), repoName,
                        event.getPayload().getRelease());
                break;
            default:
                RepositoryActivity.show(getContext(), owner, event.getRepo().getName());
                break;
        }
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadEvents(false, page);
    }

    @Override
    public void showEvents(ArrayList<Event> events) {
        adapter.setData(events);
        adapter.notifyDataSetChanged();
    }

}
