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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentViewPagerAdapter;
import com.thirtydegreesray.openhub.util.Logger;

import java.util.ArrayList;

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

    private ArrayList<Fragment> fragments ;

    private int prePosition = 0;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void onPageSelected(final int position) {
        postNotifyFragmentStatus(prePosition, false, 100);
        postNotifyFragmentStatus(position, true, 500);
        prePosition = position;
        Logger.d("onPageSelected " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Notify first pager selected, only for first launch
     */
    protected void showFirstPager(){
        prePosition = 0;
        postNotifyFragmentStatus(0, true, 100);

    }

    private void postNotifyFragmentStatus(final int position, final boolean isShow, long delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShow){
                    pagerAdapter.getPagerList().get(position).getFragment().onFragmentShowed();
                }else{
                    pagerAdapter.getPagerList().get(position).getFragment().onFragmentHided();
                }
            }
        }, delay);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        int fragmentPosition = getFragmentPosition(fragment);
        if(fragmentPosition != -1) getFragments().set(fragmentPosition, fragment);
        Logger.d("onAttachFragment" + fragment);
    }

    @NonNull
    public ArrayList<Fragment> getFragments() {
        if(fragments == null){
            fragments = new ArrayList<>();
            for(int i = 0; i < getPagerSize(); i++){
                fragments.add(null);
            }
        }
        return fragments;
    }

    public abstract int getPagerSize();

    protected abstract int getFragmentPosition(Fragment fragment);

}
