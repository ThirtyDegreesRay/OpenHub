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

import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.mvp.contract.ILoginContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class LoginPresenter extends ILoginContract.Presenter {

    @Inject
    public LoginPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getToken(String code, String state) {
        Observable<ResponseBody> observable =
                getAPPSService().getAccessToken(AppConfig.OPENHUB_CLIENT_ID,
                        AppConfig.OPENHUB_CLIENT_SECRET, code, state);
        HttpProgressSubscriber<ResponseBody> subscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<ResponseBody>() {
                            @Override
                            public void onError(Throwable error) {
                                mView.showShortToast(error.getMessage());
                            }

                            @Override
                            public void onSuccess(ResponseBody responseBody) {
                                String jsonString = getStringFromBody(responseBody);
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    String accessToken = jsonObject.getString("access_token");
                                    mView.onGetTokenSuccess(accessToken);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber);
    }

    @Override
    public String getOAuth2Url() {
        String randomState = UUID.randomUUID().toString();
        return AppConfig.OAUTH2_URL +
                "?client_id=" + AppConfig.OPENHUB_CLIENT_ID +
                "&scope=" + AppConfig.AUTH_SCOPE +
                "&state=" + randomState;
    }
}
