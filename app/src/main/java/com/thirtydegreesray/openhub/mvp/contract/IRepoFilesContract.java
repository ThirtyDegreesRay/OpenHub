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

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.model.FilePath;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 16:02:28
 */

public interface IRepoFilesContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showFiles(ArrayList<FileModel> files);
        void showFilePath(ArrayList<FilePath> filePath);
    }

    interface Presenter extends IBasePagerContract.Presenter<IRepoFilesContract.View>{
        void loadFiles(boolean isReload);
        void loadFiles(@NonNull String path, boolean isReload);
        boolean goBack();
        void goHome();
    }

}
