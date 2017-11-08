

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.mvp.model.RepoCommitExt;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 11:14:32
 */

public interface ICommitDetailContract {

    interface View extends IBaseContract.View{
        void showCommit(RepoCommit commit);
        void showCommitInfo(RepoCommitExt commitExt);
        void showUserAvatar(String userAvatarUrl);
    }

    interface Presenter extends IBaseContract.Presenter<ICommitDetailContract.View>{
        void loadCommitInfo();
    }

}
