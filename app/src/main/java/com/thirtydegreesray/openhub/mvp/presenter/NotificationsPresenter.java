

package com.thirtydegreesray.openhub.mvp.presenter;

import androidx.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.INotificationsContract;
import com.thirtydegreesray.openhub.mvp.model.Notification;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.request.MarkNotificationReadRequestModel;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.ui.adapter.base.DoubleTypesModel;
import com.thirtydegreesray.openhub.ui.fragment.NotificationsFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:52:55
 */

public class NotificationsPresenter extends BasePagerPresenter<INotificationsContract.View>
        implements INotificationsContract.Presenter {

    @AutoAccess NotificationsFragment.NotificationsType type;
    private ArrayList<Notification> notifications;
    private ArrayList<DoubleTypesModel<Repository, Notification>> sortedNotifications;

    @Inject
    public NotificationsPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    protected void loadData() {
        loadNotifications(1, false);
    }

    @Override
    public void loadNotifications(final int page, boolean isReload) {
        mView.showLoading();
        final boolean readCacheFirst = page == 1 && !isReload;

        HttpObserver<ArrayList<Notification>> httpObserver = new HttpObserver<ArrayList<Notification>>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if (!StringUtils.isBlankList(notifications)) {
                    mView.showErrorToast(getErrorTip(error));
                } else {
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Notification>> response) {
                mView.hideLoading();
                if (notifications == null || page == 1) {
                    notifications = response.body();
                } else {
                    notifications.addAll(response.body());
                }
                if (response.body().size() == 0 && notifications.size() != 0) {
                    mView.setCanLoadMore(false);
                } else {
                    sortedNotifications = sortNotifications(notifications);
                    mView.showNotifications(sortedNotifications);
                }
            }
        };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Notification>>() {
            @Override
            public Observable<Response<ArrayList<Notification>>> createObservable(boolean forceNetWork) {
                if (NotificationsFragment.NotificationsType.Unread.equals(type)) {
                    return getNotificationsService().getMyNotifications(forceNetWork, false, false);
                } else if (NotificationsFragment.NotificationsType.Participating.equals(type)) {
                    return getNotificationsService().getMyNotifications(forceNetWork, true, true);
                } else if (NotificationsFragment.NotificationsType.All.equals(type)) {
                    return getNotificationsService().getMyNotifications(forceNetWork, true, false);
                } else {
                    return null;
                }
            }
        }, httpObserver, readCacheFirst);

    }

    @Override
    public void markNotificationAsRead(String threadId) {
        generalRxHttpExecute(getNotificationsService().markNotificationAsRead(threadId), null);
    }

    @Override
    public void markAllNotificationsAsRead() {
        generalRxHttpExecute(getNotificationsService().markAllNotificationsAsRead(
                MarkNotificationReadRequestModel.newInstance()), null);

        for(DoubleTypesModel<Repository, Notification> model : sortedNotifications){
            if(model.getM2() != null){
                model.getM2().setUnread(false);
            }
        }
        mView.showNotifications(sortedNotifications);
    }

    @Override
    public boolean isNotificationsAllRead() {
        if(notifications == null){
            return true;
        }
        for(DoubleTypesModel<Repository, Notification> model : sortedNotifications){
            if(model.getM2() != null && model.getM2().isUnread()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void markRepoNotificationsAsRead(@NonNull Repository repository) {
        generalRxHttpExecute(getNotificationsService().markRepoNotificationsAsRead(
                MarkNotificationReadRequestModel.newInstance(),
                repository.getOwner().getLogin(), repository.getName()), null);

        for(DoubleTypesModel<Repository, Notification> model : sortedNotifications){
            if(model.getM2() != null && model.getM2().getRepository().getId() == repository.getId()){
                model.getM2().setUnread(false);
            }
        }
        mView.showNotifications(sortedNotifications);
    }

    private ArrayList<DoubleTypesModel<Repository, Notification>> sortNotifications(
            ArrayList<Notification> notifications) {

        ArrayList<DoubleTypesModel<Repository, Notification>> sortedList = new ArrayList<>();
        Map<String, ArrayList<Notification>> sortedMap = new LinkedHashMap<>();
        for (Notification notification : notifications) {
            ArrayList<Notification> list = sortedMap.get(notification.getRepository().getFullName());
            if (list == null) {
                list = new ArrayList<>();
                sortedMap.put(notification.getRepository().getFullName(), list);
            }
            list.add(notification);
        }

        Iterator<String> iterator = sortedMap.keySet().iterator();
        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            ArrayList<Notification> list = sortedMap.get(key);
            sortedList.add(new DoubleTypesModel<Repository, Notification>(list.get(0).getRepository(), null));
            for(Notification notification : list){
                sortedList.add(new DoubleTypesModel<Repository, Notification>(null, notification));
            }
        }
        return sortedList;
    }

    public NotificationsFragment.NotificationsType getType() {
        return type;
    }
}
