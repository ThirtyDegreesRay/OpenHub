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

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.GitHubHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.regex.Matcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/8/24 11:33:08
 */

public class ActivitiesAdapter extends BaseAdapter<ActivitiesAdapter.ViewHolder, Event> {

    @Inject
    public ActivitiesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
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
        Event model = data.get(position);
        GlideApp.with(fragment)
                .load(model.getActor().getAvatarUrl())
                .placeholder(R.mipmap.logo)
                .into(holder.userAvatar);
        holder.userName.setText(model.getActor().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));

        holder.setActionAndDesc(model);
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
        void onUserClick() {
            ProfileActivity.show(context, data.get(getAdapterPosition()).getActor().getLogin());
        }

        //TODO to be better event action and desc
        void setActionAndDesc(Event model) {
            String actionStr = null;
            switch (model.getType()) {
                case CommitCommentEvent:
                    actionStr = "Commit comment at " + model.getRepo().getFullName();
                    break;
                case CreateEvent:
                    if (model.getPayload().getRefType().equals("repository")) {
                        actionStr = "Created repository " + model.getRepo().getFullName();
                    } else {
                        actionStr = "Created " + model.getPayload().getRefType() + " "
                                + model.getPayload().getRef() + " at "
                                + model.getRepo().getFullName();
                    }
                    break;
                case DeleteEvent:
                    actionStr = "Delete " + model.getPayload().getRefType() + " " + model.getPayload().getRef()
                            + " at " + model.getRepo().getFullName();
                    break;
                case ForkEvent:
                    String oriRepo = model.getRepo().getFullName();
                    String newRepo = model.getActor().getLogin() + "/" + model.getRepo().getName();
                    actionStr = "Forked " + oriRepo + " to " + newRepo;
                    break;
                case GollumEvent:
                    actionStr = model.getPayload().getAction() + " a wiki page ";
                    break;

                case InstallationEvent:
                    actionStr = model.getPayload().getAction() + " an GitHub App ";
                    break;
                case InstallationRepositoriesEvent:
                    actionStr = model.getPayload().getAction() + " repository from an installation ";
                    break;
                case IssueCommentEvent:
                    actionStr = model.getPayload().getAction() + " issue comment at " +
                            model.getRepo().getFullName();
                    break;
                case IssuesEvent:
                    actionStr = model.getPayload().getAction() + " issue at " +
                            model.getRepo().getFullName();
                    break;

                case MarketplacePurchaseEvent:
                    actionStr = model.getPayload().getAction() + " marketplace plan ";
                    break;
                case MemberEvent:
                    actionStr = model.getPayload().getAction() + " member to " +
                            model.getRepo().getFullName();
                    break;
                case OrgBlockEvent:
                    actionStr = model.getPayload().getAction() + " a user ";
                    break;
                case ProjectCardEvent:
                    actionStr = model.getPayload().getAction() + " a project ";
                    break;
                case ProjectColumnEvent:
                    actionStr = model.getPayload().getAction() + " a project ";
                    break;

                case ProjectEvent:
                    actionStr = model.getPayload().getAction() + " a project ";
                    break;
                case PublicEvent:
                    actionStr = "Made " + model.getRepo().getFullName() + "public";
                    break;
                case PullRequestEvent:
                    actionStr = model.getPayload().getAction() + " pull request " + model.getRepo().getFullName();
                    break;
                case PullRequestReviewEvent:
                    actionStr = model.getPayload().getAction() + " pull request review at"
                            + model.getRepo().getFullName();
                    break;
                case PullRequestReviewCommentEvent:
                    actionStr = model.getPayload().getAction() + " pull request review comment at"
                            + model.getRepo().getFullName();
                    break;

                case PushEvent:
                    String ref = model.getPayload().getRef();
                    ref = ref.substring(ref.lastIndexOf("/") + 1);
                    actionStr = "Push to " + ref +
                            " at " + model.getRepo().getFullName();
                    break;
                case ReleaseEvent:
                    actionStr = model.getPayload().getAction() + " release " +
                            model.getPayload().getRelease().getTagName() + " at " +
                            model.getRepo().getFullName();
                    break;
                case WatchEvent:
                    actionStr = model.getPayload().getAction() + " " + model.getRepo().getFullName();
                    break;
            }

            action.setVisibility(View.VISIBLE);
            desc.setVisibility(View.GONE);

            actionStr = StringUtils.upCaseFisrtChar(actionStr);
            actionStr = actionStr == null ? "" : actionStr;
            SpannableStringBuilder span = new SpannableStringBuilder(actionStr);
            Matcher matcher = GitHubHelper.REPO_FULL_NAME_PATTERN.matcher(actionStr);
            for (; matcher.find(); ) {
                span.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            action.setText(span);

        }

    }

}
