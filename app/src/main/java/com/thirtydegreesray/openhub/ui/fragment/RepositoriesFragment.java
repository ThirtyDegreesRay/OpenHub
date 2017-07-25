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

package com.thirtydegreesray.openhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoriesContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepositoriesPresenter;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.RepositoriesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;

import java.util.ArrayList;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class RepositoriesFragment extends ListFragment<RepositoriesPresenter, RepositoriesAdapter>
            implements IRepositoriesContract.View{

    public enum RepositoriesType{
        OWNED, STARRED, TRENDING, EXPLORE
    }

    private RepositoriesType repositoriesType;

    private String language;

    public RepositoriesFragment setRepositoriesType(RepositoriesType repositoriesType) {
        this.repositoriesType = repositoriesType;
        return this;
    }

    public RepositoriesFragment setLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public void showRepositories(ArrayList<Repository> repositoryList) {
        adapter.setData(repositoryList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingView() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadError(String errorMsg) {
        setErrorTip(errorMsg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repositories;
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
    protected void initFragment(Bundle savedInstanceState){
        super.initFragment(savedInstanceState);
        mPresenter.loadRepositories(repositoriesType, language, false);
    }

    @Override
    protected void reLoadData() {
        mPresenter.loadRepositories(repositoriesType, language, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_repositories);
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        startActivity(new Intent(getActivity(), RepositoryActivity.class));
    }
}
