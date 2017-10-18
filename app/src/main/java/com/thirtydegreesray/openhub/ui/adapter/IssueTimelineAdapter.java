package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
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
 * Created by ThirtyDegreesRay on 2017/9/27 16:06:43
 */

public class IssueTimelineAdapter extends BaseAdapter<IssueTimelineAdapter.ViewHolder, IssueEvent> {

    @Inject
    public IssueTimelineAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_comments;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IssueEvent model = data.get(position);
        GlideApp.with(fragment)
                .load(model.getUser().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(holder.userAvatar);
        holder.userName.setText(model.getUser().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));
        if (!StringUtils.isBlank(model.getBodyHtml())) {
            holder.commentDesc.setText(Html.fromHtml(model.getBodyHtml()));
        } else if (!StringUtils.isBlank(model.getBody())) {
            holder.commentDesc.setText(model.getBody());
        } else {
            holder.commentDesc.setText(R.string.no_description);
        }
        //cause android:ellipsize="end" invalid
//        holder.commentDesc.setMovementMethod(LinkMovementMethod.getInstance());
        //TODO text bottom spacing too large
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.comment_desc) TextView commentDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        public void onUserClicked() {
            ProfileActivity.show((Activity) context, userAvatar, data.get(getAdapterPosition()).getUser().getLogin(),
                    data.get(getAdapterPosition()).getUser().getAvatarUrl());
        }

    }

}
