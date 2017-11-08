

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.CommitFilesPathModel;
import com.thirtydegreesray.openhub.ui.adapter.base.DoubleTypesModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 15:21:28
 */

public interface ICommitFilesContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ICommitFilesContract.View>{
        ArrayList<DoubleTypesModel<CommitFilesPathModel, CommitFile>> getSortedList(ArrayList<CommitFile> commitFiles);
    }

}
