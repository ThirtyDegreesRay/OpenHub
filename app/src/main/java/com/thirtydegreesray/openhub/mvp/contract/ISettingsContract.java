

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public interface ISettingsContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ISettingsContract.View>{

        void logout();

    }

}
