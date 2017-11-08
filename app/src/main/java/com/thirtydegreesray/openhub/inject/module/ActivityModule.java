

package com.thirtydegreesray.openhub.inject.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.thirtydegreesray.openhub.inject.ActivityScope;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * ActivityModule
 * Created by ThirtyDegreesRay on 2016/8/30 14:53
 */
@Module
public class ActivityModule {

    private BaseActivity mActivity;

    public ActivityModule(BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    public BaseActivity provideActivity(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    public Context provideContext(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    public FragmentManager provideFragmentManager(){
        return mActivity.getSupportFragmentManager();
    }

}
