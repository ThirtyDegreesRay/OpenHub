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
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.presenter.CommitFilesPresenter;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.ui.adapter.CommitFilesAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.GitHubHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 14:38:13
 */

public class CommitFilesFragment extends ListFragment<CommitFilesPresenter, CommitFilesAdapter> {

    public static CommitFilesFragment create(@NonNull ArrayList<CommitFile> commitFiles){
        CommitFilesFragment fragment = new CommitFilesFragment();
        fragment.setCommitFiles(commitFiles);
        return fragment;
    }

    private ArrayList<CommitFile> commitFiles;

    public void setCommitFiles(ArrayList<CommitFile> commitFiles) {
        this.commitFiles = commitFiles;
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

    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_file);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(false);
        setRefreshEnable(false);
        if(commitFiles != null){
            adapter.setData(mPresenter.getSortedList(commitFiles));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        if(adapter.getData().get(position).getTypePosition() == 1){
            CommitFile commitFile = adapter.getData().get(position).getM2();
            if(GitHubHelper.isImage(commitFile.getFileName())){
                ViewerActivity.show(getActivity(), commitFile.getRawUrl());
            } else {
                ViewerActivity.showForDiff(getActivity(), commitFile);
            }
        }
    }

    public void showCommitFiles(@NonNull ArrayList<CommitFile> commitFiles){
        this.commitFiles = commitFiles;
        adapter.setData(mPresenter.getSortedList(commitFiles));
        adapter.notifyDataSetChanged();
    }

}
