

package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.model.EventPayload;
import com.thirtydegreesray.openhub.mvp.model.PushEventCommit;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.EllipsizeLineSpan;
import com.thirtydegreesray.openhub.util.GitHubHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
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
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(holder.userAvatar);
        holder.userName.setText(model.getActor().getLogin());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));

        holder.setActionAndDesc(model);
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) ImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.action) TextView action;
        @BindView(R.id.desc) TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        void onUserClick() {
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                String loginId = data.get(getAdapterPosition()).getActor().getLogin();
                String userAvatar = data.get(getAdapterPosition()).getActor().getAvatarUrl();
                ProfileActivity.show((Activity) context, ViewHolder.this.userAvatar, loginId, userAvatar);
            }
        }

        //TODO to be better event action and desc
        void setActionAndDesc(Event model) {
            String actionStr = null;
            SpannableStringBuilder descSpan = null;
            String fullName = model.getRepo() != null ? model.getRepo().getFullName() : null;
            EventPayload.RefType refType = model.getPayload().getRefType();
            String action = model.getPayload() != null ? model.getPayload().getAction() : null;

            switch (model.getType()) {
                case CommitCommentEvent:
                    actionStr = String.format(getString(R.string.created_comment_on_commit), fullName);
                    descSpan = new SpannableStringBuilder(model.getPayload().getComment().getBody());
                    break;
                case CreateEvent:
                    if (EventPayload.RefType.repository.equals(refType)) {
                        actionStr = String.format(getString(R.string.created_repo), fullName);
                    } else if (EventPayload.RefType.branch.equals(refType)) {
                        actionStr = String.format(getString(R.string.created_branch_at),
                                model.getPayload().getRef(), fullName);
                    } else if (EventPayload.RefType.tag.equals(refType))  {
                        actionStr = String.format(getString(R.string.created_tag_at),
                                model.getPayload().getRef(), fullName);
                    }
                    break;
                case DeleteEvent:
                    if (EventPayload.RefType.branch.equals(refType)) {
                        actionStr = String.format(getString(R.string.delete_branch_at),
                                model.getPayload().getRef(), fullName);
                    } else if (EventPayload.RefType.tag.equals(refType))  {
                        actionStr = String.format(getString(R.string.delete_tag_at),
                                model.getPayload().getRef(), fullName);
                    }
                    break;
                case ForkEvent:
                    String oriRepo = model.getRepo().getFullName();
                    String newRepo = model.getActor().getLogin() + "/" + model.getRepo().getName();
                    actionStr = String.format(getString(R.string.forked_to), oriRepo, newRepo);
                    break;
                case GollumEvent:
                    actionStr = action + " a wiki page ";
                    break;

                case InstallationEvent:
                    actionStr = action + " an GitHub App ";
                    break;
                case InstallationRepositoriesEvent:
                    actionStr = action + " repository from an installation ";
                    break;
                case IssueCommentEvent:
                    actionStr = String.format(getString(R.string.created_comment_on_issue),
                            model.getPayload().getIssue().getNumber(), model.getRepo().getFullName());
                    descSpan = new SpannableStringBuilder(model.getPayload().getComment().getBody());
                    break;
                case IssuesEvent:
                    String issueEventStr = getIssueEventStr(action);
                    actionStr = String.format(issueEventStr,
                            model.getPayload().getIssue().getNumber(), model.getRepo().getFullName());
                    descSpan = new SpannableStringBuilder(model.getPayload().getIssue().getTitle());
                    break;

                case MarketplacePurchaseEvent:
                    actionStr = action + " marketplace plan ";
                    break;
                case MemberEvent:
                    String memberEventStr = getMemberEventStr(action);
                    actionStr = String.format(memberEventStr,
                            model.getPayload().getMember().getLogin(), fullName);
                    break;
                case OrgBlockEvent:
                    String orgBlockEventStr ;
                    if(EventPayload.OrgBlockEventActionType.blocked.name().equals(action)){
                        orgBlockEventStr = getString(R.string.org_blocked_user);
                    }else{
                        orgBlockEventStr = getString(R.string.org_unblocked_user);
                    }
                    actionStr = String.format(orgBlockEventStr,
                            model.getPayload().getOrganization().getLogin(),
                            model.getPayload().getBlockedUser().getLogin());
                    break;
                case ProjectCardEvent:
                    actionStr = action + " a project ";
                    break;
                case ProjectColumnEvent:
                    actionStr = action + " a project ";
                    break;

                case ProjectEvent:
                    actionStr = action + " a project ";
                    break;
                case PublicEvent:
                    actionStr = String.format(getString(R.string.made_repo_public), fullName);
                    break;
                case PullRequestEvent:
                    actionStr = action + " pull request " + model.getRepo().getFullName();
                    break;
                case PullRequestReviewEvent:
                    String pullRequestReviewStr = getPullRequestReviewEventStr(action);
                    actionStr = String.format(pullRequestReviewStr, fullName);
                    break;
                case PullRequestReviewCommentEvent:
                    String pullRequestCommentStr = getPullRequestReviewCommentEventStr(action);
                    actionStr = String.format(pullRequestCommentStr, fullName);
                    descSpan = new SpannableStringBuilder(model.getPayload().getComment().getBody());
                    break;

                case PushEvent:
                    String branch = model.getPayload().getBranch();
                    actionStr = String.format(getString(R.string.push_to), branch, fullName);

                    descSpan = new SpannableStringBuilder("");
                    int count = model.getPayload().getCommits().size();
                    int maxLines = 4;
                    int max = count > maxLines ? maxLines - 1 : count;

                    for (int i = 0; i < max; i++) {
                        PushEventCommit commit = model.getPayload().getCommits().get(i);
                        if (i != 0) {
                            descSpan.append("\n");
                        }

                        int lastLength = descSpan.length();
                        String sha = commit.getSha().substring(0, 7);
                        descSpan.append(sha);
                        descSpan.setSpan(new TextAppearanceSpan(context, R.style.text_link),
                                lastLength, lastLength + sha.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        descSpan.append(" ");
                        descSpan.append(getFirstLine(commit.getMessage()));

                        descSpan.setSpan(new EllipsizeLineSpan(i == (count - 1) ? 0 : 0),
                                lastLength, descSpan.length(), 0);
                    }
                    if(count > maxLines){
                        descSpan.append("\n").append("...");
                    }
                    break;
                case ReleaseEvent:
                    actionStr = String.format(getString(R.string.published_release_at),
                            model.getPayload().getRelease().getTagName(), fullName);
                    break;
                case WatchEvent:
                    actionStr = String.format(getString(R.string.starred_repo), fullName);
                    break;
            }

            this.action.setVisibility(View.VISIBLE);
            if(descSpan != null){
                desc.setVisibility(View.VISIBLE);
                desc.setText(descSpan);
            }else{
                desc.setVisibility(View.GONE);
            }

            actionStr = StringUtils.upCaseFirstChar(actionStr);
            actionStr = actionStr == null ? "" : actionStr;
            SpannableStringBuilder span = new SpannableStringBuilder(actionStr);
            Matcher matcher = GitHubHelper.REPO_FULL_NAME_PATTERN.matcher(actionStr);
            for (; matcher.find(); ) {
                span.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            this.action.setText(span);
        }

        private String getFirstLine(String str){
            if(str == null || !str.contains("\n")) return str;
            return str.substring(0, str.indexOf("\n"));
        }

        private String getPullRequestReviewEventStr(String action){
            EventPayload.PullRequestReviewEventActionType actionType =
                    EventPayload.PullRequestReviewEventActionType.valueOf(action);
            switch (actionType){
                case submitted:
                    return getString(R.string.submitted_pull_request_review_at);
                case edited:
                    return getString(R.string.edited_pull_request_review_at);
                case dismissed:
                    return getString(R.string.dismissed_pull_request_review_at);
                default:
                    return getString(R.string.submitted_pull_request_review_at);
            }
        }

        private String getPullRequestReviewCommentEventStr(String action){
            EventPayload.PullRequestReviewCommentEventActionType actionType =
                    EventPayload.PullRequestReviewCommentEventActionType.valueOf(action);
            switch (actionType){
                case created:
                    return getString(R.string.created_pull_request_comment_at);
                case edited:
                    return getString(R.string.edited_pull_request_comment_at);
                case deleted:
                    return getString(R.string.deleted_pull_request_comment_at);
                default:
                    return getString(R.string.created_pull_request_comment_at);
            }
        }

        private String getMemberEventStr(String action){
            EventPayload.MemberEventActionType actionType = EventPayload.MemberEventActionType.valueOf(action);
            switch (actionType){
                case added:
                    return getString(R.string.added_member_to);
                case deleted:
                    return getString(R.string.deleted_member_at);
                case edited:
                    return getString(R.string.edited_member_at);
                default:
                    return getString(R.string.added_member_to);
            }
        }

        private String getIssueEventStr(String action){
            EventPayload.IssueEventActionType actionType = EventPayload.IssueEventActionType.valueOf(action);
            switch (actionType){
                case assigned:
                    return getString(R.string.assigned_issue_at);
                case unassigned:
                    return getString(R.string.unassigned_issue_at);
                case labeled:
                    return getString(R.string.labeled_issue_at);
                case unlabeled:
                    return getString(R.string.unlabeled_issue_at);
                case opened:
                    return getString(R.string.opened_issue_at);

                case edited:
                    return getString(R.string.edited_issue_at);
                case milestoned:
                    return getString(R.string.milestoned_issue_at);
                case demilestoned:
                    return getString(R.string.demilestoned_issue_at);
                case closed:
                    return getString(R.string.closed_issue_at);
                case reopened:
                    return getString(R.string.reopened_issue_at);

                default:
                    return getString(R.string.opened_issue_at);
            }
        }

    }

}
