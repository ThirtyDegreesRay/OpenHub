package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IReleaseInfoContract;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.presenter.ReleaseInfoPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.adapter.DownloadSourcesAdapter;
import com.thirtydegreesray.openhub.ui.widget.AdaptiveView;
import com.thirtydegreesray.openhub.ui.widget.webview.CodeWebView;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 13:13:46
 */

public class ReleaseInfoActivity extends BaseActivity<ReleaseInfoPresenter>
        implements IReleaseInfoContract.View{

    public static void show(Activity activity,  String repoName, Release release){
        Intent intent = new Intent(activity, ReleaseInfoActivity.class);
        intent.putExtras(BundleBuilder.builder().put("release", release)
                .put("repoName", repoName).build());
        activity.startActivity(intent);
    }

    @BindView(R.id.web_view) CodeWebView webView;
    @BindView(R.id.user_avatar) ImageView userAvatar;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.release_time) TextView releaseTime;

    @Override
    public void showReleaseInfo(Release release) {
        webView.setMdSource(release.getBodyHtml(), null);
        GlideApp.with(getActivity())
                .load(release.getAuthor().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(userAvatar);
        userName.setText(release.getAuthor().getLogin());

        String time = StringUtils.getNewsTimeStr(getActivity(), release.getCreatedAt());
        String timeStr = "";
        if(time.contains("-")){
            timeStr = getString(R.string.released_this)
                    .concat(" ").concat(getString(R.string.on))
                    .concat(" ").concat(time);
        }else{
            timeStr = getString(R.string.released_this)
                    .concat(" ").concat(time);
        }
        releaseTime.setText(timeStr);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_release_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getTagName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_release_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mPresenter.getRelease() == null) return true;
        switch (item.getItemId()) {
            case R.id.action_download_zip:
                AppHelper.openInBrowser(getActivity(), mPresenter.getRelease().getZipballUrl());
                return true;
            case R.id.action_download_tar:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.download_bn)
    public void showDownloadDialog(){
        DownloadSourcesAdapter adapter = new DownloadSourcesAdapter(getActivity(),
                mPresenter.getRepoName(), mPresenter.getRelease().getTagName());
        adapter.setData(mPresenter.getDownloadSources());

        final RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        AdaptiveView contentView = new AdaptiveView(getActivity());
        contentView.addView(recyclerView);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.download)
                .setView(contentView)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @OnClick({R.id.user_name, R.id.user_avatar})
    public void onUserClick(){
        ProfileActivity.show(getActivity(), mPresenter.getRelease().getAuthor().getLogin());
    }

}
