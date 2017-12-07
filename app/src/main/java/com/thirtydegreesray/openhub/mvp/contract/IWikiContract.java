package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.model.WikiModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 16:34:22
 */

public interface IWikiContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showWiki(ArrayList<WikiModel> wikiList);
    }

    interface Presenter extends IBaseContract.Presenter<IWikiContract.View>{
        void loadWiki(boolean isReload);
    }

}
