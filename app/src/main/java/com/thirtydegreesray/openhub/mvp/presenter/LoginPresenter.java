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

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.AuthUser;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.model.AuthRequestModel;
import com.thirtydegreesray.openhub.mvp.contract.ILoginContract;
import com.thirtydegreesray.openhub.mvp.model.BasicToken;
import com.thirtydegreesray.openhub.mvp.model.OauthToken;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.Credentials;
import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class LoginPresenter extends BasePresenter<ILoginContract.View>
        implements ILoginContract.Presenter {

    @Inject
    public LoginPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getToken(String code, String state) {
        Observable<Response<OauthToken>> observable =
                getLoginService().getAccessToken(AppConfig.OPENHUB_CLIENT_ID,
                        AppConfig.OPENHUB_CLIENT_SECRET, code, state);

        HttpProgressSubscriber<OauthToken, Response<OauthToken>> subscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<OauthToken>() {
                            @Override
                            public void onError(@NonNull Throwable error) {
                                mView.showShortToast(error.getMessage());
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResponse<OauthToken> response) {
                                OauthToken token = response.body();
                                if(token != null){
                                    mView.onGetTokenSuccess(BasicToken.generateFromOauthToken(token));
                                }else{
                                    mView.onGetTokenError(response.getOriResponse().message());
                                }
                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber);
    }

    @NonNull
    @Override
    public String getOAuth2Url() {
        String randomState = UUID.randomUUID().toString();
        return AppConfig.OAUTH2_URL +
                "?client_id=" + AppConfig.OPENHUB_CLIENT_ID +
                "&scope=" + AppConfig.OAUTH2_SCOPE +
                "&state=" + randomState;
    }

    @Override
    public void basicLogin(String userName, String password) {
        AuthRequestModel authRequestModel = AuthRequestModel.generate();
        String token = Credentials.basic(userName, password);
        Observable<Response<BasicToken>> observable =
                getLoginService(token).authorizations(authRequestModel);
        HttpProgressSubscriber<BasicToken, Response<BasicToken>> subscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<BasicToken>() {
                            @Override
                            public void onError(@NonNull Throwable error) {
                                mView.onGetTokenError(error.getMessage());
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResponse<BasicToken> response) {
                                BasicToken token = response.body();
                                if(token != null){
                                    mView.onGetTokenSuccess(token);
                                } else {
                                    mView.onGetTokenError(response.getOriResponse().message());
                                }

                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber);
    }

    @Override
    public void handleOauth(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            getToken(code, state);
        }
    }

    @Override
    public void getUserInfo(final BasicToken basicToken) {

        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onError(@NonNull Throwable error) {
                mView.showShortToast(error.getMessage());
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                saveAuthUser(basicToken, response.body());
                mView.onLoginComplete();
            }
        };

        generalRxHttpExecute(new IObservableCreator<User, Response<User>>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return getUserService(basicToken.getToken()).getUser(forceNetWork, "");
            }
        }, httpObserver, false);

    }

    private void saveAuthUser(BasicToken basicToken, User userInfo){
        AuthUser authUser = new AuthUser();
        String scope = StringUtils.listToString(basicToken.getScopes(), ",");
        Date date = new Date();
        authUser.setAccessToken(basicToken.getToken());
        authUser.setScope(scope);
        authUser.setAuthTime(date);
        authUser.setExpireIn(360 * 24 * 60 * 60);
        authUser.setSelected(true);
        authUser.setLoginId(userInfo.getLogin());
        authUser.setName(userInfo.getName());
        authUser.setAvatar(userInfo.getAvatarUrl());
        daoSession.getAuthUserDao().insert(authUser);

        AppData.INSTANCE.setAuthUser(authUser);
        AppData.INSTANCE.setLoggedUser(userInfo);
    }


}
