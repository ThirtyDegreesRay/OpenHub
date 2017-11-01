package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IReleaseInfoContract;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 13:11:46
 */

public class ReleaseInfoPresenter extends BasePresenter<IReleaseInfoContract.View>
        implements IReleaseInfoContract.Presenter{

    @AutoAccess String owner;
    @AutoAccess String repoName;
    @AutoAccess String tagName;
    @AutoAccess Release release;

    @Inject
    public ReleaseInfoPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if(release != null){
            mView.showReleaseInfo(release);
        } else {
            loadReleaseInfo();
        }
    }

    private void loadReleaseInfo(){
        mView.showLoading();
        HttpObserver<Release> httpObserver = new HttpObserver<Release>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.hideLoading();
            }

            @Override
            public void onSuccess(HttpResponse<Release> response) {
                release = response.body();
                mView.showReleaseInfo(release);
                mView.hideLoading();
            }
        };
        generalRxHttpExecute(new IObservableCreator<Release>() {
            @Override
            public Observable<Response<Release>> createObservable(boolean forceNetWork) {
                return getRepoService().getReleaseByTagName(forceNetWork, owner, repoName, tagName);
            }
        }, httpObserver, true);
    }

    public String getTagName(){
        return  release == null ? tagName : release.getTagName();
    }

    public String getRepoName() {
        return repoName;
    }

    public Release getRelease(){
        return release;
    }

    public String getOwner() {
        return owner;
    }
}
