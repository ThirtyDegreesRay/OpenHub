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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IProfileContract;
import com.thirtydegreesray.openhub.mvp.presenter.ProfilePresenter;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class ProfileFragment extends BaseFragment<ProfilePresenter>
            implements IProfileContract.View{

    @Nullable @BindView(R.id.name) TextView tvName;

    private String name ;

    public ProfileFragment() {

    }

    @NonNull
    public ProfileFragment setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
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
        mPresenter.loadContent(name);
    }

    @Override
    public void showContent(String content) {
        tvName.setText(content);
    }
}
