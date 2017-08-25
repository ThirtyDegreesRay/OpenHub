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
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.common.AppEventBus;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.LoginService;
import com.thirtydegreesray.openhub.http.RepoService;
import com.thirtydegreesray.openhub.http.SearchService;
import com.thirtydegreesray.openhub.http.UserService;
import com.thirtydegreesray.openhub.http.core.AppRetrofit;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.core.HttpSubscriber;
import com.thirtydegreesray.openhub.http.error.HttpError;
import com.thirtydegreesray.openhub.http.error.HttpErrorCode;
import com.thirtydegreesray.openhub.mvp.contract.IBaseContract;
import com.thirtydegreesray.openhub.util.Logger;
import com.thirtydegreesray.openhub.util.NetHelper;
import com.thirtydegreesray.openhub.util.PrefHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * BasePresenter
 * Created by ThirtyDegreesRay on 2016/7/13 18:01
 */
public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    private final String TAG = "BasePresenter";

    //View
    protected V mView;
    //db Dao
    protected DaoSession daoSession;

    private ArrayList<Subscriber<?>> subscribers;
    private boolean isEventSubscriber = false;
    private boolean isAttached = false;

    public BasePresenter(DaoSession daoSession) {
        this.daoSession = daoSession;
        subscribers = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        DataAutoAccess.saveData(this, outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        if (outState == null) return;
        DataAutoAccess.getData(this, outState);
    }

    /**
     * 绑定View
     *
     * @param view view
     */
    @Override
    public void attachView(@NonNull V view) {
        mView = view;
        if (isEventSubscriber) AppEventBus.INSTANCE.getEventBus().register(this);
        onViewAttached();
        isAttached = true;
    }

    /**
     * 取消View绑定
     */
    @Override
    public void detachView() {
        mView = null;
        //view 取消绑定时，把请求取消订阅
        for (Subscriber subscriber : subscribers) {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.unsubscribe();
                Logger.d(TAG, "unsubscribe:" + subscriber.toString());
            }
        }
        if (isEventSubscriber) AppEventBus.INSTANCE.getEventBus().unregister(this);
    }

    @Override
    public void onViewInitialized() {

    }

    /**
     * Retrofit
     *
     * @return Retrofit
     */

    protected LoginService getLoginService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_BASE_URL, null)
                .create(LoginService.class);
    }

    protected LoginService getLoginService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
                .create(LoginService.class);
    }

    protected UserService getUserService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, token)
                .create(UserService.class);
    }

    protected UserService getUserService() {
        return getUserService(AppData.INSTANCE.getAuthUser().getAccessToken());
    }

    protected RepoService getRepoService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL,
                        AppData.INSTANCE.getAuthUser().getAccessToken())
                .create(RepoService.class);
    }

    protected SearchService getSearchService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL,
                        AppData.INSTANCE.getAuthUser().getAccessToken())
                .create(SearchService.class);
    }

    /**
     * 获取上下文，需在onViewAttached()后调用
     *
     * @return
     */
    @NonNull
    @Override
    public Context getContext() {
        if (mView instanceof Context) {
            return (Context) mView;
        } else if (mView instanceof Fragment) {
            return ((Fragment) mView).getContext();
        } else {
            throw new NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.");
        }
    }

    /**
     * presenter和view绑定成功
     */
    @CallSuper
    protected void onViewAttached() {

    }

    protected interface IObservableCreator<T> {
        Observable<Response<T>> createObservable(boolean forceNetWork);
    }

    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
     */
    protected <T> void generalRxHttpExecute(
            @NonNull Observable<Response<T>> observable, @Nullable HttpSubscriber<T> subscriber) {
        if (subscriber != null) {
            subscribers.add(subscriber);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<T>());
        }
    }

    protected <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator
            , @NonNull HttpObserver<T> httpObserver) {
        generalRxHttpExecute(observableCreator, httpObserver, false);
    }

    protected <T> void generalRxHttpExecute(@NonNull final IObservableCreator<T> observableCreator
            , @NonNull final HttpObserver<T> httpObserver,
                                            final boolean readCacheFirst) {

        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
            @Override
            public void onError(Throwable error) {
                httpObserver.onError(error);
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<T> response) {
                Logger.d(TAG, "get data ok:" + System.currentTimeMillis());
                Logger.d(TAG, "data:" + response.body());
                if (response.isSuccessful()) {
                    if (readCacheFirst && response.isFromCache()
                            && NetHelper.getInstance().getNetEnabled()) {
                        generalRxHttpExecute(observableCreator.createObservable(true),
                                getHttpSubscriber(this));
                    }
                    httpObserver.onSuccess(response);
                } else {
                    httpObserver.onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
                }

            }
        };

        boolean cacheFirstEnable = PrefHelper.isCacheFirstEnable();
        generalRxHttpExecute(observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
                getHttpSubscriber(tempObserver));
        Logger.d(TAG, "get date start:" + System.currentTimeMillis());
    }

    private <T> HttpSubscriber<T> getHttpSubscriber(HttpObserver<T> httpObserver) {
        return new HttpSubscriber<>(httpObserver);
    }


    @NonNull
    protected String getLoadTip() {
        return "loading...";
    }

    protected boolean isLastResponse(@NonNull HttpResponse response) {
        return response.isFromNetWork() ||
                !NetHelper.getInstance().getNetEnabled();
    }

    /**
     * 获取error提示
     *
     * @param error
     * @param typeInfo
     * @return
     */
    @NonNull
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
        return StringUtils.isBlank(typeInfo) ? errorTip : typeInfo + "，" + errorTip;
    }

    /**
     * 获取error提示
     *
     * @param error
     * @return
     */
    @NonNull
    protected String getErrorTip(Throwable error) {
        return getErrorTip(error, null);
    }

    @NonNull
    protected String getString(@StringRes int resId) {
        return getContext().getResources().getString(resId);
    }

    public void setEventSubscriber(boolean eventSubscriber) {
        isEventSubscriber = eventSubscriber;
        if(isEventSubscriber && isAttached && !AppEventBus.INSTANCE.getEventBus().isRegistered(this)){
            AppEventBus.INSTANCE.getEventBus().register(this);
        }
    }

    void checkStatus(@NonNull Observable<Response<ResponseBody>> observable,
                     @NonNull final CheckStatusCallback callback) {
        HttpSubscriber<ResponseBody> httpSubscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        callback.onChecked(response.isSuccessful());
                    }
                }
        );
        generalRxHttpExecute(observable, httpSubscriber);
    }

    interface CheckStatusCallback {
        void onChecked(boolean status);
    }


}
