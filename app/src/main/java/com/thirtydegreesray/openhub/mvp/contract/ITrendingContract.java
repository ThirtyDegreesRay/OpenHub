

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public interface ITrendingContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ITrendingContract.View>{

    }

}
