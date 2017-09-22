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
