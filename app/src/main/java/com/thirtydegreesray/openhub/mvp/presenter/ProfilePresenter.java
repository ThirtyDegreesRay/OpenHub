/*
 *    Copyright 2017 ThirtyDegressRay
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

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.IProfileContract;

import javax.inject.Inject;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class ProfilePresenter extends IProfileContract.Presenter {

    @Inject
    public ProfilePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void loadContent(String name) {
//        mView.showContent(getContent(name));
        mView.showContent(name);
    }

    private String getContent(String name){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < 200; i++){
            stringBuffer.append(name).append("-").append(i).append(" ");
        }
        return stringBuffer.toString();
    }

}
