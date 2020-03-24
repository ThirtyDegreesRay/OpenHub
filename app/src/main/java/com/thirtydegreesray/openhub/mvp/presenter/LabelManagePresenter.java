package com.thirtydegreesray.openhub.mvp.presenter;

import androidx.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.ILabelManageContract;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2018/1/11 10:52:43
 */

public class LabelManagePresenter extends BasePresenter<ILabelManageContract.View>
        implements ILabelManageContract.Presenter {

    @AutoAccess String owner;
    @AutoAccess String repo;

    private ArrayList<Label> labels;

    @Inject
    public LabelManagePresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadLabels(false);
    }

    @Override
    public void loadLabels(boolean isReload) {
        mView.showLoading();
        HttpObserver<ArrayList<Label>> httpObserver = new HttpObserver<ArrayList<Label>>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if(labels != null && labels.size() != 0){
                    mView.showErrorToast(getErrorTip(error));
                } else {
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<Label>> response) {
                mView.hideLoading();
                labels = response.body();
                mView.showLabels(labels);
            }
        };

        generalRxHttpExecute(forceNetWork ->
                getIssueService().getRepoLabels(forceNetWork, owner, repo),
                httpObserver, !isReload);

    }

    @Override
    public void createLabel(@NonNull Label label) {
        HttpObserver<Label> httpObserver = new HttpObserver<Label>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<Label> response) {

            }
        };
        generalRxHttpExecute(forceNetWork ->
                getIssueService().createLabel(owner, repo, label),
                httpObserver, false);
        labels.add(label);
        mView.notifyItemInserted(labels.size() - 1);
    }

    @Override
    public void deleteLabel(@NonNull Label label) {
        executeSimpleRequest(getIssueService().deleteLabel(owner, repo, label.getName()));
        int position = labels.indexOf(label);
        labels.remove(label);
        mView.notifyItemRemoved(position);
    }

    @Override
    public void updateLabel(@NonNull Label oriLabel, @NonNull Label newLabel) {
        HttpObserver<Label> httpObserver = new HttpObserver<Label>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<Label> response) {

            }
        };
        generalRxHttpExecute(forceNetWork ->
                        getIssueService().updateLabel(owner, repo, oriLabel.getName(), newLabel),
                httpObserver, false);
        int position = labels.indexOf(oriLabel);
        labels.set(position, newLabel);
        mView.notifyItemChanged(position);
    }


}
