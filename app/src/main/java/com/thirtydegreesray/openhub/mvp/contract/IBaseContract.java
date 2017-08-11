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

package com.thirtydegreesray.openhub.mvp.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 9:56:42
 */

public interface IBaseContract {

    interface View {

        /**
         * 显示dialog
         * @param content
         */
        void showProgressDialog(String content);

        /**
         * 取消dailog
         */
        void dismissProgressDialog();

        /**
         * 获取View的dialog
         * @param content
         * @return
         */
        ProgressDialog getProgressDialog(String content);

        /**
         * 显示Toast
         * @param message
         */
        void showShortToast(String message);

        /**
         * 显示LongToast
         * @param message
         */
        void showLongToast(String message);

        /**
         * 显示tip dialog
         * @param content
         */
        void showTipDialog(String content);

        void showConfirmDialog(String msn, String title, String confirmText
                , DialogInterface.OnClickListener confirmListener);
    }

    interface Presenter<V extends IBaseContract.View>{

        void attachView(@NonNull V view);

        void detachView();

        @Nullable Context getContext();
    }

}
