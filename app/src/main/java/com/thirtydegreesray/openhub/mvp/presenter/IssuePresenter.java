package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IIssuesContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.SearchResult;
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
        boolean readCacheFirst = page == 1 && !isReload;
        if(issuesFilter.getType().equals(IssuesFilter.Type.Repo)){
            loadRepoIssues(page, isReload, readCacheFirst);
        } else {
            loadUserIssues(page, isReload, readCacheFirst);
        }

    }

    @Override
    public void loadIssues(IssuesFilter issuesFilter, int page, boolean isReload) {
        this.issuesFilter = issuesFilter;
        setLoaded(false);
        prepareLoadData();
    }

    private void loadUserIssues(final int page, final boolean isReload, final boolean readCacheFirst){

//        HttpSubscriber<ResponseBody> subscriber = new HttpSubscriber<ResponseBody>(
//                new HttpObserver<ResponseBody>() {
//                    @Override
//                    public void onError(Throwable error) {
//                        error.toString();
//                    }
//
//                    @Override
//                    public void onSuccess(HttpResponse<ResponseBody> response) {
//                        response.body();
//                    }
//                }
//        );
//        generalRxHttpExecute(getSearchService().searchIssues(false, "state:open", "created", "desc", page), subscriber);

        mView.showLoading();
        HttpObserver<SearchResult<Issue>> httpObserver =
                new HttpObserver<SearchResult<Issue>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<SearchResult<Issue>> response) {
                        mView.hideLoading();
                        handleSuccess(response.body().getItems(), isReload, readCacheFirst);
                    }
                };
        final String sortStr = issuesFilter.getSortType().name().toLowerCase();
        final String sortDirectionStr = issuesFilter.getSortDirection().name().toLowerCase();
        final String queryStr = getUserQueryStr();

        generalRxHttpExecute(new IObservableCreator<SearchResult<Issue>>() {
            @Override
            public Observable<Response<SearchResult<Issue>>> createObservable(boolean forceNetWork) {
                return getSearchService().searchIssues(forceNetWork, queryStr, sortStr, sortDirectionStr, page);
            }
        }, httpObserver, readCacheFirst);
    }

    private void loadRepoIssues(final int page, final boolean isReload, final boolean readCacheFirst){
        mView.showLoading();
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
                        handleSuccess(response.body(), isReload, readCacheFirst);
                    }
                };
        final String statusStr = issuesFilter.getIssueState().name().toLowerCase();
        final String sortStr = issuesFilter.getSortType().name().toLowerCase();
        final String sortDirectionStr = issuesFilter.getSortDirection().name().toLowerCase();

        generalRxHttpExecute(new IObservableCreator<ArrayList<Issue>>() {
            @Override
            public Observable<Response<ArrayList<Issue>>> createObservable(boolean forceNetWork) {
                return getIssueService().getRepoIssues(forceNetWork, userId, repoName, statusStr,
                        sortStr, sortDirectionStr, page);
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

    private void handleSuccess(ArrayList<Issue> resultIssues, boolean isReload, boolean readCacheFirst){
        if (isReload || issues == null || readCacheFirst) {
            issues = resultIssues;
        } else {
            issues.addAll(resultIssues);
        }
        if (resultIssues.size() == 0 && issues.size() != 0) {
            mView.setCanLoadMore(false);
        } else {
            mView.showIssues(issues);
        }
    }

    public IssuesFilter getIssuesFilter() {
        return issuesFilter;
    }

    public String getUserQueryStr(){
        String queryStr = "";
        String action = "";
        switch (issuesFilter.getUserIssuesFilterType()){
            case All:
                action = "involves";
                break;
            case Created:
                action = "author";
                break;
            case Assigned:
                action = "assignee";
                break;
            case Mentioned:
                action = "mentions";
                break;
        }
        queryStr = queryStr + action + ":" + AppData.INSTANCE.getLoggedUser().getLogin()
                + "+" + "state:" + issuesFilter.getIssueState().name().toLowerCase();
        return queryStr;
    }

}
