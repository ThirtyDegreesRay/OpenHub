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

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:24:34
 */

public abstract class BaseViewPagerAdapter<M extends Object> extends FragmentStatePagerAdapter {

    protected List<M> mPageList;

    protected List<String> mTitleList;

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPagerList(List<M> pagerList) {
        this.mPageList = pagerList;
    }

    public void setTitleList(List<String> titleList) {
        this.mTitleList = titleList;
    }

    @Override
    public int getCount() {
        return mPageList == null ? 0 : mPageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList != null ? mTitleList.get(position) : super.getPageTitle(position);
    }

}
