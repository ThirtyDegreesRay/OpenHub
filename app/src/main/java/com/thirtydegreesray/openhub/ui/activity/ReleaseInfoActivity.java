package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.thirtydegreesray.openhub.ui.widget.DownloadSourceDialog;
import com.thirtydegreesray.openhub.ui.widget.webview.CodeWebView;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 13:13:46
 */

public class ReleaseInfoActivity extends BaseActivity<ReleaseInfoPresenter>
        implements IReleaseInfoContract.View {

    public static void show(Activity activity, @NonNull String owner, @NonNull String repoName,
                            @NonNull String tagName) {
        Intent intent = createIntent(activity, owner, repoName, tagName);
        activity.startActivity(intent);
    }

    public static void show(Activity activity, @NonNull String owner,
                            @NonNull String repoName, @Nullable Release release) {
        Intent intent = new Intent(activity, ReleaseInfoActivity.class);
        intent.putExtras(BundleHelper.builder().put("release", release)
                .put("owner", owner).put("repoName", repoName).build());
        activity.startActivity(intent);
    }

    public static Intent createIntent(Activity activity, @NonNull String owner, @NonNull String repoName,
                                      @NonNull String tagName) {
        return new Intent(activity, ReleaseInfoActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("tagName", tagName)
                        .put("owner", owner)
                        .put("repoName", repoName).build());
    }

    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    @BindView(R.id.web_view) CodeWebView webView;
    @BindView(R.id.user_avatar) ImageView userAvatar;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.download_bn) FloatingActionButton downloadBn;
    @BindView(R.id.loader) ProgressBar loader;

    @Override
    public void showReleaseInfo(Release release) {
        downloadBn.setVisibility(View.VISIBLE);
        webView.setMdSource(StringUtils.isBlank(release.getBodyHtml()) ?
                release.getBody() : release.getBodyHtml(), null);

        GlideApp.with(getActivity())
                .load(release.getAuthor().getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userAvatar);

        String time = StringUtils.getNewsTimeStr(getActivity(), release.getPublishedAt());
        String timeStr = "";
        if (time.contains("-")) {
            timeStr = getString(R.string.released_this)
                    .concat(" ").concat(getString(R.string.on))
                    .concat(" ").concat(time);
        } else {
            timeStr = getString(R.string.released_this)
                    .concat(" ").concat(time);
        }

        String str = release.getAuthor().getLogin().concat(" ").concat(timeStr);
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ViewUtils.getAccentColor(getActivity()));
        spannable.setSpan(colorSpan, 0, release.getAuthor().getLogin().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userName.setText(spannable);
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
        setToolbarTitle(mPresenter.getTagName(),
                mPresenter.getOwner().concat("/").concat(mPresenter.getRepoName()));
        downloadBn.setVisibility(View.GONE);
        setToolbarScrollAble(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.download_bn)
    public void showDownloadDialog() {
        DownloadSourceDialog.show(getActivity(), mPresenter.getRepoName(),
                mPresenter.getRelease().getTagName(), mPresenter.getRelease());
    }

    @OnClick({R.id.user_name, R.id.user_avatar})
    public void onUserClick() {
        ProfileActivity.show(getActivity(), userAvatar, mPresenter.getRelease().getAuthor().getLogin(),
                mPresenter.getRelease().getAuthor().getAvatarUrl());
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loader.setVisibility(View.GONE);
    }

    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        scrollView.scrollTo(0, 0);
    }
}
