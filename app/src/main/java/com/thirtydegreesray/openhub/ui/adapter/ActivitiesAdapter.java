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
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/24 11:33:08
 */

public class ActivitiesAdapter extends BaseAdapter<ActivitiesAdapter.ViewHolder, Event> {

    @Inject
    public ActivitiesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_activity;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Event model = mData.get(position);
        Picasso.with(mContext)
                .load(model.getActor().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(holder.userAvatar);
        holder.userName.setText(model.getActor().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(mContext, model.getCreatedAt()));

        if(Event.EventType.WatchEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = model.getPayload().getAction() + " " + model.getRepo().getFullName();
            action = StringUtils.upCaseFisrtChar(action);
            holder.action.setText(action);
        } else if(Event.EventType.CreateEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = "Created repository " + model.getRepo().getFullName();
            holder.action.setText(action);
        } else if(Event.EventType.ForkEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String oriRepo = model.getRepo().getFullName();
            String newRepo = model.getActor().getLogin() + "/" + model.getRepo().getName();
            String action = "Forked " + oriRepo + " to " + newRepo;
            holder.action.setText(action);
        } else if(Event.EventType.PushEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String ref = model.getPayload().getRef();
            ref = ref.substring(ref.lastIndexOf("/") + 1);
            String action = "Push to " + ref +
                    " at " + model.getRepo().getFullName();
            holder.action.setText(action);
        } else if(Event.EventType.PullRequestEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = model.getPayload().getAction() + " pull request " + model.getRepo().getFullName();
            action = StringUtils.upCaseFisrtChar(action);
            holder.action.setText(action);
//            opened pull request MobDevGroup/AndroidResources#2
        } else if(Event.EventType.PullRequestReviewCommentEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = model.getPayload().getAction() + " pull request review comment at"
                    + model.getRepo().getFullName();
            action = StringUtils.upCaseFisrtChar(action);
            holder.action.setText(action);
//            opened pull request MobDevGroup/AndroidResources#2
        } else if(Event.EventType.PublicEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = "Made " + model.getRepo().getFullName() + "public";
            holder.action.setText(action);
        } else if(Event.EventType.IssuesEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = model.getPayload().getAction() + " issue at " +
                    model.getRepo().getFullName();
            action = StringUtils.upCaseFisrtChar(action);
            holder.action.setText(action);
        }else if(Event.EventType.IssueCommentEvent.equals(model.getType())){
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
            String action = model.getPayload().getAction() + " issue comment at " +
                     model.getRepo().getFullName();
            action = StringUtils.upCaseFisrtChar(action);
            holder.action.setText(action);
        } else {
            holder.action.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) RoundedImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.action) TextView action;
        @BindView(R.id.desc) TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        public void onUserClick(){
            ProfileActivity.show(mContext, mData.get(getAdapterPosition()).getActor().getLogin());
        }

    }

}
