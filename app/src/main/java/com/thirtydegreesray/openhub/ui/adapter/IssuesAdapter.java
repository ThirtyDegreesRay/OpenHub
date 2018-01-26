package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:58:40
 */

public class IssuesAdapter extends BaseAdapter<IssuesAdapter.ViewHolder, Issue> {

    private boolean isUserIssues = false;

    @Inject
    public IssuesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    public void setUserIssues(boolean userIssues) {
        isUserIssues = userIssues;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_issue;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Issue model = data.get(position);
        GlideApp.with(fragment)
                .load(model.getUser().getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(holder.userAvatar);
        holder.userName.setText(model.getUser().getLogin());
        holder.issueTitle.setText(model.getTitle());
        holder.commentNum.setText(String.valueOf(model.getCommentNum()));
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));
        if(isUserIssues) {
            holder.repoFullName.setText(model.getRepoFullName().concat(" #").concat(String.valueOf(model.getNumber())));
        } else {
            holder.repoFullName.setText(("#").concat(String.valueOf(model.getNumber())));
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) ImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.issue_title) TextView issueTitle;
        @BindView(R.id.comment_num) TextView commentNum;
        @BindView(R.id.repo_full_name) TextView repoFullName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        public void onUserClick(){
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                Issue issue = data.get(getAdapterPosition());
                ProfileActivity.show((Activity) context, userAvatar, issue.getUser().getLogin(),
                        issue.getUser().getAvatarUrl());
            }
        }

    }

}
