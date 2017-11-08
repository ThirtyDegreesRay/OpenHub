

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.ITrendingContract;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class TrendingPresenter extends BasePresenter<ITrendingContract.View>
        implements ITrendingContract.Presenter{

    @Inject
    public TrendingPresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
