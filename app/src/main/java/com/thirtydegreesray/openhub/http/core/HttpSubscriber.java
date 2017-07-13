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

package com.thirtydegreesray.openhub.http.core;

import android.os.Handler;

import com.thirtydegreesray.openhub.http.error.HttpError;
import com.thirtydegreesray.openhub.http.error.HttpErrorCode;
import com.thirtydegreesray.openhub.util.NetHelper;

import rx.Subscriber;

/**
 * 网络请求订阅者，处理网络请求的返回，判断数据有效性，同时处理网络请求异常情况<br>
 * Created by YuYunHao on 2016/7/15 11:19
 */
public class HttpSubscriber<T> extends Subscriber<T> {

    private HttpObserver<T> mObserver;
    private Handler mHandler;

    private final int ON_ERROR = 0;

    public HttpSubscriber() {
    }

    public HttpSubscriber(HttpObserver<T> observer) {
        mObserver = observer;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mObserver != null)
            mObserver.onError(e);
    }

    @Override
    public void onNext(T t) {
        if (mObserver != null)
            mObserver.onSuccess(t);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!NetHelper.getInstance().getNetEnabled()) {
            onError(new HttpError("网络未连接！", HttpErrorCode.NET_UNABLE));
            unsubscribe();
        }
    }
}
