

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:33:00
 */

public interface IRepoInfoContract {

    interface View extends IBaseContract.View, IBasePagerContract.View{
        void showRepoInfo(Repository repository);
        void showReadMe(String content, String baseUrl);
        void showReadMeLoader();
        void showNoReadMe();
    }

    interface Presenter extends IBasePagerContract.Presenter<IRepoInfoContract.View>{
        void loadReadMe();
    }

}
