

package com.thirtydegreesray.openhub.mvp.contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        void loadHtmlSource(@NonNull String htmlSource);
    }

    interface Presenter extends IBaseContract.Presenter<IViewerContract.View>{
        void load(boolean isReload);
    }

}
