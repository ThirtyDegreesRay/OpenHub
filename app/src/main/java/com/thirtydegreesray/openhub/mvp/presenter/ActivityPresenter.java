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

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IActivityContract;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:53:51
 */

public class ActivityPresenter extends BasePagerPresenter<IActivityContract.View>
        implements IActivityContract.Presenter{

    @AutoAccess ActivityFragment.ActivityType type ;
    @AutoAccess String user ;
    @AutoAccess String repo ;

    ArrayList<Event> events;

    @Inject
    public ActivityPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        if(events != null){
            mView.showEvents(events);
            mView.hideLoading();
            return;
        }
        loadEvents(false, 1);
    }

    @Override
    public void loadEvents(final boolean isReload, final int page) {
        mView.showLoading();
        final boolean readCacheFirst = !isReload && page == 1;
        HttpObserver<ArrayList<Event>> httpObserver = new HttpObserver<ArrayList<Event>>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if(!StringUtils.isBlankList(events)){
                    mView.showErrorToast(getErrorTip(error));
                } else if(error instanceof HttpPageNoFoundError){
                    mView.showEvents(new ArrayList<Event>());
                }else{
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Event>> response) {
                mView.hideLoading();
                correctEvent(response.body());
                if(events == null || isReload || readCacheFirst){
                    events = response.body();
                } else {
                    events.addAll(response.body());
                }
                if(response.body().size() == 0 && events.size() != 0){
                    mView.setCanLoadMore(false);
                } else {
                    mView.showEvents(events);
                }
            }
        };
        generalRxHttpExecute(new IObservableCreator<ArrayList<Event>>() {
            @Override
            public Observable<Response<ArrayList<Event>>> createObservable(boolean forceNetWork) {
                return getObservable(forceNetWork, page);
            }
        }, httpObserver, readCacheFirst);
    }

    private Observable<Response<ArrayList<Event>>> getObservable(boolean forceNetWork, int page){
        if(type.equals(ActivityFragment.ActivityType.News)){
            return getUserService().getNewsEvent(forceNetWork, user, page);
        } else if(type.equals(ActivityFragment.ActivityType.User)){
            return getUserService().getUserEvents(forceNetWork, user, page);
        } else if(type.equals(ActivityFragment.ActivityType.Repository)){
            return getRepoService().getRepoEvent(forceNetWork, user, repo, page);
        } else {
            return null;
        }
    }

    private void correctEvent(ArrayList<Event> events){
        for(Event event : events){
            if(event.getActor() != null) event.getActor().setType(User.UserType.User);
            if(event.getOrg() != null) event.getOrg().setType(User.UserType.Organization);
            if(event.getRepo() != null){
                String fullName = event.getRepo().getName();
                event.getRepo().setFullName(fullName);
                event.getRepo().setName(fullName.substring(fullName.indexOf("/") + 1));
            }
        }
    }

}

