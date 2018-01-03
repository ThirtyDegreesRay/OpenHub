

package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IViewerContract;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.presenter.ViewerPresenter;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.webview.CodeWebView;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 15:59:55
 */

public class ViewerFragment extends BaseFragment<ViewerPresenter>
        implements IViewerContract.View,
        CodeWebView.ContentChangedListener{

    @BindView(R.id.web_view) CodeWebView webView;
    @BindView(R.id.loader) ProgressBar loader;

    @AutoAccess boolean wrap = false;
    //store scroll y percent, recover position when needed
    @AutoAccess float scrollYPercent = 0;

    @NonNull
    public static ViewerFragment createForHtml(@NonNull String title, @NonNull String htmlSource) {
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("viewerType", ViewerActivity.ViewerType.HtmlSource)
                .put("title", title).put("source", htmlSource).build());
        return fragment;
    }

    @NonNull
    public static ViewerFragment createForMd(@NonNull String title, @NonNull String mdSource) {
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("viewerType", ViewerActivity.ViewerType.MarkDown)
                .put("title", title).put("source", mdSource).build());
        return fragment;
    }

    @NonNull
    public static ViewerFragment createForImage(@NonNull String title, @NonNull String imageUrl) {
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("viewerType", ViewerActivity.ViewerType.Image)
                .put("title", title).put("imageUrl", imageUrl).build());
        return fragment;
    }

    @NonNull
    public static ViewerFragment create(@NonNull FileModel fileModel) {
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("viewerType", ViewerActivity.ViewerType.RepoFile)
                .put("fileModel", fileModel).build());
        return fragment;
    }

    @NonNull
    public static ViewerFragment createForDiff(@NonNull CommitFile commitFile) {
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("viewerType", ViewerActivity.ViewerType.DiffFile)
                .put("commitFile", commitFile).build());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewer;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        wrap = PrefUtils.isCodeWrap();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_wrap_lines);
        MenuItem menuItemDownload = menu.findItem(R.id.action_download);
        MenuItem menuItemViewFile = menu.findItem(R.id.action_view_file);
        MenuItem menuItemRefresh = menu.findItem(R.id.action_refresh);
        if(ViewerActivity.ViewerType.RepoFile.equals(mPresenter.getViewerType())) {
            menuItem.setVisible(mPresenter.isCode() && !StringUtils.isBlank(mPresenter.getDownloadSource()));
            menuItem.setChecked(wrap);
            menuItemDownload.setVisible(!StringUtils.isBlank(mPresenter.getFileModel().getDownloadUrl()));
            menuItemViewFile.setVisible(false);
            menuItemRefresh.setVisible(true);
        } else if(ViewerActivity.ViewerType.DiffFile.equals(mPresenter.getViewerType())){
            menuItem.setVisible(true);
            menuItem.setChecked(wrap);
            menuItemDownload.setVisible(false);
            menuItemViewFile.setVisible(true);
            menuItemRefresh.setVisible(false);
        } else if(ViewerActivity.ViewerType.Image.equals(mPresenter.getViewerType())) {
            menuItem.setVisible(false);
            menuItemViewFile.setVisible(false);
            menuItemRefresh.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_wrap_lines){
            item.setChecked(!item.isChecked());
            wrap = item.isChecked();
            PrefUtils.set(PrefUtils.CODE_WRAP, wrap);
            if(ViewerActivity.ViewerType.RepoFile.equals(mPresenter.getViewerType())){
                loadCode(mPresenter.getDownloadSource(), mPresenter.getExtension());
            } else if(ViewerActivity.ViewerType.DiffFile.equals(mPresenter.getViewerType())){
                loadDiffFile(mPresenter.getCommitFile().getPatch());
            }
            return true;
        } else if(item.getItemId() == R.id.action_refresh){
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadImageUrl(@NonNull String url) {
        webView.loadImage(url);
        webView.setContentChangedListener(this);
    }

    @Override
    public void loadMdText(@NonNull String text, @NonNull String baseUrl) {
        webView.setMdSource(text, baseUrl);
        webView.setContentChangedListener(this);
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    @Override
    public void loadCode(@NonNull String text, @Nullable String extension) {
        webView.setCodeSource(text, wrap, extension);
        webView.setContentChangedListener(this);
        getActivity().invalidateOptionsMenu();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    @Override
    public void loadDiffFile(@NonNull String text) {
        webView.setDiffFileSource(text, wrap);
        webView.setContentChangedListener(this);
        getActivity().invalidateOptionsMenu();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    @Override
    public void loadHtmlSource(@NonNull String htmlSource) {
        webView.setHtmlSource(htmlSource);
        webView.setContentChangedListener(this);
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loader.setVisibility(View.GONE);
    }

    public void refresh() {
        mPresenter.load(true);
    }

    @Override
    public void onContentChanged(int progress) {
        if (loader != null) {
            loader.setProgress(progress);
            if (progress == 100) {
                loader.setVisibility(View.GONE);
                //delay 300 mills, in case of content height unavailable
                webView.postDelayed(() -> {
                    if(webView == null) return;
                    int scrollY = (int) (webView.getContentHeight() * scrollYPercent);
                    webView.scrollTo(0, scrollY);
                }, 300);
            }
        }
    }

    @Override
    public void onScrollChanged(boolean reachedTop, int scroll) {

    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        webView.scrollTo(0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(webView != null){
            int scrollY = webView.getScrollY();
            scrollYPercent = scrollY * 1.0f / webView.getContentHeight();
        }
    }
}
