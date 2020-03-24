

package com.thirtydegreesray.openhub.mvp.presenter;

import androidx.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.model.IssueRequestModel;
import com.thirtydegreesray.openhub.mvp.contract.IEditIssueContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;

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

    private ArrayList<Label> allLabels;

    @Inject
    public EditIssuePresenter(DaoSession daoSession) {
        super(daoSession);
        issue = new Issue();
    }

    public String getIssueTitle(){
        return issue.getTitle();
    }

    public String getIssueComment(){
        return issue.getBody();
    }

    public ArrayList<Label> getLabels(){
        return issue.getLabels();
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

        issue.setTitle(title);
        issue.setBody(comment);

        generalRxHttpExecute(new IObservableCreator<Issue>() {
            @Override
            public Observable<Response<Issue>> createObservable(boolean forceNetWork) {
                return addMode ? getIssueService().createIssue(userId, repoName, issue) :
                        getIssueService().editIssue(getRepoOwner(), getRepoName(), issue.getNumber(),
                                IssueRequestModel.generateFromIssue(issue));
            }
        }, httpObserver, false, mView.getProgressDialog(getLoadTip()));

    }

    @Override
    public void loadLabels() {
        if(allLabels != null){
            mView.onLoadLabelsComplete(getAllLabelsWithSelectFlag());
            return;
        }

        HttpObserver<ArrayList<Label>> httpObserver = new HttpObserver<ArrayList<Label>>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Label>> response) {
                allLabels = response.body();
                mView.onLoadLabelsComplete(getAllLabelsWithSelectFlag());
            }
        };

        generalRxHttpExecute(forceNetWork ->
                        getIssueService().getRepoLabels(forceNetWork, getRepoOwner(), getRepoName()),
                httpObserver, false, mView.getProgressDialog(getLoadTip()));
    }

    @Override
    public void clearAllLabelsData() {
        allLabels = null;
    }

    private ArrayList<Label> getAllLabelsWithSelectFlag(){
        for(Label label : allLabels){
            if(issue.getLabels() == null || !issue.getLabels().contains(label)){
                label.setSelect(false);
            } else {
                label.setSelect(true);
            }
        }
        return allLabels;
    }

    public boolean isAddMode() {
        return addMode;
    }

    public String getRepoOwner() {
        return addMode ? userId : issue.getRepoAuthorName();
    }

    public String getRepoName() {
        return addMode ? repoName : issue.getRepoName();
    }

    public Issue getIssue() {
        return issue;
    }

}
