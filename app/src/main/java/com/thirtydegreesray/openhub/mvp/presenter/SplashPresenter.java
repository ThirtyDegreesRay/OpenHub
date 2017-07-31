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

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.AuthUser;
import com.thirtydegreesray.openhub.dao.AuthUserDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.ISplashContract;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class SplashPresenter extends ISplashContract.Presenter {

    private final String TAG = "SplashPresenter";

    private AuthUser authUser;

    @Inject
    public SplashPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getUser() {
        AuthUserDao authUserDao = daoSession.getAuthUserDao();

        List<AuthUser> users = authUserDao.queryBuilder()
                .where(AuthUserDao.Properties.Selected.eq(true))
                .limit(1)
                .list();

        AuthUser selectedUser = users != null && users.size() > 0 ? users.get(0) : null;

        if (selectedUser != null && selectedUser.isExpired()) {
            authUserDao.delete(selectedUser);
            selectedUser = null;
        }

        if (selectedUser != null) {
            AppData.INSTANCE.setAuthUser(selectedUser);
            getUserInfo(selectedUser.getAccessToken());
        } else {
            mView.showLoginPage();
        }

    }

    @Override
    public void saveAccessToken(String accessToken, String scope, int expireIn) {
        AuthUser authUser = new AuthUser();
        authUser.setSelected(true);
        authUser.setScope(scope);
        authUser.setExpireIn(expireIn);
        authUser.setAuthTime(new Date());
        authUser.setAccessToken(accessToken);
        daoSession.getAuthUserDao().insert(authUser);
        this.authUser = authUser;
    }

    private void getUserInfo(final String accessToken) {

        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onError(@NonNull Throwable error) {
                mView.showShortToast(error.getMessage());
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                AppData.INSTANCE.setLoginedUser(response.body());
                if (authUser != null) {
                    authUser.setLoginId(response.body().getLogin());
                    daoSession.getAuthUserDao().update(authUser);
                }
                mView.showMainPage();
            }
        };

        generalRxHttpExecute(new IObservableCreator<User, Response<User>>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return getAPPSService().getUser(forceNetWork, "");
            }
        }, httpObserver, false);

    }

}
