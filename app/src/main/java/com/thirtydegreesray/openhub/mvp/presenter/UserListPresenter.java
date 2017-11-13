

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.common.Event;
import com.thirtydegreesray.openhub.dao.BookMarkUser;
import com.thirtydegreesray.openhub.dao.BookMarkUserDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.TraceUser;
import com.thirtydegreesray.openhub.dao.TraceUserDao;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IUserListContract;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;
import com.thirtydegreesray.openhub.mvp.model.SearchResult;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/16 17:38:43
 */

public class UserListPresenter extends BasePagerPresenter<IUserListContract.View>
        implements IUserListContract.Presenter {

    @AutoAccess UserListFragment.UserListType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    @AutoAccess SearchModel searchModel;

    private ArrayList<User> users;

    @Inject
    public UserListPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (type.equals(UserListFragment.UserListType.SEARCH)) {
            setEventSubscriber(true);
        }
    }

    @Override
    protected void loadData() {
        if(UserListFragment.UserListType.SEARCH.equals(type)){
            if(searchModel != null) searchUsers(1);
        } else if(UserListFragment.UserListType.TRACE.equals(type)){
            loadTrace(1);
        } else if(UserListFragment.UserListType.BOOKMARK.equals(type)){
            loadBookmarks(1);
        } else {
            loadUsers(1, false);
        }
    }

    @Override
    public void loadUsers(final int page, final boolean isReload) {
        if (type.equals(UserListFragment.UserListType.SEARCH)) {
            searchUsers(page);
            return;
        }
        if(UserListFragment.UserListType.TRACE.equals(type)){
            loadTrace(page);
            return;
        }
        if(UserListFragment.UserListType.BOOKMARK.equals(type)){
            loadBookmarks(page);
            return;
        }
        mView.showLoading();
        final boolean readCacheFirst = page == 1 && !isReload;
        HttpObserver<ArrayList<User>> httpObserver =
                new HttpObserver<ArrayList<User>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<User>> response) {
                        mView.hideLoading();
                        if (isReload || users == null || readCacheFirst) {
                            users = response.body();
                        } else {
                            users.addAll(response.body());
                        }
                        if(response.body().size() == 0 && users.size() != 0){
                            mView.setCanLoadMore(false);
                        } else {
                            mView.showUsers(users);
                        }
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ArrayList<User>>() {
            @Override
            public Observable<Response<ArrayList<User>>> createObservable(boolean forceNetWork) {
                if (type.equals(UserListFragment.UserListType.STARGAZERS)) {
                    return getRepoService().getStargazers(forceNetWork, user, repo, page);
                } else if (type.equals(UserListFragment.UserListType.WATCHERS)) {
                    return getRepoService().getWatchers(forceNetWork, user, repo, page);
                } else if (type.equals(UserListFragment.UserListType.FOLLOWERS)) {
                    return getUserService().getFollowers(forceNetWork, user, page);
                } else if (type.equals(UserListFragment.UserListType.FOLLOWING)) {
                    return getUserService().getFollowing(forceNetWork, user, page);
                } else if (type.equals(UserListFragment.UserListType.ORG_MEMBERS)) {
                    return getUserService().getOrgMembers(forceNetWork, user, page);
                } else {
                    throw new IllegalArgumentException(type.name());
                }
            }
        }, httpObserver, readCacheFirst);
    }

    private void searchUsers(final int page) {
        mView.showLoading();
        HttpObserver<SearchResult<User>> httpObserver =
                new HttpObserver<SearchResult<User>>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(HttpResponse<SearchResult<User>> response) {
                        mView.hideLoading();
                        if (users == null || page == 1) {
                            users = response.body().getItems();
                        } else {
                            users.addAll(response.body().getItems());
                        }
                        if(response.body().getItems().size() == 0 && users.size() != 0){
                            mView.setCanLoadMore(false);
                        } else {
                            mView.showUsers(users);
                        }
                    }
                };
        generalRxHttpExecute(new IObservableCreator<SearchResult<User>>() {
            @Override
            public Observable<Response<SearchResult<User>>> createObservable(boolean forceNetWork) {
                return getSearchService().searchUsers(searchModel.getQuery(), searchModel.getSort(),
                        searchModel.getOrder(), page);
            }
        }, httpObserver);
    }

    @Subscribe
    public void onSearchEvent(Event.SearchEvent searchEvent) {
        if (!searchEvent.searchModel.getType().equals(SearchModel.SearchType.User)) return;
        setLoaded(false);
        this.searchModel = searchEvent.searchModel;
        prepareLoadData();
    }

    private void handleError(Throwable error){
        if(!StringUtils.isBlankList(users)){
            mView.showErrorToast(getErrorTip(error));
        } else if(error instanceof HttpPageNoFoundError){
            mView.showUsers(new ArrayList<User>());
        } else {
            mView.showLoadError(getErrorTip(error));
        }
    }

    private void loadTrace(int page){
        List<TraceUser> traceUsers = daoSession.getTraceUserDao().queryBuilder()
                .orderDesc(TraceUserDao.Properties.LatestTime)
                .offset((page - 1) * 30)
                .limit(page * 30)
                .list();
        ArrayList<User> queryUsers = new ArrayList<>();
        for(TraceUser traceUser : traceUsers){
            queryUsers.add(User.generateFromTrace(traceUser));
        }
        showQueryUsers(queryUsers, page);
    }

    private void loadBookmarks(int page){
        List<BookMarkUser> bookMarkUsers = daoSession.getBookMarkUserDao().queryBuilder()
                .orderDesc(BookMarkUserDao.Properties.MarkTime)
                .offset((page - 1) * 30)
                .limit(page * 30)
                .list();
        ArrayList<User> queryUsers = new ArrayList<>();
        for(BookMarkUser bookMarkUser : bookMarkUsers){
            queryUsers.add(User.generateFromBookmark(bookMarkUser));
        }
        showQueryUsers(queryUsers, page);
    }

    private void showQueryUsers(ArrayList<User> queryUsers, int page){
        if(users == null || page == 1){
            users = queryUsers;
        } else {
            users.addAll(queryUsers);
        }

        mView.showUsers(users);
        mView.hideLoading();
    }

}
