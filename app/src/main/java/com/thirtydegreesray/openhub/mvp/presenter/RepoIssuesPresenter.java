package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.IRepoIssuesContract;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 17:22:16
 */

public class RepoIssuesPresenter extends BasePresenter<IRepoIssuesContract.View>
        implements IRepoIssuesContract.Presenter{

    @Inject
    public RepoIssuesPresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
