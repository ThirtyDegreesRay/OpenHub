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

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:24:34
 */

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentPagerModel> mPagerList;

    private Fragment curFragment;

    @Inject
    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPagerList(List<FragmentPagerModel> pagerList) {
        mPagerList = pagerList;
    }

    @Override
    public Fragment getItem(int position) {
        return mPagerList.get(position).getFragment();
    }


    @Override
    public int getCount() {
        return mPagerList == null ? 0 : mPagerList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPagerList.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(curFragment != null && curFragment.equals(object)){
            curFragment = null;
        }
//        super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        curFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurFragment() {
        return curFragment;
    }

    public List<FragmentPagerModel> getPagerList() {
        return mPagerList;
    }

}
