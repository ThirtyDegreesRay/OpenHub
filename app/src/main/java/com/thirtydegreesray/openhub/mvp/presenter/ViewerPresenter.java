

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IViewerContract;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.util.GitHubHelper;
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

    @AutoAccess ViewerActivity.ViewerType viewerType;

    @AutoAccess FileModel fileModel;
    private String downloadSource;

    @AutoAccess String title;
    @AutoAccess String source;
    @AutoAccess String imageUrl;

    @AutoAccess CommitFile commitFile;

    @Inject
    public ViewerPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if(ViewerActivity.ViewerType.RepoFile.equals(viewerType)){
            load(false);
        } else if(ViewerActivity.ViewerType.DiffFile.equals(viewerType)) {
            mView.loadDiffFile(commitFile.getPatch());
        } else if(ViewerActivity.ViewerType.Image.equals(viewerType)) {
            mView.loadImageUrl(imageUrl);
        } else if(ViewerActivity.ViewerType.HtmlSource.equals(viewerType)) {
            mView.loadMdText(source, null);
        } else {
            mView.loadMdText(source, null);
        }
    }

    @Override
    public void load(boolean isReload) {
        final String url = fileModel.getUrl();
        final String htmlUrl = fileModel.getHtmlUrl();
        if(StringUtils.isBlank(url) || StringUtils.isBlank(htmlUrl)){
            mView.showWarningToast(getString(R.string.url_invalid));
            mView.hideLoading();
            return;
        }

        if(GitHubHelper.isArchive(url)){
            mView.showWarningToast(getString(R.string.view_archive_file_error));
            mView.hideLoading();
            return;
        }

        if(GitHubHelper.isImage(url)){
            mView.loadImageUrl(fileModel.getDownloadUrl());
            mView.hideLoading();
            return;
        }

        HttpObserver<ResponseBody> httpObserver =
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {
                        mView.hideLoading();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        mView.hideLoading();
                        try {
                            downloadSource = response.body().string();
                            if(GitHubHelper.isMarkdown(url)){
                                mView.loadMdText(downloadSource, htmlUrl);
                            }else{
                                mView.loadCode(downloadSource, getExtension());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return GitHubHelper.isMarkdown(url) ?
                        getRepoService().getFileAsHtmlStream(forceNetWork, url) :
                        getRepoService().getFileAsStream(forceNetWork, url);
            }
        }, httpObserver, false);
        mView.showLoading();
    }

    public boolean isCode(){
        String url = fileModel != null ? fileModel.getUrl() : commitFile.getBlobUrl();
        return !GitHubHelper.isArchive(url) &&
                !GitHubHelper.isImage(url) &&
                !GitHubHelper.isMarkdown(url);
    }

    public String getDownloadSource() {
        return downloadSource;
    }

    public String getExtension(){
        return GitHubHelper.getExtension(fileModel.getUrl());
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public CommitFile getCommitFile() {
        return commitFile;
    }

    public ViewerActivity.ViewerType getViewerType() {
        return viewerType;
    }
}
