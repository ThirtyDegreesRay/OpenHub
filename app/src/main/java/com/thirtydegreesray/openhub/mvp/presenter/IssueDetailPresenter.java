package com.thirtydegreesray.openhub.mvp.presenter;

import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IIssueDetailContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
import com.thirtydegreesray.openhub.mvp.model.request.CommentRequestModel;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 16:53:52
 */

public class IssueDetailPresenter extends BasePresenter<IIssueDetailContract.View>
        implements IIssueDetailContract.Presenter{

    @AutoAccess Issue issue;

    @Inject
    public IssueDetailPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        mView.showIssue(issue);
    }

    public Issue getIssue() {
        return issue;
    }

    @Override
    public void addComment(@NonNull final String text) {
        HttpObserver<IssueEvent> httpObserver
                = new HttpObserver<IssueEvent>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<IssueEvent> response) {
                mView.showSuccessToast(getString(R.string.comment_success));
                mView.showAddedComment(response.body());
            }
        };
        generalRxHttpExecute(new IObservableCreator<IssueEvent>() {
            @Override
            public Observable<Response<IssueEvent>> createObservable(boolean forceNetWork) {
                return getIssueService().addComment(issue.getUser().getLogin(),
                        issue.getRepoName(), issue.getNumber(), new CommentRequestModel(text));
            }
        }, httpObserver);
    }
}
