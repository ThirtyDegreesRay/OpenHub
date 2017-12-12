

package com.thirtydegreesray.openhub.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IRepoInfoContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepoInfoPresenter;
import com.thirtydegreesray.openhub.ui.activity.IssuesActivity;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.RepoListActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.activity.UserListActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.webview.CodeWebView;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:35:39
 */

public class RepoInfoFragment extends BaseFragment<RepoInfoPresenter>
        implements IRepoInfoContract.View,
        CodeWebView.ContentChangedListener,
        RepositoryActivity.RepositoryListener {

    public static RepoInfoFragment create(Repository repository) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setArguments(BundleHelper.builder().put("repository", repository).build());
        return fragment;
    }

    @BindView(R.id.scroll_view) NestedScrollView nestedScrollView;
    @BindView(R.id.repo_title_text) TextView repoTitleText;
    @BindView(R.id.fork_info_text) TextView forkInfoText;
//    @BindView(R.id.repo_desc_text) TextView repoDescText;
    @BindView(R.id.repo_created_info_text) TextView repoCreatedInfoText;
    @BindView(R.id.issues_num_text) TextView issuesNumText;
    @BindView(R.id.issues_lay) View issueLay;
    @BindView(R.id.stargazers_num_text) TextView stargazersNumText;
    @BindView(R.id.forks_num_text) TextView forksNumText;
    @BindView(R.id.watchers_num_text) TextView watchersNumText;

    @BindView(R.id.readme_title) TextView readmeTitle;
    @BindView(R.id.readme_loader) ProgressBar readmeLoader;
    @BindView(R.id.web_view) CodeWebView webView;

    private boolean isReadmeSetted = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_repo_info;
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
        webView.setContentChangedListener(this);
    }

    @Override
    public void showRepoInfo(Repository repository) {
//        repoDescText.setVisibility(View.GONE);

        issueLay.setVisibility(repository.isHasIssues() ? View.VISIBLE :View.GONE);
        issuesNumText.setText(String.valueOf(repository.getOpenIssuesCount()));
        stargazersNumText.setText(String.valueOf(repository.getStargazersCount()));
        forksNumText.setText(String.valueOf(repository.getForksCount()));
        watchersNumText.setText(String.valueOf(repository.getSubscribersCount()));
//        repoDescText.setText(repository.getDescription());

//        String language = StringUtils.isBlank(repository.getLanguage()) ?
//                getString(R.string.unknown) : repository.getLanguage();
//        repoCreatedInfoText.setText(String.format(Locale.getDefault(), "Language %s, size %s",
//                language, StringUtils.getSizeString(repository.getSize() * 1024)));

        String createStr = (repository.isFork() ? getString(R.string.forked_at)
                : getString(R.string.created_at)) + " " + StringUtils.getDateStr(repository.getCreatedAt());
        if(repository.getPushedAt() != null){
            String updateStr = getString(R.string.latest_commit) + " "
                    + StringUtils.getNewsTimeStr(getActivity(), repository.getPushedAt());
            repoCreatedInfoText.setText(String.format("%s, %s", createStr, updateStr));
        } else {
            repoCreatedInfoText.setText(createStr);
        }

        if (repository.isFork() && repository.getParent() != null) {
            forkInfoText.setVisibility(View.VISIBLE);
            forkInfoText.setText(getString(R.string.forked_from)
                    .concat(" ").concat(repository.getParent().getFullName()));
        } else {
            forkInfoText.setVisibility(View.GONE);
        }

        String fullName = repository.getFullName();
        SpannableStringBuilder spannable = new SpannableStringBuilder(fullName);
        spannable.setSpan(new ForegroundColorSpan(ViewUtils.getAccentColor(getContext())),
                0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ProfileActivity.show(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getOwner().getAvatarUrl());
            }

            @Override
            public void updateDrawState(TextPaint ds) {

            }
        }, 0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        repoTitleText.setMovementMethod(LinkMovementMethod.getInstance());
        repoTitleText.setText(spannable);
    }

    //TODO fix scroll conflict when have tables
    @Override
    public void showReadMe(String source, String baseUrl) {
        if (!isReadmeSetted) {
            isReadmeSetted = true;
            webView.setMdSource(source, baseUrl, true);
            readmeLoader.setVisibility(View.VISIBLE);
            readmeLoader.setIndeterminate(false);
            webView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showReadMeLoader() {
        isReadmeSetted = false;
        readmeLoader.setVisibility(View.VISIBLE);
        readmeLoader.setIndeterminate(true);
        webView.setVisibility(View.GONE);
    }

    @Override
    public void showNoReadMe() {
        readmeTitle.setText(R.string.no_readme);
        readmeLoader.setVisibility(View.GONE);
    }

    @OnClick({R.id.issues_lay, R.id.stargazers_lay, R.id.froks_lay, R.id.watchers_lay,
            R.id.fork_info_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.issues_lay:
                IssuesActivity.showForRepo(getActivity(), mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getName());
//                showInfoToast(getString(R.string.developing));
                break;
            case R.id.stargazers_lay:
                if(mPresenter.getRepository().getStargazersCount() == 0) return;
                UserListActivity.show(getActivity(), UserListFragment.UserListType.STARGAZERS,
                        mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getName());
                break;
            case R.id.froks_lay:
                if(mPresenter.getRepository().getForksCount() == 0) return;
                RepoListActivity.showForks(getContext(),
                        mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getName());
                break;
            case R.id.watchers_lay:
                if(mPresenter.getRepository().getSubscribersCount() == 0) return;
                UserListActivity.show(getActivity(), UserListFragment.UserListType.WATCHERS,
                        mPresenter.getRepository().getOwner().getLogin(),
                        mPresenter.getRepository().getName());
                break;
            case R.id.fork_info_text:
                RepositoryActivity.show(getActivity(), mPresenter.getRepository().getParent());
                break;
        }
    }

    @Override
    public void onContentChanged(int progress) {
        if (readmeLoader != null) {
            readmeLoader.setProgress(progress);
            if (progress == 100) {
                readmeLoader.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onScrollChanged(boolean reachedTop, int scroll) {

    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onRepositoryInfoUpdated(Repository repository) {
        if(mPresenter == null){
            getArguments().putParcelable("repository", repository);
        }else{
            mPresenter.setRepository(repository);
            mPresenter.setLoaded(false);
            mPresenter.prepareLoadData();
        }
    }

    @Override
    public void onBranchChanged(Branch branch) {
        if(mPresenter == null){
            getArguments().putString("curBranch", branch.getName());
        }else{
            mPresenter.setCurBranch(branch.getName());
            mPresenter.setLoaded(false);
            mPresenter.prepareLoadData();
        }
    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        nestedScrollView.scrollTo(0, 0);
    }
}
