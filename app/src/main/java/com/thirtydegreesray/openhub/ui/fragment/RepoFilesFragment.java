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
import android.view.KeyEvent;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IRepoFilesContract;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepoFilesPresenter;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.RepoFilesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 16:13:20
 */

public class RepoFilesFragment extends ListFragment<RepoFilesPresenter, RepoFilesAdapter>
        implements IRepoFilesContract.View, PagerActivity.IFragmentKeyListener{

    public static RepoFilesFragment create(Repository repository) {
        return new RepoFilesFragment().setRepository(repository);
    }

    private Repository repo;
    private String curPath = "";

    public RepoFilesFragment setRepository(Repository repository) {
        this.repo = repository;
        return this;
    }

    @Override
    public void showFiles(ArrayList<FileModel> files) {
        adapter.setData(files);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
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
        mPresenter.loadFiles(repo, repo.getDefaultBranch(), curPath, false);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadFiles(repo, repo.getDefaultBranch(), curPath, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_cache_and_network);
    }

    @Override
    public void onItemClick(int position) {
        super.onItemClick(position);
        FileModel model = adapter.getData().get(position);
        if(model.isDir()){
            curPath = model.getPath();
            mPresenter.loadFiles(repo, repo.getDefaultBranch(), curPath, false);
        }else{
            ViewerActivity.show(getActivity(), model.getUrl(), model.getHtmlUrl());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(!StringUtils.isBlank(curPath)){
                curPath = curPath.contains("/") ?
                        curPath.substring(0, curPath.lastIndexOf("/")) : "";
                mPresenter.loadFiles(repo, repo.getDefaultBranch(), curPath, false);
                return true;
            }
        }
        return false;
    }
}
