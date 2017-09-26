package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;

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
                .placeholder(R.mipmap.logo)
                .into(holder.userAvatar);
        holder.userName.setText(model.getUser().getLogin());
        holder.issueTitle.setText(model.getTitle());
        holder.commentNum.setText(String.valueOf(model.getCommentNum()));
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));
        holder.repoFullName.setVisibility(isUserIssues ? View.VISIBLE : View.INVISIBLE);
        if(isUserIssues) holder.repoFullName.setText(model.getRepoFullName());
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) RoundedImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.issue_title) TextView issueTitle;
        @BindView(R.id.comment_num) TextView commentNum;
        @BindView(R.id.repo_full_name) TextView repoFullName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

}
