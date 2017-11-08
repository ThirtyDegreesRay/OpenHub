

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 14:33:59
 */

public interface IProfileInfoContract {

    interface View extends IBaseContract.View, IBasePagerContract.View{
        void showProfileInfo(User user);
        void showUserOrgs(ArrayList<User> orgs);
    }

    interface Presenter extends IBasePagerContract.Presenter<IProfileInfoContract.View>{

    }

}
