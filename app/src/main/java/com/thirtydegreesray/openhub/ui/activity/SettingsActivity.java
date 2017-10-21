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

package com.thirtydegreesray.openhub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.ISettingsContract;
import com.thirtydegreesray.openhub.mvp.presenter.SettingsPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.SettingsFragment;

import butterknife.BindView;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public class SettingsActivity extends SingleFragmentActivity<SettingsPresenter, SettingsFragment>
        implements ISettingsContract.View ,
        SettingsFragment.SettingsCallBack{

    public static void show(@NonNull Activity activity, int requestCode){
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @BindView(R.id.root_layout) View rootLayout;
    @AutoAccess boolean recreated = false;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle(getString(R.string.settings));
        if(recreated){
            rootLayout.post(new Runnable() {
                @Override
                public void run() {
                    startAnimation();
                }
            });
            setResult(Activity.RESULT_OK);
        }

    }

    @Override
    protected SettingsFragment createFragment() {
        return new SettingsFragment();
    }

    private void startAnimation(){
        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0f, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        rootLayout.setVisibility(View.VISIBLE);
        anim.start();
    }


    @Override
    public void onLogout() {
        mPresenter.logout();
    }

    @Override
    public void onRecreate() {
        recreated = true;
        recreate();
    }

    @Override
    public void finishActivity() {
        super.finishActivity();
    }

}
