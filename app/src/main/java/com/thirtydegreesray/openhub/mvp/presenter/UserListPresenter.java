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

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IUserListContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/16 17:38:43
 */

public class UserListPresenter extends BasePresenter<IUserListContract.View>
        implements IUserListContract.Presenter{

    @AutoAccess UserListFragment.UserListType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    private ArrayList<User> users;

    @Inject
    public UserListPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void loadUsers(final int page, final boolean isReload) {
        mView.showLoading();
        final boolean readCacheFirst = page == 1 && !isReload ;
        HttpObserver<ArrayList<User>> httpObserver =
                new HttpObserver<ArrayList<User>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        mView.showShortToast(error.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<User>> response) {
                        mView.hideLoading();
                        if(isReload || users == null || readCacheFirst){
                            users = response.body();
                        } else {
                            users.addAll(response.body());
                        }
                        mView.showUsers(users);
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ArrayList<User>>() {
            @Override
            public Observable<Response<ArrayList<User>>> createObservable(boolean forceNetWork) {
                if(type.equals(UserListFragment.UserListType.STARGAZERS)){
                    return getRepoService().getStargazers(forceNetWork, user, repo, page);
                }else if(type.equals(UserListFragment.UserListType.WATCHERS)){
                    return getRepoService().getWatchers(forceNetWork, user, repo, page);
                }else if(type.equals(UserListFragment.UserListType.FOLLOWERS)){
                    return getUserService().getFollowers(forceNetWork, user, page);
                }else if(type.equals(UserListFragment.UserListType.FOLLOWING)){
                    return getUserService().getFollowing(forceNetWork, user, page);
                }else{
                    throw new IllegalArgumentException(type.name());
                }
            }
        }, httpObserver, readCacheFirst);
    }
}
