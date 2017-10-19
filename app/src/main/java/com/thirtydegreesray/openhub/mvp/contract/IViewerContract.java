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

package com.thirtydegreesray.openhub.mvp.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:57:16
 */

public interface IViewerContract {

    interface View extends IBaseContract.View{
        void loadImageUrl(@NonNull String url);
        void loadMdText(@NonNull String text, @Nullable String baseUrl);
        void loadCode(@NonNull String text, @Nullable String extension);
        void loadDiffFile(@NonNull String text);
    }

    interface Presenter extends IBaseContract.Presenter<IViewerContract.View>{
        void load(boolean isReload);
    }

}
