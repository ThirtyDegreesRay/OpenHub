

package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IUserListContract;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.UserListPresenter;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.UsersAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/16 17:40:20
 */

public class UserListFragment extends ListFragment<UserListPresenter, UsersAdapter>
        implements IUserListContract.View{

    public enum UserListType{
        STARGAZERS, WATCHERS, FOLLOWERS, FOLLOWING, SEARCH, ORG_MEMBERS, TRACE, BOOKMARK
    }

    public static UserListFragment create(@NonNull UserListType type, @NonNull String user,
                                          @NonNull String repo){
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(
                BundleHelper.builder()
                .put("type", type)
                .put("user", user)
                .put("repo", repo)
                .build()
        );
        return fragment;
    }

    public static UserListFragment createForSearch(@NonNull SearchModel searchModel){
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(
                BundleHelper.builder()
                        .put("type", UserListType.SEARCH)
                        .put("searchModel", searchModel)
                        .build()
        );
        return fragment;
    }

    public static UserListFragment createForTrace(){
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(BundleHelper.builder().put("type", UserListType.TRACE).build());
        return fragment;
    }

    public static UserListFragment createForBookmark(){
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(BundleHelper.builder().put("type", UserListType.BOOKMARK).build());
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
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
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadUsers(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_user);
    }

    @Override
    public void showUsers(ArrayList<User> users) {
        adapter.setData(users);
        postNotifyDataSetChanged();
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadUsers(page, false);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        View userAvatar = view.findViewById(R.id.avatar);
        ProfileActivity.show(getActivity(), userAvatar, adapter.getData().get(position).getLogin(),
                adapter.getData().get(position).getAvatarUrl());
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

}
