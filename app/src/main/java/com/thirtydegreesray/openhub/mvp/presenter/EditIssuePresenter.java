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

import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IEditIssueContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/10/14 16:49:20
 */

public class EditIssuePresenter extends BasePresenter<IEditIssueContract.View>
        implements IEditIssueContract.Presenter{

    @AutoAccess String userId;
    @AutoAccess String repoName;
    @AutoAccess Issue issue;
    @AutoAccess boolean addMode;

    @Inject
    public EditIssuePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    public String getIssueTitle(){
        return issue == null ? null : issue.getTitle();
    }

    public String getIssueComment(){
        return issue == null ? null : issue.getBody();
    }

    @Override
    public void commitIssue(@NonNull String title, @NonNull String comment) {
        HttpObserver<Issue> httpObserver = new HttpObserver<Issue>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<Issue> response) {
                mView.showNewIssue(response.body());
            }
        };

        if(addMode){
            issue = new Issue();
        } else {
            userId = issue.getRepoAuthorName();
            repoName = issue.getRepoName();
        }
        issue.setTitle(title);
        issue.setBody(comment);

        generalRxHttpExecute(new IObservableCreator<Issue>() {
            @Override
            public Observable<Response<Issue>> createObservable(boolean forceNetWork) {
                return addMode ? getIssueService().createIssue(userId, repoName, issue) :
                        getIssueService().editIssue(userId, repoName, issue.getNumber(), issue);
            }
        }, httpObserver, false, mView.getProgressDialog(getLoadTip()));

    }

    public boolean isAddMode() {
        return addMode;
    }

}
