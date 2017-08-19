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

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IViewerContract;
import com.thirtydegreesray.openhub.util.MarkdownHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:58:43
 */

public class ViewerPresenter extends BasePresenter<IViewerContract.View>
        implements IViewerContract.Presenter{

    @AutoAccess String url;
    @AutoAccess String htmlUrl;
    private String downloadSource;

    @Inject
    public ViewerPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        load(false);
    }

    @Override
    public void load(boolean isReload) {
        if(StringUtils.isBlank(url) || StringUtils.isBlank(htmlUrl)){
            mView.showShortToast(getString(R.string.url_invalid));
            mView.hideLoading();
            return;
        }

        if(MarkdownHelper.isArchive(url)){
            mView.showLongToast(getString(R.string.view_archive_file_error));
            mView.hideLoading();
            return;
        }

        if(MarkdownHelper.isImage(url)){
            mView.loadImageUrl(url);
            mView.hideLoading();
            return;
        }

        HttpObserver<ResponseBody> httpObserver =
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        mView.showShortToast(error.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        mView.hideLoading();
                        try {
                            downloadSource = response.body().string();
                            if(MarkdownHelper.isMarkdown(url)){
                                mView.loadMdText(downloadSource, htmlUrl);
                            }else{
                                mView.loadCode(downloadSource);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return MarkdownHelper.isMarkdown(url) ?
                        getRepoService().getFileAsHtmlStream(forceNetWork, url) :
                        getRepoService().getFileAsStream(forceNetWork, url);
            }
        }, httpObserver, !isReload);
        mView.showLoading();
    }

    public boolean isCode(){
        return !MarkdownHelper.isArchive(url) && !MarkdownHelper.isImage(url) &&
                !MarkdownHelper.isMarkdown(url);
    }

    public String getDownloadSource() {
        return downloadSource;
    }
}
