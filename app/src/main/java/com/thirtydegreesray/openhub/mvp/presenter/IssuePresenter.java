package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IIssuesContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.ui.fragment.IssuesFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:56:49
 */

public class IssuePresenter extends BasePresenter<IIssuesContract.View>
        implements IIssuesContract.Presenter {

    @AutoAccess IssuesFragment.IssueFragmentType type;
    @AutoAccess String userId;
    @AutoAccess String repoName;

    @AutoAccess ArrayList<Issue> issues;

    @Inject
    public IssuePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (issues == null) {
            loadIssues(1, false);
        } else {
            mView.showIssues(issues);
        }
    }

    @Override
    public void loadIssues(final int page, final boolean isReload) {

        mView.showLoading();
        final boolean readCacheFirst = page == 1 && !isReload;
        HttpObserver<ArrayList<Issue>> httpObserver =
                new HttpObserver<ArrayList<Issue>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<Issue>> response) {
                        mView.hideLoading();
                        if (isReload || issues == null || readCacheFirst) {
                            issues = response.body();
                        } else {
                            issues.addAll(response.body());
                        }
                        if (response.body().size() == 0 && issues.size() != 0) {
                            mView.setCanLoadMore(false);
                        } else {
                            mView.showIssues(issues);
                        }
                    }
                };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Issue>>() {
            @Override
            public Observable<Response<ArrayList<Issue>>> createObservable(boolean forceNetWork) {
                if (type.equals(IssuesFragment.IssueFragmentType.RepoOpen)) {
                    return getIssueService().getRepoIssues(forceNetWork, userId, repoName, "open", page);
                } else if (type.equals(IssuesFragment.IssueFragmentType.RepoClosed)) {
                    return getIssueService().getRepoIssues(forceNetWork, userId, repoName, "closed", page);
                } else {
                    throw new IllegalArgumentException(type.name());
                }
            }
        }, httpObserver, readCacheFirst);

    }

    private void handleError(Throwable error){
        if(!StringUtils.isBlankList(issues)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showIssues(new ArrayList<Issue>());
        }else{
            mView.showLoadError(getErrorTip(error));
        }
    }

}
