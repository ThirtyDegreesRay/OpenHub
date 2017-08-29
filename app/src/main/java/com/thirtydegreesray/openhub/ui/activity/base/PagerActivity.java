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

package com.thirtydegreesray.openhub.ui.activity.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.IBaseContract;
import com.thirtydegreesray.openhub.mvp.presenter.BasePresenter;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentViewPagerAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 17:51:00
 */

public abstract class PagerActivity<P extends BasePresenter> extends BaseActivity<P>
        implements IBaseContract.View,
        ViewPager.OnPageChangeListener{

    @Inject protected FragmentViewPagerAdapter pagerAdapter;

    @BindView(R.id.view_pager) protected ViewPager viewPager;
    @BindView(R.id.tab_layout) protected TabLayout tabLayout;

    @Override
    protected void initActivity() {
        super.initActivity();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    @Deprecated
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = pagerAdapter.getCurFragment();
        if(fragment != null
                && fragment instanceof IFragmentKeyListener
                && ((IFragmentKeyListener)fragment).onKeyDown(keyCode, event)){
            return true;
        }
        return onMainKeyDown(keyCode, event);
    }

    protected boolean onMainKeyDown(int keyCode, KeyEvent event){
        return super.onKeyDown(keyCode, event);
    }

    public interface IFragmentKeyListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
