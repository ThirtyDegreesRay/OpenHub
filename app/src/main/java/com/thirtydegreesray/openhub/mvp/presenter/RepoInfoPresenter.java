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

import android.os.Bundle;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.common.Event;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IRepoInfoContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.util.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:34:31
 */

public class RepoInfoPresenter extends BasePagerPresenter<IRepoInfoContract.View>
        implements IRepoInfoContract.Presenter{

    @AutoAccess Repository repository;
    @AutoAccess String readmeSource;

    @Inject
    public RepoInfoPresenter(DaoSession daoSession) {
        super(daoSession);
        setEventSubscriber(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        checkReadmeSourceSize();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        mView.showRepoInfo(repository);
    }

    @Override
    public void loadReadMe() {
        final String readmeFileUrl = AppConfig.GITHUB_API_BASE_URL + "repos/" + repository.getFullName()
                + "/" + "readme";
        final String baseUrl = AppConfig.GITHUB_BASE_URL + repository.getFullName();

        if(!StringUtils.isBlank(readmeSource)){
            mView.showReadMe(readmeSource, baseUrl);
            return;
        }

        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    readmeSource = response.body().string();
                    mView.showReadMe(readmeSource, baseUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return getRepoService().getFileAsHtmlStream(forceNetWork, readmeFileUrl);
            }
        }, httpObserver, true);

    }

    public Repository getRepository() {
        return repository;
    }

    @Subscribe
    public void onRepoInfoUpdated(Event.RepoInfoUpdatedEvent event){
        if(!this.repository.getFullName().equals(event.repository.getFullName())) return;
        this.repository = event.repository;
        setLoaded(false);
        prepareLoadData();
    }

    /**
     * check if the string size is too large to save
     */
    private void checkReadmeSourceSize(){
        if(readmeSource != null && readmeSource.getBytes().length > 128 * 1024){
            readmeSource = null;
        }
    }

}
