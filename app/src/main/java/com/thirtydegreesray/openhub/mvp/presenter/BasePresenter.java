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

package com.thirtydegreesray.openhub.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.http.AppsService;
import com.thirtydegreesray.openhub.http.HttpConfig;
import com.thirtydegreesray.openhub.http.core.AppRetrofit;
import com.thirtydegreesray.openhub.http.core.HttpSubscriber;
import com.thirtydegreesray.openhub.http.error.HttpError;
import com.thirtydegreesray.openhub.http.error.HttpErrorCode;
import com.thirtydegreesray.openhub.mvp.contract.IBaseView;
import com.thirtydegreesray.openhub.util.StringUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * BasePresenter
 * Created by YuYunHao on 2016/7/13 18:01
 */
public class BasePresenter<V extends IBaseView> {

    //View
    protected V mView;
    //db Dao
    protected DaoSession daoSession;

    public BasePresenter(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    /**
     * 绑定View
     *
     * @param view
     */
    @NonNull
    public void attachView(V view) {
        mView = view;
        onViewAttached();
    }

    /**
     * 取消View绑定
     */
    public void detachView() {
        mView = null;
    }

    /**
     * Retrofit
     *
     * @return Retrofit
     */
    protected AppsService getAPPSService() {
        return AppRetrofit.getInstance().getRetrofit(HttpConfig.SERVER_BASE_URL)
                .create(AppsService.class);
    }

    /**
     * 获取上下文，需在onViewAttached()后调用
     *
     * @return
     */
    protected Context getContext() {
        if (mView instanceof Context) {
            return (Context) mView;
        } else {
            throw new NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.");
        }
    }

    /**
     * presenter和view绑定成功
     */
    protected void onViewAttached() {

    }

    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
     */
    protected <T extends ResponseBody> void generalRxHttpExecute(
            Observable<T> observable, HttpSubscriber<T> subscriber) {
        if (subscriber != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<>());
        }
    }

    protected void showShortToast(String message) {
        if (mView != null) {
            mView.showShortToast(message);
        }
    }

    protected String getStringFromBody(ResponseBody responseBody) {
        try {
            return responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getLoadTip() {
        return "加载中...";
    }

    /**
     * 获取error提示
     * @param error
     * @param typeInfo
     * @return
     */
    protected String getErrorTip(Throwable error, String typeInfo) {
        String errorTip = null;
        if (error instanceof SocketTimeoutException
                || error instanceof ConnectTimeoutException) {
            errorTip = "网络超时，请检查网络后重试！";
        } else if (error instanceof HttpError) {
            errorTip = error.getMessage();
        } else {
            errorTip = "网络异常，请检查网络后重试！";
        }
        return StringUtil.isBlank(typeInfo) ? errorTip : typeInfo + "，" + errorTip;
    }
    /**
     * 获取error提示
     * @param error
     * @return
     */
    protected String getErrorTip(Throwable error) {
        return getErrorTip(error, null);
    }

    protected boolean isNetUnable(Throwable error) {
        if (error instanceof HttpError) {
            if(((HttpError) error).getErrorCode() == HttpErrorCode.NET_UNABLE){
                return true;
            }
        }
        return false;
    }

    protected String getStringFromResource(@StringRes int resId){
        return getContext().getResources().getString(resId);
    }

    protected interface CheckListener {
        void onOK();
    }

}
