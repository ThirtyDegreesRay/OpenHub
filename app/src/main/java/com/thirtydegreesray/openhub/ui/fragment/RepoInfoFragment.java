/*
 *    Copyright 2017 ThirtyDegressRay
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

package com.thirtydegreesray.openhub.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.RepoInfoPresenter;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.RepoListActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.activity.UserListActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.webview.CodeWebView;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewHelper;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 11:35:39
 */

public class RepoInfoFragment extends BaseFragment<RepoInfoPresenter>
        implements IRepoInfoContract.View,
        CodeWebView.ContentChangedListener {

    public static RepoInfoFragment create(Repository repository) {
        RepoInfoFragment fragment = new RepoInfoFragment();
        fragment.setArguments(BundleBuilder.builder().put("repository", repository).build());
        return fragment;
    }

    @BindView(R.id.repo_title_text) TextView repoTitleText;
    @BindView(R.id.fork_info_text) TextView forkInfoText;
    @BindView(R.id.repo_desc_text) TextView repoDescText;
    @BindView(R.id.repo_code_text) TextView repoCodeText;
    @BindView(R.id.issues_num_text) TextView issuesNumText;
    @BindView(R.id.stargazers_num_text) TextView stargazersNumText;
    @BindView(R.id.forks_num_text) TextView forksNumText;
    @BindView(R.id.watchers_num_text) TextView watchersNumText;

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
        isReadmeSetted = false;
        webView.setContentChangedListener(this);
        readmeLoader.setVisibility(View.VISIBLE);
        readmeLoader.setIndeterminate(true);
        mPresenter.loadReadMe();
    }

    @Override
    public void showRepoInfo(Repository repository) {
        issuesNumText.setText(String.valueOf(repository.getOpenIssuesCount()));
        stargazersNumText.setText(String.valueOf(repository.getStargazersCount()));
        forksNumText.setText(String.valueOf(repository.getForksCount()));
        watchersNumText.setText(String.valueOf(repository.getSubscribersCount()));
        repoDescText.setText(repository.getDescription());
        String language = StringUtils.isBlank(repository.getLanguage()) ?
                getString(R.string.unknown) : repository.getLanguage();
        repoCodeText.setText(String.format(Locale.getDefault(), "Language %s, size %s",
                language, StringUtils.getSizeString(repository.getSize() * 1024)));

        if (repository.isFork() && repository.getParent() != null) {
            forkInfoText.setVisibility(View.VISIBLE);
            forkInfoText.setText(getString(R.string.forked_from)
                    .concat(" ").concat(repository.getParent().getFullName()));
        } else {
            forkInfoText.setVisibility(View.GONE);
        }

        String fullName = repository.getFullName();
        SpannableStringBuilder spannable = new SpannableStringBuilder(fullName);
        spannable.setSpan(new ForegroundColorSpan(ViewHelper.getAccentColor(getContext())),
                0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ProfileActivity.show(getContext(), mPresenter.getRepository().getOwner().getLogin());
            }

            @Override
            public void updateDrawState(TextPaint ds) {

            }
        }, 0, fullName.indexOf("/"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        repoTitleText.setMovementMethod(LinkMovementMethod.getInstance());
        repoTitleText.setText(spannable);
    }

    @Override
    public void showReadMe(String source, String baseUrl) {
        if (!isReadmeSetted) {
            isReadmeSetted = true;
            webView.setMdSource(source, baseUrl);
            readmeLoader.setVisibility(View.VISIBLE);
            readmeLoader.setIndeterminate(false);
        }
    }

    @OnClick({R.id.issues_lay, R.id.stargazers_lay, R.id.froks_lay, R.id.watchers_lay,
            R.id.fork_info_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.issues_lay:

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
                if(mPresenter.getRepository().getWatchersCount() == 0) return;
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
}
