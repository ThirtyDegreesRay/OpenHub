/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.mvp.contract;

import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:40:25
 */

public interface IRepositoryContract {

    interface View extends IBaseContract.View{
        void showRepo(Repository repo);
        void showBranchesAndTags(ArrayList<Branch> list, Branch curBranch);
        void invalidateOptionsMenu();
    }

    interface Presenter extends IBaseContract.Presenter<IRepositoryContract.View>{
        void loadBranchesAndTags();
        void starRepo(boolean star);
        void watchRepo(boolean watch);
        void createFork();
        boolean isForkEnable();
    }

}
