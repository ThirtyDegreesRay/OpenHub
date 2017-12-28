

package com.thirtydegreesray.openhub.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IProfileInfoContract;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.ProfileInfoPresenter;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.RepoListActivity;
import com.thirtydegreesray.openhub.ui.activity.UserListActivity;
import com.thirtydegreesray.openhub.ui.adapter.UsersAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 14:39:19
 */

public class ProfileInfoFragment extends BaseFragment<ProfileInfoPresenter>
        implements IProfileInfoContract.View,
        BaseViewHolder.OnItemClickListener{

    @BindView(R.id.name) TextView name;
    @BindView(R.id.bio) TextView bio;
    @BindView(R.id.company) TextView company;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.link) TextView link;

    @BindView(R.id.members_lay) LinearLayout membersLay;
    @BindView(R.id.followers_lay) LinearLayout followersLay;
    @BindView(R.id.following_lay) LinearLayout followingLay;
    @BindView(R.id.gists_lay) LinearLayout gistsLay;
    @BindView(R.id.followers_num_text) TextView followersNumText;
    @BindView(R.id.following_num_text) TextView followingNumText;
    @BindView(R.id.repos_num_text) TextView reposNumText;
    @BindView(R.id.gists_num_text) TextView gistsNumText;

    @BindView(R.id.orgs_lay) CardView orgsLay;
    @BindView(R.id.orgs_recycler_view) RecyclerView orgsRecyclerView;

    @Inject UsersAdapter orgsAdapter;

    public static ProfileInfoFragment create(User user) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        fragment.setArguments(BundleHelper.builder().put("user", user).build());
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
        ViewUtils.setLongClickCopy(email);
        ViewUtils.setLongClickCopy(link);
        orgsLay.setVisibility(View.GONE);
    }

    @OnClick({R.id.followers_lay, R.id.following_lay, R.id.repos_lay, R.id.gists_lay,
                R.id.email, R.id.link, R.id.members_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.followers_lay:
                if(mPresenter.getUser().getFollowers() == 0) return;
                UserListActivity.show(getActivity(), UserListFragment.UserListType.FOLLOWERS,
                        mPresenter.getUser().getLogin());
                break;
            case R.id.following_lay:
                if(mPresenter.getUser().getFollowing() == 0) return;
                UserListActivity.show(getActivity(), UserListFragment.UserListType.FOLLOWING,
                        mPresenter.getUser().getLogin());
                break;
            case R.id.repos_lay:
                if(mPresenter.getUser().getPublicRepos() == 0) return;
                RepositoriesFragment.RepositoriesType type =
                        AppData.INSTANCE.getLoggedUser().getLogin().equals(mPresenter.getUser().getLogin()) ?
                        RepositoriesFragment.RepositoriesType.OWNED : RepositoriesFragment.RepositoriesType.PUBLIC;
                RepoListActivity.show(getContext(), type, mPresenter.getUser().getLogin());
                break;
            case R.id.gists_lay:
//                if(mPresenter.getUser().getPublicGists() == 0) return;
                showInfoToast(getString(R.string.developing));
                break;
            case R.id.email:
                AppOpener.launchEmail(getActivity(), mPresenter.getUser().getEmail());
                break;
            case R.id.link:
                AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getUser().getBlog());
                break;
            case R.id.members_lay:
                UserListActivity.show(getActivity(), UserListFragment.UserListType.ORG_MEMBERS,
                        mPresenter.getUser().getLogin());
                break;
        }
    }

    @Override
    public void showProfileInfo(User user) {

        String nameStr = StringUtils.isBlank(user.getName()) ? user.getLogin() : user.getName();
        nameStr = user.isUser() ? nameStr : nameStr.concat("(ORG)");
        name.setText(nameStr);

        bio.setText(user.getBio());
        bio.setVisibility(StringUtils.isBlank(user.getBio()) ? View.GONE :View.VISIBLE);

        followersNumText.setText(String.valueOf(user.getFollowers()));
        followingNumText.setText(String.valueOf(user.getFollowing()));
        reposNumText.setText(String.valueOf(user.getPublicRepos()));
        gistsNumText.setText(String.valueOf(user.getPublicGists()));
        if(!user.isUser()){
            membersLay.setVisibility(View.VISIBLE);
            followersLay.setVisibility(View.GONE);
            followingLay.setVisibility(View.GONE);
            gistsLay.setVisibility(View.GONE);
        }else {
            membersLay.setVisibility(View.GONE);
        }

        ViewUtils.setTextView(company, user.getCompany());
        ViewUtils.setTextView(email, user.getEmail());
        ViewUtils.setTextView(link, user.getBlog());
    }

    @Override
    public void showUserOrgs(ArrayList<User> orgs) {
        if(orgsRecyclerView.getAdapter() == null){
            orgsLay.setVisibility(View.VISIBLE);
            orgsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            orgsRecyclerView.setAdapter(orgsAdapter);
            orgsAdapter.setOnItemClickListener(this);
            orgsAdapter.setCardEnable(false);
        }
        orgsAdapter.setData(orgs);
        orgsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        View userAvatar = view.findViewById(R.id.avatar);
        ProfileActivity.show((Activity) getContext(), userAvatar, orgsAdapter.getData().get(position).getLogin(),
                orgsAdapter.getData().get(position).getAvatarUrl());
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    public void updateProfileInfo(User user){
        mPresenter.setUser(user);
        showProfileInfo(user);
    }

}
