

package com.thirtydegreesray.openhub.mvp.contract;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseListContract;
import com.thirtydegreesray.openhub.mvp.contract.base.IBasePagerContract;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 14:26:15
 */

public interface ICommitsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showCommits(ArrayList<RepoCommit> commits);
    }

    interface Presenter extends IBasePagerContract.Presenter<ICommitsContract.View>{
        void loadCommits(boolean isReload, int page);
    }

}
