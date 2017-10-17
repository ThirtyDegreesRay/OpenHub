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
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:42:47
 */

public class RepositoryPresenter extends BasePresenter<IRepositoryContract.View>
        implements IRepositoryContract.Presenter {

    @AutoAccess(dataName = "repository") Repository repository;

    @AutoAccess String owner;
    @AutoAccess String repoName;

    private ArrayList<Branch> branches;
    @AutoAccess Branch curBranch;
    private boolean starred;
    private boolean watched;

    private boolean isStatusChecked = false;

    @Inject
    public RepositoryPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (repository != null) {
            //from trending
            if(repository.getFullName() == null){
                owner = repository.getOwner().getLogin();
                repoName = repository.getName();
                getRepoInfo(true);
                return;
            }
            owner = repository.getOwner().getLogin();
            repoName = repository.getName();
            initCurBranch();
            mView.showRepo(repository);
            getRepoInfo(false);
            checkStatus();
        } else {
            getRepoInfo(true);
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
                                mView.showErrorToast(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(HttpResponse<ArrayList<Branch>> response) {
                                setTags(response.body());
                                branches.addAll(response.body());
                                mView.showBranchesAndTags(branches, curBranch);
                            }
                        }
                );
        Observable<Response<ArrayList<Branch>>> observable = getRepoService().getBranches(owner, repoName)
                .flatMap(new Func1<Response<ArrayList<Branch>>, Observable<Response<ArrayList<Branch>>>>() {
                    @Override
                    public Observable<Response<ArrayList<Branch>>> call(
                            Response<ArrayList<Branch>> arrayListResponse) {
                        branches = arrayListResponse.body();
                        return getRepoService().getTags(owner, repoName);
                    }
                });
        generalRxHttpExecute(observable, httpProgressSubscriber);
    }

    @Override
    public void starRepo(boolean star) {
        starred = star;
        Observable<Response<ResponseBody>> observable = starred ?
                getRepoService().starRepo(owner, repoName) :
                getRepoService().unstarRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void watchRepo(boolean watch) {
        watched = watch;
        Observable<Response<ResponseBody>> observable = watched ?
                getRepoService().watchRepo(owner, repoName) :
                getRepoService().unwatchRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void createFork() {
        mView.getProgressDialog(getLoadTip()).show();
        HttpObserver<Repository> httpObserver = new HttpObserver<Repository>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.getProgressDialog(getLoadTip()).dismiss();
            }

            @Override
            public void onSuccess(HttpResponse<Repository> response) {
                if(response.body() != null) {
                    mView.showSuccessToast(getString(R.string.forked));
                    RepositoryActivity.show(getContext(), response.body());
                } else {
                    mView.showErrorToast(getString(R.string.fork_failed));
                }
                mView.getProgressDialog(getLoadTip()).dismiss();
            }
        };
        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().createFork(owner, repoName);
            }
        }, httpObserver);
    }

    @Override
    public boolean isForkEnable() {
        if(repository != null && !repository.isFork() &&
                !repository.getOwner().getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin())){
            return true;
        }
        return false;
    }

    private void setTags(ArrayList<Branch> list) {
        for (Branch branch : list) {
            branch.setBranch(false);
        }
    }

    private void getRepoInfo(final boolean isShowLoading) {
        if (isShowLoading) mView.showLoading();
        HttpObserver<Repository> httpObserver =
                new HttpObserver<Repository>() {
                    @Override
                    public void onError(Throwable error) {
                        if (isShowLoading) mView.hideLoading();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<Repository> response) {
                        if (isShowLoading) mView.hideLoading();
                        repository = response.body();
                        initCurBranch();
                        mView.showRepo(repository);
                        checkStatus();
                    }
                };

        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().getRepoInfo(forceNetWork, owner, repoName);
            }
        }, httpObserver, true);
    }

    private void checkStatus(){
        if(isStatusChecked) return;
        isStatusChecked = true;
        checkStarred();
        checkWatched();
    }


    private void checkStarred() {
        checkStatus(
                getRepoService().checkRepoStarred(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        starred = status;
                        mView.invalidateOptionsMenu();
                    }
                }
        );
    }

    private void checkWatched() {
        checkStatus(
                getRepoService().checkRepoWatched(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        watched = status;
                        mView.invalidateOptionsMenu();
                    }
                }
        );
    }

    private void initCurBranch(){
        curBranch = new Branch(repository.getDefaultBranch());
        curBranch.setZipballUrl("https://github.com/".concat(owner).concat("/")
                .concat(repoName).concat("/archive/")
                .concat(curBranch.getName()).concat(".zip"));
        curBranch.setTarballUrl("https://github.com/".concat(owner).concat("/")
                .concat(repoName).concat("/archive/")
                .concat(curBranch.getName()).concat(".tar.gz"));
    }

    public Repository getRepository() {
        return repository;
    }

    public boolean isFork() {
        return repository != null && repository.isFork();
    }

    public boolean isStarred() {
        return starred;
    }

    public boolean isWatched() {
        return watched;
    }

    public String getZipSourceUrl(){
        return curBranch.getZipballUrl();
    }

    public String getZipSourceName(){
        return repoName.concat("-").concat(curBranch.getName()).concat(".zip");
    }

    public String getTarSourceUrl(){
        return curBranch.getTarballUrl();
    }

    public String getTarSourceName(){
        return repoName.concat("-").concat(curBranch.getName()).concat(".tar.gz");
    }

    public void setCurBranch(Branch curBranch) {
        this.curBranch = curBranch;
    }

    public String getRepoName() {
        return repository == null ? repoName : repository.getName();
    }

}
