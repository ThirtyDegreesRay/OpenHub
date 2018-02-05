package com.thirtydegreesray.openhub.mvp.presenter;

import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.model.IssueRequestModel;
import com.thirtydegreesray.openhub.mvp.contract.IIssueDetailContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
import com.thirtydegreesray.openhub.mvp.model.request.CommentRequestModel;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.util.GitHubHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 16:53:52
 */

public class IssueDetailPresenter extends BasePresenter<IIssueDetailContract.View>
        implements IIssueDetailContract.Presenter {

    @AutoAccess Issue issue;
    @AutoAccess String issueUrl;

    @AutoAccess String owner;
    @AutoAccess String repoName;
    @AutoAccess int issueNumber;

    @Inject
    public IssueDetailPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        initIssueInfo();
        if (issue == null || issue.getBodyHtml() == null) {
            loadIssueInfo();
        } else {
            mView.showIssue(issue);
        }
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    private void loadIssueInfo(final String user, final String repo, final int issueNumber) {
        HttpObserver<Issue> httpObserver = new HttpObserver<Issue>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.hideLoading();
            }

            @Override
            public void onSuccess(HttpResponse<Issue> response) {
                issue = response.body();
                mView.showIssue(issue);
                mView.hideLoading();
            }
        };
        generalRxHttpExecute(new IObservableCreator<Issue>() {
            @Override
            public Observable<Response<Issue>> createObservable(boolean forceNetWork) {
                return getIssueService().getIssueInfo(forceNetWork, user, repo, issueNumber);
            }
        }, httpObserver, true);
        mView.showLoading();
    }

    private void initIssueInfo(){
        if (issue != null) {
            owner = issue.getRepoAuthorName();
            repoName = issue.getRepoName();
            issueNumber = issue.getNumber();
        } else if (!StringUtils.isBlank(issueUrl)) {
            issueUrl = issueUrl.replace("api.github.com/repos", "github.com");
            if (!GitHubHelper.isIssueUrl(issueUrl)) return;
            String[] arrays = issueUrl.substring(issueUrl.indexOf("com/") + 4).split("/");
            owner = arrays[0];
            repoName = arrays[1];
            issueNumber = Integer.parseInt(arrays[3]);
        }
    }

    private void loadIssueInfo() {
        loadIssueInfo(owner, repoName, issueNumber);
    }

    @Override
    public void addComment(@NonNull final String text) {
        HttpObserver<IssueEvent> httpObserver = new HttpObserver<IssueEvent>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.showAddCommentPage(text);
            }

            @Override
            public void onSuccess(HttpResponse<IssueEvent> response) {
                mView.showSuccessToast(getString(R.string.comment_success));
                IssueEvent comment = response.body();
                comment.setType(IssueEvent.Type.commented);
                mView.showAddedComment(comment);
            }
        };
        generalRxHttpExecute(new IObservableCreator<IssueEvent>() {
            @Override
            public Observable<Response<IssueEvent>> createObservable(boolean forceNetWork) {
                return getIssueService().addComment(issue.getRepoAuthorName(),
                        issue.getRepoName(), issue.getNumber(), new CommentRequestModel(text));
            }
        }, httpObserver, false, mView.getProgressDialog(getLoadTip()));
    }

    @Override
    public void toggleIssueState() {
        issue.setState(issue.getState().equals(Issue.IssueState.open) ?
                Issue.IssueState.closed : Issue.IssueState.open);
        HttpProgressSubscriber<Issue> subscriber = new HttpProgressSubscriber<>(
                mView.getProgressDialog(getLoadTip()),
                new HttpObserver<Issue>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<Issue> response) {
                        mView.showIssue(issue);
                        String tip = issue.getState().equals(Issue.IssueState.open) ?
                                getString(R.string.reopen_success) : getString(R.string.close_success);
                        mView.showSuccessToast(tip);
                    }
                }
        );
        generalRxHttpExecute(getIssueService().editIssue(issue.getRepoAuthorName(),
                issue.getRepoName(), issue.getNumber(),
                IssueRequestModel.generateFromIssue(issue)), subscriber);
    }

    public String getOwner() {
        return owner;
    }

    public String getRepoName() {
        return repoName;
    }
}
