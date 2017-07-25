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

package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class RepositoriesAdapter extends BaseAdapter<RepositoriesAdapter.ViewHolder, Repository> {

    @Inject
    public RepositoriesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_repository;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_user_avatar) ImageView ivUserAvatar;
        @BindView(R.id.tv_repo_name) TextView tvRepoName;
        @BindView(R.id.tv_repo_description) TextView tvRepoDescription;
        @BindView(R.id.tv_star_num) TextView tvStarNum;
        @BindView(R.id.tv_fork_num) TextView tvForkNum;
        @BindView(R.id.tv_owner_name) TextView tvOwnerName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Repository repository = mData.get(position);
        holder.tvRepoName.setText(repository.getName());
        holder.tvRepoDescription.setText(repository.getDescription());
        holder.tvStarNum.setText(String.valueOf(repository.getStargazersCount()));
        holder.tvForkNum.setText(String.valueOf(repository.getForksCount()));
        holder.tvOwnerName.setText(repository.getOwner().getLogin());
        Picasso.with(mContext)
                .load(repository.getOwner().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(holder.ivUserAvatar);

    }
}
