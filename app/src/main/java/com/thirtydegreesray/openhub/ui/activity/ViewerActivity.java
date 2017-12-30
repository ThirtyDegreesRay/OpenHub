

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.ViewerFragment;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:05:44
 */

public class ViewerActivity extends SingleFragmentActivity<IBaseContract.Presenter, ViewerFragment> {

    public enum ViewerType{
        RepoFile, MarkDown, DiffFile, Image, HtmlSource
    }

    public static void showHtmlSource(@NonNull Context context, @NonNull String title,
                                    @NonNull String htmlSource){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleHelper.builder().put("viewerType", ViewerType.HtmlSource)
                .put("title", title).put("source", htmlSource).build());
        context.startActivity(intent);
    }

    public static void showMdSource(@NonNull Context context, @NonNull String title,
                                    @NonNull String mdSource){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleHelper.builder().put("viewerType", ViewerType.MarkDown)
                .put("title", title).put("source", mdSource).build());
        context.startActivity(intent);
    }

    public static void showImage(@NonNull Context context, @NonNull String imageUrl){
        String title = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        showImage(context, title, imageUrl);
    }

    public static void showImage(@NonNull Context context, @NonNull String title,
                                 @NonNull String imageUrl){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleHelper.builder().put("viewerType", ViewerType.Image)
                .put("title", title).put("imageUrl", imageUrl).build());
        context.startActivity(intent);
    }

    public static void show(@NonNull Context context, @NonNull String url
            , @Nullable String htmlUrl){
        show(context, url, htmlUrl, null);
    }

    public static void show(@NonNull Context context, @NonNull String url
            , @Nullable String htmlUrl, @Nullable String downloadUrl){
        FileModel fileModel = new FileModel();
        fileModel.setHtmlUrl(htmlUrl);
        fileModel.setDownloadUrl(downloadUrl);
        fileModel.setUrl(url);
        fileModel.setName(url.substring(url.lastIndexOf("/") + 1,
                url.contains("?") ? url.indexOf("?") : url.length()));
        show(context, fileModel);
    }

    public static void show(@NonNull Context context, @NonNull FileModel fileModel){
        show(context, fileModel, null);
    }

    public static void show(@NonNull Context context, @NonNull FileModel fileModel
            , @Nullable String repoName){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleHelper.builder().put("viewerType", ViewerType.RepoFile)
                .put("fileModel", fileModel).put("repoName", repoName).build());
        context.startActivity(intent);
    }

    public static void showForDiff(@NonNull Context context, @NonNull CommitFile commitFile){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleHelper.builder().put("viewerType", ViewerType.DiffFile)
                .put("commitFile", commitFile).build());
        context.startActivity(intent);
    }

    @AutoAccess ViewerType viewerType;

    @AutoAccess FileModel fileModel;
    @AutoAccess CommitFile commitFile;
    @AutoAccess String repoName;

    @AutoAccess String title;
    @AutoAccess String source;

    @AutoAccess String imageUrl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(fileModel != null || commitFile != null || imageUrl != null)
            getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String title;
        if(ViewerType.RepoFile.equals(viewerType)){
            title = fileModel.getName();
        } else if(ViewerType.DiffFile.equals(viewerType)){
            title = commitFile.getShortFileName();
        } else {
            title = this.title;
        }
        setToolbarTitle(title);
    }

    @Override
    protected ViewerFragment createFragment() {
        ViewerFragment fragment;
        if(ViewerType.RepoFile.equals(viewerType)){
            fragment = ViewerFragment.create(fileModel);
        } else if(ViewerType.DiffFile.equals(viewerType)){
            fragment = ViewerFragment.createForDiff(commitFile);
        } else if(ViewerType.Image.equals(viewerType)){
            fragment = ViewerFragment.createForImage(title, imageUrl);
        } else if(ViewerType.HtmlSource.equals(viewerType)){
            fragment = ViewerFragment.createForHtml(title, source);
        } else {
            fragment = ViewerFragment.createForMd(title, source);
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            return super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_fullscreen){
            intoFullScreen();
            return true;
        }
        String htmlUrl = null;
        if(fileModel != null) htmlUrl = fileModel.getHtmlUrl();
        if(commitFile != null) htmlUrl = commitFile.getBlobUrl();
        if(imageUrl != null) htmlUrl = imageUrl;
        if(!StringUtils.isBlank(htmlUrl)){
            switch (item.getItemId()) {
                case R.id.action_open_in_browser:
                    AppOpener.openInCustomTabsOrBrowser(getActivity(), htmlUrl);
                    return true;
                case R.id.action_share:
                    AppOpener.shareText(getActivity(), htmlUrl);
                    return true;
                case R.id.action_copy_url:
                    AppUtils.copyToClipboard(getActivity(), htmlUrl);
                    return true;
            }
        }

        switch (item.getItemId()) {
            case R.id.action_view_file:
                ViewerActivity.show(getActivity(), commitFile.getContentsUrl(),
                        commitFile.getBlobUrl(), commitFile.getRawUrl());
                return true;
            case R.id.action_download:
                String downloadUrl ;
                String fileName ;
                if(fileModel != null){
                    downloadUrl = fileModel.getDownloadUrl();
                    fileName = fileModel.getName();
                    if(!StringUtils.isBlank(repoName)) fileName = repoName.concat("-").concat(fileName);
                } else {
                    downloadUrl = imageUrl;
                    fileName = title;
                }
                AppOpener.startDownload(getActivity(), downloadUrl, fileName);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
