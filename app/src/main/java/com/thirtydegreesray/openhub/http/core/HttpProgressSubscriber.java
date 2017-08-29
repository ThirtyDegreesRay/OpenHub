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

package com.thirtydegreesray.openhub.http.core;

import android.app.AlertDialog;
import android.support.annotation.NonNull;

/**
 * 网络请求开始时显示dialog，结束取消dialog
 * Created by ThirtyDegreesRay on 2016/7/15 11:24
 */
public class HttpProgressSubscriber<T> extends HttpSubscriber<T> {

    /**
     * 网络请求dialog
     */
    private AlertDialog mDialog;

    public HttpProgressSubscriber(@NonNull AlertDialog dialog, @NonNull HttpObserver<T> observer) {
        super(observer);
        mDialog = dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUnsubscribed())
            mDialog.show();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        mDialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        mDialog.dismiss();
    }
}
