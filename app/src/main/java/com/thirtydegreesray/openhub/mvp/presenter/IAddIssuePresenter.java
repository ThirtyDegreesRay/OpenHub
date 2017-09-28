package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.IAddIssueContract;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 16:56:35
 */

public class IAddIssuePresenter extends BasePresenter<IAddIssueContract.View>
        implements IAddIssueContract.Presenter{

    @Inject
    public IAddIssuePresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
