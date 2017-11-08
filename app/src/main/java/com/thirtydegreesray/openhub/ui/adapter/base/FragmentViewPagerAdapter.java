

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
