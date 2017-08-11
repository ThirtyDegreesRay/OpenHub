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

package com.thirtydegreesray.openhub.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewPagerAdapter;
import com.thirtydegreesray.openhub.ui.fragment.ProfileFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 22:28:16
 */

public class RepoPagerAdapter extends BaseViewPagerAdapter<RepositoryActivity.RepositoryPage> {

    private Repository repository;

    @Inject
    public RepoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return RepoInfoFragment.create(repository);
        }
        return new ProfileFragment().setName("repo");
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
