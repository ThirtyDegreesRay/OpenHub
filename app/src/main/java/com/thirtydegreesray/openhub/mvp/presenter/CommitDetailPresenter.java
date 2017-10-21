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

import android.os.Handler;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.ICommitDetailContract;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.mvp.model.RepoCommitExt;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 11:31:06
 */

public class CommitDetailPresenter extends BasePresenter<ICommitDetailContract.View>
        implements ICommitDetailContract.Presenter {

    @AutoAccess String user;
    @AutoAccess String repo;
    @AutoAccess RepoCommit commit;
    @AutoAccess String commitSha;
    @AutoAccess String userAvatarUrl;
    private RepoCommitExt commitExt;

    @Inject
    public CommitDetailPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if(commit != null) {
            mView.showCommit(commit);
            commitSha = commit.getSha();
        }else{
            mView.showUserAvatar(userAvatarUrl);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mView == null) return;
                loadCommitInfo();
            }
        }, 500);
    }

    @Override
    public void loadCommitInfo() {
        mView.showLoading();
        HttpObserver<RepoCommitExt> httpObserver = new HttpObserver<RepoCommitExt>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<RepoCommitExt> response) {
                mView.hideLoading();
                commitExt = response.body();
                mView.showCommitInfo(response.body());
            }
        };
        generalRxHttpExecute(new IObservableCreator<RepoCommitExt>() {
            @Override
            public Observable<Response<RepoCommitExt>> createObservable(boolean forceNetWork) {
                return getCommitService().getCommitInfo(forceNetWork, user, repo, commitSha);
            }
        }, httpObserver, true);
    }

    public RepoCommit getCommit() {
        return commit;
    }
}
