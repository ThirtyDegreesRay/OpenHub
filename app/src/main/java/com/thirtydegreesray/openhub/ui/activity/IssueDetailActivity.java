package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IIssueDetailContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
import com.thirtydegreesray.openhub.mvp.presenter.IssueDetailPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.fragment.IssueTimelineFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 19:27:11
 */

public class IssueDetailActivity extends BaseActivity<IssueDetailPresenter>
        implements IIssueDetailContract.View{

    public static void show(@NonNull Activity activity, @NonNull View avatarView,
                            @NonNull View titleView, @NonNull Issue issue){
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        Pair<View, String> avatarPair = Pair.create(avatarView, "userAvatar");
        Pair<View, String> titlePair = Pair.create(titleView, "issueTitle");
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, avatarPair, titlePair);
        intent.putExtras(BundleBuilder.builder().put("issue", issue).build());
        activity.startActivity(intent, optionsCompat.toBundle());
    }

    public static void show(@NonNull Activity activity, @NonNull Issue issue){
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        intent.putExtras(BundleBuilder.builder().put("issue", issue).build());
        activity.startActivity(intent);
    }

    public static void show(@NonNull Activity activity, @NonNull String issueUrl){
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        intent.putExtras(BundleBuilder.builder().put("issueUrl", issueUrl).build());
        activity.startActivity(intent);
    }

    @BindView(R.id.user_avatar) ImageView userImageView;
    @BindView(R.id.issue_title) TextView issueTitle;
    @BindView(R.id.issue_state_img) ImageView issueStateImg;
    @BindView(R.id.issue_state_text) TextView issueStateText;
    @BindView(R.id.comment_bn) FloatingActionButton commentBn;
    @BindView(R.id.edit_bn) FloatingActionButton editBn;
    @BindView(R.id.loader) ProgressBar loader;

    private IssueTimelineFragment issueTimelineFragment;

    private final int ADD_COMMENT_REQUEST_CODE = 100;
    public static final int EDIT_COMMENT_REQUEST_CODE = 101;
    public static final int EDIT_ISSUE_REQUEST_CODE = 102;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_issue_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.issue));
        commentBn.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mPresenter.getIssue() != null) {
            getMenuInflater().inflate(R.menu.menu_issue_detail, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mPresenter.getIssue() == null){
            return true;
        }
        boolean isCanToggle = AppData.INSTANCE.getLoggedUser().getLogin()
                .equals(mPresenter.getIssue().getUser().getLogin()) ||
                AppData.INSTANCE.getLoggedUser().getLogin()
                        .equals(mPresenter.getIssue().getRepoAuthorName());
        boolean isOpen = mPresenter.getIssue().getState().equals(Issue.IssueState.open);
        if(isCanToggle){
            MenuItem item = menu.findItem(R.id.action_issue_toggle);
            item.setTitle(isOpen ? R.string.close : R.string.reopen);
        }else{
            menu.removeItem(R.id.action_issue_toggle);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                editBn.setVisibility(View.GONE);
                supportFinishAfterTransition();
                return true;
            case R.id.action_open_in_browser:
                AppHelper.openInBrowser(getActivity(), mPresenter.getIssue().getHtmlUrl());
                return true;
            case R.id.action_share:
                AppHelper.shareText(getActivity(), mPresenter.getIssue().getHtmlUrl());
                return true;
            case R.id.action_copy_url:
                AppHelper.copyToClipboard(getActivity(), mPresenter.getIssue().getHtmlUrl());
                return true;
            case R.id.action_issue_toggle:
                mPresenter.toggleIssueState();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showIssue(final Issue issue) {
        setToolbarTitle(getString(R.string.issue).concat(" #").concat(String.valueOf(issue.getNumber())));
        GlideApp.with(getActivity())
                .load(issue.getUser().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(userImageView);
        issueTitle.setText(issue.getTitle());
        commentBn.setVisibility(issue.isLocked() ? View.GONE : View.VISIBLE);

        String commentStr = String.valueOf(issue.getCommentNum()).concat(" ")
                .concat(getString(R.string.comments).toLowerCase());
        if(Issue.IssueState.open.equals(issue.getState())){
            issueStateImg.setImageResource(R.drawable.ic_issues);
            issueStateText.setText(getString(R.string.open).concat("    ").concat(commentStr));
        } else {
            issueStateImg.setImageResource(R.drawable.ic_issues_closed);
            issueStateText.setText(getString(R.string.closed).concat("    ").concat(commentStr));
        }
        invalidateOptionsMenu();

        if(issueTimelineFragment == null){
            issueTimelineFragment = IssueTimelineFragment.create(issue);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!isAlive) return;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, issueTimelineFragment)
                            .commit();
                }
            }, 500);
        }

        String loggedUser = AppData.INSTANCE.getLoggedUser().getLogin();
        boolean editAble = loggedUser.equals(issue.getUser().getLogin()) ||
                loggedUser.equals(issue.getRepoAuthorName());
        editBn.setVisibility(editAble ? View.VISIBLE : View.GONE);
        commentBn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof  IssueTimelineFragment){
            issueTimelineFragment = (IssueTimelineFragment) fragment;
        }
    }

    @Override
    public void showAddedComment(@NonNull IssueEvent event) {
        issueTimelineFragment.addComment(event);
    }

    @Override
    public void showAddCommentPage(@Nullable String text) {
        MarkdownEditorActivity.show(getActivity(), R.string.comment, ADD_COMMENT_REQUEST_CODE, text);
    }

    @OnClick(R.id.comment_bn)
    public void onCommentBnClicked(){
        showAddCommentPage(null);
    }

    @OnClick(R.id.edit_bn)
    public void onEditBnClicked(){
        EditIssueActivity.showForEdit(getActivity(), mPresenter.getIssue(), EDIT_ISSUE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == ADD_COMMENT_REQUEST_CODE){
            String text = data.getExtras().getString("text");
            mPresenter.addComment(text);
            return ;
        } else if(requestCode == EDIT_COMMENT_REQUEST_CODE){
            String text = data.getExtras().getString("text");
            issueTimelineFragment.onEditComment(text);
            return ;
        } else if(requestCode == EDIT_ISSUE_REQUEST_CODE){
            Issue issue = data.getParcelableExtra("issue");
            mPresenter.setIssue(issue);
            issueTitle.setText(issue.getTitle());
            issueTimelineFragment.onEditIssue(issue);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        editBn.setVisibility(View.GONE);
        super.onBackPressed();
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
}
