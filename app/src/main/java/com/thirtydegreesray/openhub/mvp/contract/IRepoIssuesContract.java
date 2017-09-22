package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 17:21:14
 */

public interface IRepoIssuesContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<IRepoIssuesContract.View>{

    }

}
