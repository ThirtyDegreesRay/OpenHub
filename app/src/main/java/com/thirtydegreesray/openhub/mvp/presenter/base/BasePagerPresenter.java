package com.thirtydegreesray.openhub.mvp.presenter.base;

import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;

/**
 * Created by ThirtyDegreesRay on 2017/9/22 13:09:23
 */

public abstract class BasePagerPresenter<V extends IBasePagerContract.View> extends BasePresenter<V>
        implements IBasePagerContract.Presenter<V>{

    private boolean isLoaded = false;

    public BasePagerPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        prepareLoadData();
    }

    @Override
    public void prepareLoadData() {
        if(mView == null) {
            return;
        }
        if(mView.isPagerFragment() && (!isViewInitialized() || !mView.isFragmentShowed())){
            return;
        }
        if(isLoaded) return;
        isLoaded = true;
        loadData();
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    protected abstract void loadData();

}
