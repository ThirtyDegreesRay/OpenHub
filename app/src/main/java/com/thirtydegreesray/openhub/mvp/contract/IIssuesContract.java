package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.model.Issue;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:55:29
 */

public interface IIssuesContract {

    interface View extends IBaseContract.View{
        void showIssues(ArrayList<Issue> issues);
        void setCanLoadMore(boolean canLoadMore);
        void showLoadError(String error);
    }

    interface Presenter extends IBaseContract.Presenter<IIssuesContract.View>{
        void loadIssues(int page, boolean isReload);
    }

}
