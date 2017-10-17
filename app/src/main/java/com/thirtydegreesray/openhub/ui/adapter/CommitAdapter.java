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

package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 15:04:24
 */

public class CommitAdapter extends BaseAdapter<CommitAdapter.ViewHolder, RepoCommit> {

    @Inject
    public CommitAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_commit;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RepoCommit model = data.get(position);
        if(model.getAuthor() != null){
            GlideApp.with(fragment)
                    .load(model.getAuthor().getAvatarUrl())
                    .placeholder(R.mipmap.logo)
                    .into(holder.userAvatar);
        } else {
            holder.userAvatar.setImageResource(R.drawable.ic_question);
        }

        holder.userName.setText(model.getAuthor() == null ?
                model.getCommit().getAuthor().getName() : model.getAuthor().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCommit().getAuthor().getDate()));
        holder.commitMessage.setText(model.getCommit().getMessage());
        holder.commitBriefSha.setText(model.getSha().substring(0, 7));
        holder.commentNum.setText(String.valueOf(model.getCommit().getCommentCount()));
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.commit_message) TextView commitMessage;
        @BindView(R.id.commit_brief_sha) TextView commitBriefSha;
        @BindView(R.id.comment_num) TextView commentNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        void onUserClick() {
            RepoCommit commit = data.get(getAdapterPosition());
            String loginId = commit.getAuthor() == null ?
                    commit.getCommit().getAuthor().getName() : commit.getAuthor().getLogin();
            String userAvatar = commit.getAuthor() == null ?
                    null : commit.getAuthor().getAvatarUrl();
            ProfileActivity.show((Activity)context, ViewHolder.this.userAvatar, loginId, userAvatar);
        }

    }

}
