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
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoriesContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class RepositoriesPresenter extends IRepositoriesContract.Presenter {

    private RepositoriesFragment.RepositoriesType mRepositoriesType;
    private String mLanguage;

    @Inject
    public RepositoriesPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void loadRepositories(@NonNull RepositoriesFragment.RepositoriesType repositoriesType,
                                 String language, boolean isReLoad) {
        mRepositoriesType = repositoriesType;
        mLanguage = language;
        if (repositoriesType.equals(RepositoriesFragment.RepositoriesType.TRENDING)) {
            mView.showRepositories(getLanguageRepTest(language));
        } else {
            loadRepositories(isReLoad);
        }

    }

    @NonNull
    private ArrayList<Repository> getLanguageRepTest(String language) {
        ArrayList<Repository> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Repository repository = new Repository();
            repository.setName(language + "-" + i);
            list.add(repository);
        }
        return list;
    }

    private void loadRepositories(boolean isReLoad) {
        mView.showLoadingView();

        boolean readCacheFirst = !isReLoad &&
                !mRepositoriesType.equals(RepositoriesFragment.RepositoriesType.EXPLORE);

        HttpObserver<ArrayList<Repository>> httpObserver = new HttpObserver<ArrayList<Repository>>() {
            @Override
            public void onError(@NonNull Throwable error) {
                mView.showLoadError(error.getMessage());
                mView.hideLoadingView();
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<ArrayList<Repository>> response) {
                mView.showRepositories(response.body());
                mView.hideLoadingView();
            }
        };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Repository>,
                Response<ArrayList<Repository>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Repository>>> createObservable(boolean forceNetWork) {
                return getObservable(forceNetWork);
            }
        }, httpObserver, readCacheFirst);

    }

    private Observable<Response<ArrayList<Repository>>> getObservable(boolean forceNetWork){
        String loginedUser = AppData.INSTANCE.getLoggedUser().getLogin();
        switch (mRepositoriesType){
            case OWNED:
                return getRepoService().getUserRepos(forceNetWork, "");
            case STARRED:
            case TRENDING:
            case EXPLORE:
                return getRepoService().getStarredRepos(forceNetWork, "");
            default:
                return null;
        }
    }


}
