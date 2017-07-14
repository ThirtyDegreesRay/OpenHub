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
import android.util.Log;

import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.http.AppsService;
import com.thirtydegreesray.openhub.http.AuthService;
import com.thirtydegreesray.openhub.http.core.AppRetrofit;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.core.HttpSubscriber;
import com.thirtydegreesray.openhub.http.error.HttpError;
import com.thirtydegreesray.openhub.http.error.HttpErrorCode;
import com.thirtydegreesray.openhub.mvp.contract.IBaseView;
import com.thirtydegreesray.openhub.util.NetHelper;
import com.thirtydegreesray.openhub.util.StringUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * BasePresenter
 * Created by ThirtyDegreesRay on 2016/7/13 18:01
 */
public class BasePresenter<V extends IBaseView> {

    private final String TAG = "BasePresenter";

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
        return AppRetrofit.getInstance()
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL)
                .create(AppsService.class);
    }

    protected AuthService getAuthService() {
        return AppRetrofit.getInstance()
                .getRetrofit(AppConfig.GITHUB_BASE_URL)
                .create(AuthService.class);
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

    public interface IObservableCreator<T, R extends Response<T>>{
        Observable<R> createObservable(boolean forceNetWork);
    }

    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
     */
    protected <T, R extends Response<T>> void generalRxHttpExecute(
            Observable<R> observable, HttpSubscriber<T, R> subscriber) {
        if (subscriber != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<T, R>());
        }
    }

    protected <T, R extends Response<T>> void generalRxHttpExecute(
            IObservableCreator<T, R> observableCreator, HttpObserver<T> httpObserver) {
        generalRxHttpExecute(observableCreator, httpObserver, false);
    }

    protected <T, R extends Response<T>> void generalRxHttpExecute(
            final IObservableCreator<T, R> observableCreator, final HttpObserver<T> httpObserver,
            final boolean readCacheFirst) {

        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
            @Override
            public void onError(Throwable error) {
                httpObserver.onError(error);
            }

            @Override
            public void onSuccess(HttpResponse<T> response) {
                Log.i(TAG, "get data ok:" + System.currentTimeMillis());
                Log.i(TAG, "data:" + response.body());
                if(response.isSuccessful()){
                    if(readCacheFirst && response.isFromCache()
                            && NetHelper.getInstance().getNetEnabled()){
                        generalRxHttpExecute(observableCreator.createObservable(true),
                                getHttpSubscriber(this));
                    }
                    httpObserver.onSuccess(response);
                }else{
                    httpObserver.onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
                }
            }
        };
        generalRxHttpExecute(observableCreator.createObservable(false),
                getHttpSubscriber(tempObserver));
        Log.i(TAG, "get cache start:" + System.currentTimeMillis());
    }

    private <T> HttpSubscriber getHttpSubscriber(
            HttpObserver<T> httpObserver){
        return new HttpSubscriber<>(httpObserver);
    }


    protected String getLoadTip() {
        return "loading...";
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

    protected String getStringFromResource(@StringRes int resId){
        return getContext().getResources().getString(resId);
    }


}
