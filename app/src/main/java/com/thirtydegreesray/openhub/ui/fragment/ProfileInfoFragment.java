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

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IProfileInfoContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.ProfileInfoPresenter;
import com.thirtydegreesray.openhub.ui.activity.RepoListActivity;
import com.thirtydegreesray.openhub.ui.activity.UserListActivity;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 14:39:19
 */

public class ProfileInfoFragment extends BaseFragment<ProfileInfoPresenter>
        implements IProfileInfoContract.View {

    @BindView(R.id.avatar) RoundedImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.joined_time) TextView joinedTime;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.link) TextView link;
    @BindView(R.id.followers_num_text) TextView followersNumText;
    @BindView(R.id.following_num_text) TextView followingNumText;
    @BindView(R.id.repos_num_text) TextView reposNumText;
    @BindView(R.id.gists_num_text) TextView gistsNumText;

    public static ProfileInfoFragment create(User user) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        fragment.setArguments(BundleBuilder.builder().put("user", user).build());
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile_info;
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
        ViewHelper.setLongClickCopy(email);
        ViewHelper.setLongClickCopy(link);
    }

    @OnClick({R.id.followers_lay, R.id.following_lay, R.id.repos_lay, R.id.gists_lay,
                R.id.email, R.id.link, R.id.avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                break;
            case R.id.followers_lay:
                UserListActivity.show(getActivity(), UserListFragment.UserListType.FOLLOWERS,
                        mPresenter.getUser().getLogin());
                break;
            case R.id.following_lay:
                UserListActivity.show(getActivity(), UserListFragment.UserListType.FOLLOWING,
                        mPresenter.getUser().getLogin());
                break;
            case R.id.repos_lay:
                RepoListActivity.show(getContext(), RepositoriesFragment.RepositoriesType.OWNED,
                        mPresenter.getUser().getLogin());
                break;
            case R.id.gists_lay:
                break;
            case R.id.email:
                AppHelper.launchEmail(getActivity(), mPresenter.getUser().getEmail());
                break;
            case R.id.link:
                AppHelper.openInBrowser(getActivity(), mPresenter.getUser().getBlog());
                break;
        }
    }

    @Override
    public void showProfileInfo(User user) {
        Picasso.with(getActivity())
                .load(user.getAvatarUrl())
                .into(avatar);
        name.setText(StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName());
        joinedTime.setText(getString(R.string.joined_at).concat(" ")
                .concat(StringUtils.getDateStr(user.getCreatedAt())));
        location.setText(user.getLocation());
        followersNumText.setText(String.valueOf(user.getFollowers()));
        followingNumText.setText(String.valueOf(user.getFollowing()));
        reposNumText.setText(String.valueOf(user.getPublicRepos()));
        gistsNumText.setText(String.valueOf(user.getPublicGists()));
        ViewHelper.setTextView(email, user.getEmail());
        ViewHelper.setTextView(link, user.getBlog());
    }

}
