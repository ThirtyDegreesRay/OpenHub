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

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.mvp.contract.ILanguageTrendingContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.LanguageTrendingPresenter;
import com.thirtydegreesray.openhub.ui.adapter.RepositoriesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class LanguageTrendingFragment extends BaseFragment<LanguageTrendingPresenter>
            implements ILanguageTrendingContract.View{

    private String language;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private RepositoriesAdapter adapter;

    public LanguageTrendingFragment setLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public void showRepositories(ArrayList<Repository> repositoryList) {
        adapter.setData(repositoryList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repositories;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        adapter = new RepositoriesAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        mPresenter.loadRepositories(language);
    }

}
