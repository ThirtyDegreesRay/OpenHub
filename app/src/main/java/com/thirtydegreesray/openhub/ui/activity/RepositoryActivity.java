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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.http.Downloader;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepositoryPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.PagerActivity;
import com.thirtydegreesray.openhub.ui.adapter.BranchesAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.FragmentPagerModel;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:39:20
 */
//FIXME fix fragment not show from background
public class RepositoryActivity extends PagerActivity<RepositoryPresenter>
        implements IRepositoryContract.View {

    public static void show(@NonNull Context activity, @NonNull String owner,
                            @NonNull String repoName) {
        Intent intent = new Intent(activity, RepositoryActivity.class);
        intent.putExtras(BundleBuilder.builder().put("owner", owner).put("repoName", repoName).build());
        activity.startActivity(intent);
    }

    public static void show(@NonNull Context activity, @NonNull Repository repository) {
        Intent intent = new Intent(activity, RepositoryActivity.class);
        intent.putExtra("repository", repository);
        activity.startActivity(intent);
    }

    public static void show(@NonNull Activity activity, @NonNull Repository repository,
                            @NonNull View titleView) {
        Intent intent = new Intent(activity, RepositoryActivity.class);
        intent.putExtra("repository", repository);
        activity.startActivity(intent);
    }

    @BindView(R.id.user_avatar_bg) ImageView userImageViewBg;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.desc) TextView desc;
    @BindView(R.id.info) TextView info;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_repository;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repository, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem starItem = menu.findItem(R.id.action_star);
        starItem.setTitle(mPresenter.isStarred() ? R.string.unstar : R.string.star);
        starItem.setIcon(mPresenter.isStarred() ?
                R.drawable.ic_star_title : R.drawable.ic_un_star_title);
        menu.findItem(R.id.action_watch).setTitle(mPresenter.isWatched() ?
                R.string.unwatch : R.string.watch);
        menu.findItem(R.id.action_fork).setTitle(mPresenter.isFork() ?
                R.string.forked : R.string.fork);
        menu.findItem(R.id.action_fork).setVisible(mPresenter.isForkEnable());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTransparentStatusBar();
        toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_TitleText);
        toolbar.setSubtitleTextAppearance(getActivity(), R.style.Toolbar_Subtitle);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.getRepoName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mPresenter.getRepository() == null && item.getItemId() != android.R.id.home){
            showWarningToast(getString(R.string.no_data));
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_star:
                mPresenter.starRepo(!mPresenter.isStarred());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isStarred() ?
                        getString(R.string.starred) : getString(R.string.unstarred));
                return true;
            case R.id.action_branch:
                mPresenter.loadBranchesAndTags();
                return true;
            case R.id.action_share:
                AppHelper.shareText(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_open_in_browser:
                AppHelper.openInBrowser(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_copy_url:
                AppHelper.copyToClipboard(getActivity(), mPresenter.getRepository().getHtmlUrl());
                return true;
            case R.id.action_copy_clone_url:
                AppHelper.copyToClipboard(getActivity(), mPresenter.getRepository().getCloneUrl());
                return true;
            case R.id.action_watch:
                mPresenter.watchRepo(!mPresenter.isWatched());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isWatched() ?
                        getString(R.string.watched) : getString(R.string.unwatched));
                return true;
            case R.id.action_fork:
                if(!mPresenter.getRepository().isFork()) forkRepo();
                return true;
            case R.id.action_releases:
                showReleases();
                return true;
            case R.id.action_download_source_zip:
                Downloader.create(getApplicationContext())
                        .start(mPresenter.getZipSourceUrl(), mPresenter.getZipSourceName());
                return true;
            case R.id.action_download_source_tar:
                Downloader.create(getApplicationContext())
                        .start(mPresenter.getTarSourceUrl(), mPresenter.getTarSourceName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRepo(Repository repo) {
//        setToolbarTitle(repo.getFullName(), repo.getDefaultBranch());
        desc.setText(repo.getDescription());
        String language = StringUtils.isBlank(repo.getLanguage()) ?
                getString(R.string.unknown) : repo.getLanguage();
        info.setText(String.format(Locale.getDefault(), "Language %s, size %s",
                language, StringUtils.getSizeString(repo.getSize() * 1024)));

        if (pagerAdapter.getCount() == 0) {
            pagerAdapter.setPagerList(FragmentPagerModel
                    .createRepoPagerList(getActivity(), repo, getFragments()));
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(pagerAdapter);
            showFirstPager();

            GlideApp.with(getActivity())
                    .load(repo.getOwner().getAvatarUrl())
                    .centerCrop()
                    .into(userImageViewBg);
        } else {
            noticeRepositoryUpdated(repo);
        }
    }

    @Override
    public void showBranchesAndTags(final ArrayList<Branch> list, Branch curBranch) {
        BranchesAdapter branchesAdapter = new BranchesAdapter(getActivity(), curBranch.getName());
        branchesAdapter.setData(list);

        final RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(branchesAdapter);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(getString(R.string.select_branch))
                .setView(recyclerView)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

        branchesAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, @NonNull View view) {
                Branch branch = list.get(position);
                mPresenter.getRepository().setDefaultBranch(branch.getName());
                mPresenter.setCurBranch(branch);
                noticeBranchChanged(branch);
//                showRepo(mPresenter.getRepository());
                alertDialog.dismiss();
            }
        });

    }

    private void forkRepo(){
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.fork_warning_msg)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.fork, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.createFork();
                    }
                }).show();
    }

    private void showReleases(){
        ReleasesActivity.show(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
                mPresenter.getRepository().getName());
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

    private void noticeBranchChanged(Branch branch){
        for(FragmentPagerModel pagerModel : pagerAdapter.getPagerList()){
            if(pagerModel.getFragment() instanceof RepositoryListener){
                ((RepositoryListener)pagerModel.getFragment()).onBranchChanged(branch);
            }
        }
    }

    private void noticeRepositoryUpdated(Repository repository){
        for(FragmentPagerModel pagerModel : pagerAdapter.getPagerList()){
            if(pagerModel.getFragment() instanceof RepositoryListener){
                ((RepositoryListener)pagerModel.getFragment()).onRepositoryInfoUpdated(repository);
            }
        }
    }

    public interface RepositoryListener{
        void onRepositoryInfoUpdated(Repository repository);
        void onBranchChanged(Branch branch);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public int getPagerSize() {
        return 4;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof RepoInfoFragment){
            return 0;
        }else if(fragment instanceof RepoFilesFragment){
            return 1;
        }else if(fragment instanceof CommitsFragment){
            return 2;
        }else if(fragment instanceof ActivityFragment){
            return 3;
        }else
            return -1;
    }
}
