package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IIssuesContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.filter.IssuesFilter;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:56:49
 */

public class IssuePresenter extends BasePagerPresenter<IIssuesContract.View>
        implements IIssuesContract.Presenter {

    @AutoAccess IssuesFilter issuesFilter;
    @AutoAccess String userId;
    @AutoAccess String repoName;

    private ArrayList<Issue> issues;

    @Inject
    public IssuePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        loadIssues(1, false);
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
                return getObservable(forceNetWork, page);
            }
        }, httpObserver, readCacheFirst);

    }

    @Override
    public void loadIssues(IssuesFilter issuesFilter, int page, boolean isReload) {
        this.issuesFilter = issuesFilter;
        setLoaded(false);
        prepareLoadData();
    }

    private Observable<Response<ArrayList<Issue>>> getObservable(boolean forceNetWork, int page){
        String statusStr = issuesFilter.getIssueState().name().toLowerCase();
        String filterStr = issuesFilter.getUserIssuesFilterType() == null ?
                null : issuesFilter.getUserIssuesFilterType().name().toLowerCase();
        String sortStr = issuesFilter.getSortType().name().toLowerCase();
        String sortDirectionStr = issuesFilter.getSortDirection().name().toLowerCase();
        if (IssuesFilter.Type.Repo.equals(issuesFilter.getType())) {
            return getIssueService().getRepoIssues(forceNetWork, userId, repoName, statusStr,
                    sortStr, sortDirectionStr, page);
        } else if (IssuesFilter.Type.User.equals(issuesFilter.getType())) {
            return getIssueService().getUserIssues(forceNetWork, filterStr, statusStr,
                    sortStr, sortDirectionStr,  page);
        } else {
            throw new IllegalArgumentException(issuesFilter.getType() + "");
        }
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

    public IssuesFilter getIssuesFilter() {
        return issuesFilter;
    }
}
