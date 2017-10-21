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
import com.thirtydegreesray.openhub.http.Downloader;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.ViewerFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:05:44
 */

public class ViewerActivity extends SingleFragmentActivity<IBaseContract.Presenter, ViewerFragment> {

    public enum ViewerType{
        RepoFile, MarkDown, DiffFile
    }

    public static void showMdSource(@NonNull Context context, @NonNull String title,
                                    @NonNull String mdSource){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleBuilder.builder().put("viewerType", ViewerType.MarkDown)
                .put("title", title).put("mdSource", mdSource).build());
        context.startActivity(intent);
    }

    public static void show(@NonNull Context context, @NonNull String imageUrl){
        show(context, imageUrl, imageUrl, imageUrl);
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
        intent.putExtras(BundleBuilder.builder().put("viewerType", ViewerType.RepoFile)
                .put("fileModel", fileModel).put("repoName", repoName).build());
        context.startActivity(intent);
    }

    public static void showForDiff(@NonNull Context context, @NonNull CommitFile commitFile){
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtras(BundleBuilder.builder().put("viewerType", ViewerType.DiffFile)
                .put("commitFile", commitFile).build());
        context.startActivity(intent);
    }

    @AutoAccess ViewerType viewerType;

    @AutoAccess FileModel fileModel;
    @AutoAccess CommitFile commitFile;
    @AutoAccess String repoName;

    @AutoAccess String title;
    @AutoAccess String mdSource;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(fileModel != null || commitFile != null)
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
            title = fileModel.getName();
            fragment = ViewerFragment.create(fileModel);
        } else if(ViewerType.DiffFile.equals(viewerType)){
            title = commitFile.getShortFileName();
            fragment = ViewerFragment.createForDiff(commitFile);
        } else {
            title = this.title;
            fragment = ViewerFragment.createForMd(title, mdSource);
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            return super.onOptionsItemSelected(item);
        String htmlUrl = null;
        if(fileModel != null) htmlUrl = fileModel.getHtmlUrl();
        if(commitFile != null) htmlUrl = commitFile.getBlobUrl();
        if(!StringUtils.isBlank(htmlUrl)){
            switch (item.getItemId()) {
                case R.id.action_open_in_browser:
                    AppHelper.openInBrowser(getActivity(), htmlUrl);
                    return true;
                case R.id.action_share:
                    AppHelper.shareText(getActivity(), htmlUrl);
                    return true;
                case R.id.action_copy_url:
                    AppHelper.copyToClipboard(getActivity(), htmlUrl);
                    return true;
            }
        }

        switch (item.getItemId()) {
            case R.id.action_view_file:
                ViewerActivity.show(getActivity(), commitFile.getContentsUrl(),
                        commitFile.getBlobUrl(), commitFile.getRawUrl());
                return true;
            case R.id.action_download:
                String fileName = fileModel.getName();
                if(!StringUtils.isBlank(repoName)) fileName = repoName.concat("-").concat(fileName);
                Downloader.create(getApplicationContext())
                        .start(fileModel.getDownloadUrl(), fileName);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
