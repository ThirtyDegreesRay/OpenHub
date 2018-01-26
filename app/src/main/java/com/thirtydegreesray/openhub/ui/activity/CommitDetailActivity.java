

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.ICommitDetailContract;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.mvp.model.RepoCommitExt;
import com.thirtydegreesray.openhub.mvp.presenter.CommitDetailPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.fragment.CommitFilesFragment;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 11:50:11
 */

public class CommitDetailActivity extends BaseActivity<CommitDetailPresenter>
        implements ICommitDetailContract.View {

    public static void show(@NonNull Activity activity, @NonNull String commitUrl) {
        Intent intent = new Intent(activity, CommitDetailActivity.class);
        intent.putExtras(BundleHelper.builder().put("commitUrl", commitUrl).build());
        activity.startActivity(intent);
    }

    public static void show(@NonNull Activity activity, @NonNull String user, @NonNull String repo,
                            @NonNull RepoCommit commit, @NonNull View userAvatar) {
        Intent intent = new Intent(activity, CommitDetailActivity.class);
        intent.putExtras(BundleHelper.builder().put("user", user).put("repo", repo)
                .put("commit", commit).build());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, userAvatar, "userAvatar");
        activity.startActivity(intent, optionsCompat.toBundle());
    }

    public static void show(@NonNull Activity activity, @NonNull String user, @NonNull String repo,
                            @NonNull String commitSha, @NonNull View userAvatar, @NonNull String userAvatarUrl) {
        Intent intent = createIntent(activity, user, repo, commitSha, userAvatarUrl);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, userAvatar, "userAvatar");
        activity.startActivity(intent, optionsCompat.toBundle());
    }

    public static Intent createIntent(@NonNull Activity activity, @NonNull String user, @NonNull String repo,
                                      @NonNull String commitSha) {
        return createIntent(activity, user, repo, commitSha, null);

    }

    public static Intent createIntent(@NonNull Activity activity, @NonNull String user, @NonNull String repo,
                                      @NonNull String commitSha, @NonNull String userAvatarUrl) {
        return new Intent(activity, CommitDetailActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("user", user)
                        .put("repo", repo)
                        .put("commitSha", commitSha)
                        .put("userAvatarUrl", userAvatarUrl).build());

    }

    @BindView(R.id.user_avatar) CircleImageView userAvatar;
    @BindView(R.id.commit_message) TextView commitMessage;
    @BindView(R.id.changed_files_count) TextView changedFileCount;
    @BindView(R.id.additions_count) TextView addtionsCount;
    @BindView(R.id.deletions_count) TextView deletionsCount;

    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.comment_bn) FloatingActionButton commentBn;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.commit));
        commentBn.setVisibility(View.GONE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_commit_detail;
    }

    @Override
    public void showCommit(RepoCommit commit) {
        setToolbarTitle(getString(R.string.commit).concat(" ").concat(commit.getShortSha()));
        if (commit.getAuthor() != null) {
            GlideApp.with(getActivity())
                    .load(commit.getAuthor().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(userAvatar);
        } else {
            userAvatar.setImageResource(R.drawable.ic_question);
        }
        String commitDateStr = getString(R.string.committed).concat(" ").concat(
                StringUtils.getNewsTimeStr(getActivity(), commit.getCommit().getAuthor().getDate()));
        commitMessage.setText(commitDateStr.concat("\n").concat(commit.getCommit().getMessage()));
        invalidateOptionsMenu();
    }

    private CommitFilesFragment commitFilesFragment;

    @Override
    public void showCommitInfo(RepoCommitExt commitExt) {
        showCommit(commitExt);

        changedFileCount.setText(String.valueOf(commitExt.getFiles().size()));
        addtionsCount.setText(String.valueOf(commitExt.getStats().getAdditions()));
        deletionsCount.setText(String.valueOf(commitExt.getStats().getDeletions()));

        if (commitFilesFragment == null) {
            commitFilesFragment = CommitFilesFragment.create(commitExt.getFiles());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, commitFilesFragment)
                    .commitAllowingStateLoss();
        } else {
            commitFilesFragment.showCommitFiles(commitExt.getFiles());
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CommitFilesFragment) {
            commitFilesFragment = (CommitFilesFragment) fragment;
        }
    }

    @Override
    public void showUserAvatar(String userAvatarUrl) {
        GlideApp.with(getActivity())
                .load(userAvatarUrl)
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userAvatar);
    }

    @OnClick(R.id.comment_bn)
    public void onViewClicked() {

    }

    @Override
    public void finishActivity() {
        supportFinishAfterTransition();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_in_browser:
                AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getCommit().getHtmlUrl());
                return true;
            case R.id.action_share:
                AppOpener.shareText(getActivity(), mPresenter.getCommit().getHtmlUrl());
                return true;
            case R.id.action_copy_url:
                AppUtils.copyToClipboard(getActivity(), mPresenter.getCommit().getHtmlUrl());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mPresenter.getCommit() != null)
            getMenuInflater().inflate(R.menu.menu_commit_detail, menu);
        return true;
    }

    @OnClick(R.id.commit_message)
    public void onCommitMessageClick() {
        commitMessage.setMaxLines(commitMessage.getMaxLines() == 6 ? 20 : 6);
    }

    @OnClick(R.id.user_avatar)
    public void onUserAvatarClick() {
        if (mPresenter.getCommit() != null && mPresenter.getCommit().getAuthor() != null) {
            RepoCommit commit = mPresenter.getCommit();
            ProfileActivity.show(getActivity(), userAvatar,
                    commit.getAuthor().getLogin(), commit.getAuthor().getAvatarUrl());
        }
    }

    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        if (commitFilesFragment != null) commitFilesFragment.scrollToTop();
    }

}
