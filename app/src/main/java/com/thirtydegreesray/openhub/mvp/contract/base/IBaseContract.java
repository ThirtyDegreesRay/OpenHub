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

package com.thirtydegreesray.openhub.mvp.contract.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 9:56:42
 */

public interface IBaseContract {

    interface View {

        void showProgressDialog(String content);

        void dismissProgressDialog();

        ProgressDialog getProgressDialog(String content);

        void showTipDialog(String content);

        void showConfirmDialog(String msn, String title, String confirmText
                , DialogInterface.OnClickListener confirmListener);

        void showToast(String message);

        void showInfoToast(String message);

        void showSuccessToast(String message);

        void showErrorToast(String message);

        void showWarningToast(String message);

        void showLoading();

        void hideLoading();

        void showLoginPage();

    }

    interface Presenter<V extends IBaseContract.View>{

        void onSaveInstanceState(Bundle outState);

        void onRestoreInstanceState(Bundle outState);

        void attachView(@NonNull V view);

        void detachView();

        /**
         * view initialized, you can init view data
         */
        void onViewInitialized();

        @Nullable Context getContext();
    }

}
