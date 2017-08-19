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
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:42:47
 */

public class RepositoryPresenter extends BasePresenter<IRepositoryContract.View>
        implements IRepositoryContract.Presenter {

    @AutoAccess(dataName = "repository") Repository repository;
    @AutoAccess(dataName = "repoUrl") String repoUrl;

    private ArrayList<Branch> branches;
    private Branch curBranch;

    @Inject
    public RepositoryPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (repository != null) {
            curBranch = new Branch(repository.getDefaultBranch());
            mView.showRepo(repository);
            getRepoInfo(repository.getOwner().getLogin(), repository.getName(), false);
        } else {
            //TODO load by repo url
        }
    }

    @Override
    public void loadBranchesAndTags() {
        if (branches != null) {
            mView.showBranchesAndTags(branches, curBranch);
            return;
        }
        HttpProgressSubscriber<ArrayList<Branch>> httpProgressSubscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<ArrayList<Branch>>() {
                            @Override
                            public void onError(Throwable error) {
                                mView.showShortToast(error.getMessage());
                            }

                            @Override
                            public void onSuccess(HttpResponse<ArrayList<Branch>> response) {
                                setTags(response.body());
                                branches.addAll(response.body());
                                mView.showBranchesAndTags(branches, curBranch);
                            }
                        }
                );
        getRepoService().getBranches(repository.getOwner().getLogin(), repository.getName())
                .flatMap(new Func1<Response<ArrayList<Branch>>, Observable<Response<ArrayList<Branch>>>>() {
                    @Override
                    public Observable<Response<ArrayList<Branch>>> call(
                            Response<ArrayList<Branch>> arrayListResponse) {
                        branches = arrayListResponse.body();
                        return getRepoService().getTags(repository.getOwner().getLogin(),
                                repository.getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpProgressSubscriber);
    }

    private void setTags(ArrayList<Branch> list) {
        for (Branch branch : list) {
            branch.setBranch(false);
        }
    }

    private void getRepoInfo(final String owner, final String repoName, final boolean isShowLoading) {
        if(isShowLoading) mView.getProgressDialog(getLoadTip()).show();
        HttpObserver<Repository> httpObserver =
                new HttpObserver<Repository>() {
                    @Override
                    public void onError(Throwable error) {
                        if(isShowLoading) mView.getProgressDialog(getLoadTip()).cancel();
                        mView.showShortToast(error.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResponse<Repository> response) {
                        if(isShowLoading) mView.getProgressDialog(getLoadTip()).cancel();
                        repository = response.body();
                        mView.showRepo(repository);
                    }
                };

        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().getRepoInfo(forceNetWork, owner, repoName);
            }
        }, httpObserver, true);

    }


    public Repository getRepository() {
        return repository;
    }
}
