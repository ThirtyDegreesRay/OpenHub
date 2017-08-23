/*
 *    Copyright 2017 ThirtyDegressRay
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

import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IActivityContract;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:53:51
 */

public class ActivityPresenter extends BasePresenter<IActivityContract.View>
        implements IActivityContract.Presenter{

    @Inject
    public ActivityPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadActivities();
    }

    private void loadActivities(){
        HttpObserver<ArrayList<Event>> httpObserver = new HttpObserver<ArrayList<Event>>() {
            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Event>> response) {
                correctEvent(response.body());
            }
        };
        generalRxHttpExecute(new IObservableCreator<ArrayList<Event>>() {
            @Override
            public Observable<Response<ArrayList<Event>>> createObservable(boolean forceNetWork) {
                return getUserService().getUserEvents(forceNetWork, "ThirtyDegreesRay", 1);
            }
        }, httpObserver, true);
    }

    private void correctEvent(ArrayList<Event> events){
        for(Event event : events){
            if(event.getActor() != null) event.getActor().setType(User.UserType.User.name());
            if(event.getOrg() != null) event.getOrg().setType(User.UserType.User.name());
            if(event.getRepo() != null){
                String fullName = event.getRepo().getName();
                event.getRepo().setFullName(fullName);
                event.getRepo().setName(fullName.substring(fullName.indexOf("/") + 1));
            }
        }
    }
}

