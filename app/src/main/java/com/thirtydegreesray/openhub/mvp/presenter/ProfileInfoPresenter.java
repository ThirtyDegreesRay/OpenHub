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

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IProfileInfoContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 14:37:51
 */

public class ProfileInfoPresenter extends BasePagerPresenter<IProfileInfoContract.View>
        implements IProfileInfoContract.Presenter{

    @AutoAccess User user;
    private ArrayList<User> orgs;

    @Inject
    public ProfileInfoPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        mView.showProfileInfo(user);
        if(user.isUser()) loadOrgs();
    }

    public User getUser() {
        return user;
    }

    private void loadOrgs(){
        if(orgs != null && orgs.size() != 0){
            mView.showUserOrgs(orgs);
            return;
        }
        HttpObserver<ArrayList<User>> httpObserver = new HttpObserver<ArrayList<User>>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<User>> response) {
                if(response.body().size() != 0){
                    orgs = response.body();
                    mView.showUserOrgs(orgs);
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator<ArrayList<User>>() {
            @Override
            public Observable<Response<ArrayList<User>>> createObservable(boolean forceNetWork) {
                return getUserService().getUserOrgs(forceNetWork, user.getLogin());
            }
        }, httpObserver, true);
    }

}
