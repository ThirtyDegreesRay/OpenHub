

package com.thirtydegreesray.openhub.mvp.presenter;

import android.os.Handler;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.TraceUser;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IProfileContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import java.util.Date;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class ProfilePresenter extends BasePresenter<IProfileContract.View>
        implements IProfileContract.Presenter{

    @AutoAccess String loginId;
    @AutoAccess String userAvatar;
    private User user;
    private boolean following = false;

    private boolean isTransitionComplete = false;
    private boolean isWaitForTransition = false;
    @AutoAccess boolean isTraceSaved = false;

    private boolean isBookmarkQueried = false;
    private boolean bookmarked = false;

    @Inject
    public ProfilePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mView == null) return;
                isTransitionComplete = true;
                if(isWaitForTransition) mView.showProfileInfo(user);
                isWaitForTransition = false;
                getProfileInfo();
                checkFollowingStatus();
            }
        }, 500);
    }

    private void getProfileInfo(){
        mView.showLoading();
        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.hideLoading();
            }

            @Override
            public void onSuccess(HttpResponse<User> response) {
                user = response.body();
                mView.hideLoading();
                if(isTransitionComplete){
                    mView.showProfileInfo(user);
                } else {
                    isWaitForTransition = true;
                }
                saveTrace();
            }
        };
        generalRxHttpExecute(new IObservableCreator<User>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return getUserService().getUser(forceNetWork, loginId);
            }
        }, httpObserver, true);
    }

    public String getLoginId() {
        return loginId;
    }

    public String getUserAvatar() {
        return user != null ? user.getAvatarUrl() : userAvatar;
    }

    public User getUser() {
        return user;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isUser(){
        return user != null && user.isUser();
    }

    public boolean isMe(){
        return user != null && user.getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin());
    }

    private void checkFollowingStatus(){
        checkStatus(
                getUserService().checkFollowing(loginId),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        following = status;
                        mView.invalidateOptionsMenu();
                    }
                }
        );
    }

    @Override
    public void followUser(boolean follow) {
        following = follow;
        executeSimpleRequest(follow ?
                getUserService().followUser(loginId) : getUserService().unfollowUser(loginId));
    }

    @Override
    public boolean isBookmarked() {
        if(!isBookmarkQueried){
            bookmarked = daoSession.getBookMarkUserDao().load(loginId) != null;
            isBookmarkQueried = true;
        }
        return bookmarked;
    }

    @Override
    public void bookmark(boolean bookmark) {
        bookmarked = bookmark;
        if(bookmark){
            daoSession.getBookMarkUserDao().insert(user.toBookmarkUser());
        } else {
            daoSession.getBookMarkUserDao().deleteByKey(loginId);
        }
    }

    private void saveTrace(){
        TraceUser traceUser = daoSession.getTraceUserDao().load(user.getLogin());
        if(traceUser == null){
            traceUser = user.toTraceUser();
            daoSession.getTraceUserDao().insert(traceUser);
        } else {
            TraceUser updatedTraceUser = user.toTraceUser();
            updatedTraceUser.setStartTime(traceUser.getStartTime());
            updatedTraceUser.setLatestTime(new Date());
            updatedTraceUser.setTraceNum(isTraceSaved ? traceUser.getTraceNum() : traceUser.getTraceNum() + 1);
            daoSession.getTraceUserDao().update(updatedTraceUser);
        }
        isTraceSaved = true;
    }

}
