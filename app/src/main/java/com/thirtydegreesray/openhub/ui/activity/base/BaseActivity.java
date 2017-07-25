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

package com.thirtydegreesray.openhub.ui.activity.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
import com.thirtydegreesray.openhub.ActivitiesManager;
import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.mvp.contract.IBaseView;
import com.thirtydegreesray.openhub.mvp.presenter.BasePresenter;
import com.thirtydegreesray.openhub.util.WindowUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * This is base activity to set some common things.
 * Created by ThirtyDegreesRay on 2016/7/13 18:13
 */
public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity implements IBaseView {

    @Inject
    protected P mPresenter;
    private ProgressDialog mProgressDialog;
    private static BaseActivity curActivity;

    protected boolean isAlive = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curActivity = getActivity();
        isAlive = true;
        ActivitiesManager.getInstance().addActivity(this);
        DataAutoAccess.getData(this, savedInstanceState);

        setContentView(getContentView());
        getScreenSize();
        ButterKnife.bind(getActivity());
        setupActivityComponent(getAppComponent());
        mPresenter.attachView(this);

        initActivity();
        initView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //系统由于内存不足而杀死activity，此时保存数据
        DataAutoAccess.saveData(this, outState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 依赖注入的入口
     * @param appComponent appComponent
     */
    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 获取ContentView id
     * @return
     */
    protected abstract int getContentView();

    /**
     * 初始化activity
     */
    protected abstract void initActivity();
    /**
     * 初始化view
     */
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
        curActivity = getActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        ActivitiesManager.getInstance().removeActivity(this);
        isAlive = false;
    }

    @Override
    public void showProgressDialog(String content) {
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }else{
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must show dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showShortToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTipDialog(String content) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    protected void postDelayFinish(int time){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, time);
    }

    public static BaseActivity getCurActivity() {
        return curActivity;
    }


    protected BaseActivity getActivity() {
        return this;
    }

    protected AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    protected AppComponent getAppComponent(){
        return getAppApplication().getAppComponent();
    }

    protected DaoSession getDaoSession(){
        return getAppComponent().getDaoSession();
    }

    public void onRefreshWebPage(){

    }

    /**
     * @param id  viewId
     * @param <T> View
     * @return View
     * @Description: 优化activity的方法，添加类型自动转换
     * @author: Yuyunhao
     */
    @Nullable
    protected <T extends View> T findViewByViewId(@IdRes int id){
        return (T) findViewById(id);
    }

    private void getScreenSize(){
        if(WindowUtil.screenHeight == 0 ||
                WindowUtil.screenWidth == 0){
            Display display = getWindowManager().getDefaultDisplay();
            WindowUtil.screenWidth = display.getWidth();
            WindowUtil.screenHeight = display.getHeight();
        }
    }

    public String getAppVersionName() {
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 引用资源文件中的字符串
     *
     * @param strId
     * @see [类、类#方法、类#成员]
     */
    protected String getResuceString(int strId) {
        return getResources().getString(strId);
    }

    protected void delayFinish(){
        delayFinish(1000);
    }

    protected void delayFinish(int mills){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, mills);
    }
}
