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

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IRepoInfoContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepoInfoPresenter;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.webview.PrettifyWebView;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:35:39
 */

public class RepoInfoFragment extends BaseFragment<RepoInfoPresenter>
        implements IRepoInfoContract.View{

    public static RepoInfoFragment create(Repository repository){
        return new RepoInfoFragment().setRepository(repository);
    }

    @BindView(R.id.prettify_web_view) PrettifyWebView webView;

    private Repository repository;

    public RepoInfoFragment setRepository(Repository repository) {
        this.repository = repository;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repo_info;
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
        mPresenter.loadReadMe(repository);
    }

    @Override
    public void showReadMe(String source, String baseUrl) {
        webView.setGithubContent(source, baseUrl);
    }
}
