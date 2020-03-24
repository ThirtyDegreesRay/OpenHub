

package com.thirtydegreesray.openhub.mvp.contract;

import androidx.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.ActivityRedirectionModel;
import com.thirtydegreesray.openhub.mvp.model.Event;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:51:44
 */

public interface IActivityContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showEvents(ArrayList<Event> events);
    }

    interface Presenter extends IBasePagerContract.Presenter<IActivityContract.View>{
        void loadEvents(boolean isReload, int page);
        ArrayList<ActivityRedirectionModel> getRedirectionList(@NonNull Event event);
    }

}
