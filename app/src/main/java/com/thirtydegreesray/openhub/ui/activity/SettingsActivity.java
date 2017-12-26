

package com.thirtydegreesray.openhub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
            recreated = false;
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
        /*
         * if set RESULT_OK, then called finishAffinity(), will caused crash:
         *      IllegalStateException Can not be called to deliver a result
         * set RESULT_CANCELED can solve the problem
         */
        setResult(Activity.RESULT_CANCELED);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//      finish setting activity to avoid  java.io.NotSerializableException
//      com.thirtydegreesray.openhub.ui.widget.colorChooser.ColorChooserPreference
//      android.os.Parcel.writeSerializable(Parcel.java:1761)
        if(recreated){
            super.onSaveInstanceState(outState);
        } else {
            finish();
        }
    }
}
