package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.CircleBackgroundImageView;
import com.thirtydegreesray.openhub.ui.widget.IssueLabelSpan;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;
import com.thirtydegreesray.openhub.util.WindowUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ThirtyDegreesRay on 2017/9/27 16:06:43
 */

public class IssueTimelineAdapter extends BaseAdapter<BaseViewHolder, IssueEvent> {

    private final int TYPE_HEAD_COMMENT = 1;
    private final int TYPE_COMMENT = 2;
    private final int TYPE_EVENT = 3;

    @Inject
    public IssueTimelineAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getParentIssue() != null){
            return TYPE_HEAD_COMMENT;
        }
        switch (data.get(position).getType()) {
            case commented:
                return TYPE_COMMENT;
            default:
                return TYPE_EVENT;
        }
    }

    @Override
    protected int getLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_HEAD_COMMENT:
                return R.layout.layout_item_head_comments;
            case TYPE_COMMENT:
                return R.layout.layout_item_comments;
            default:
                return R.layout.layout_item_event;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        switch (viewType) {
            case TYPE_HEAD_COMMENT:
                return new HeadCommentViewHolder(itemView);
            case TYPE_COMMENT:
                return new CommentViewHolder(itemView);
            default:
                return new EventViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IssueEvent model = data.get(position);
        switch (getItemViewType(position)) {
            case TYPE_COMMENT:
                ((CommentViewHolder) holder).setData(model);
                break;
            case TYPE_HEAD_COMMENT:
                ((HeadCommentViewHolder) holder).setData(model);
                break;
            default:
                ((EventViewHolder) holder).setData(model, position);
                break;
        }

    }

    class HeadCommentViewHolder extends CommentViewHolder{
        @BindView(R.id.issue_labels) TextView issueLabels;
        public HeadCommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        void setData(IssueEvent model) {
            super.setData(model);
            ArrayList<Label> labels = model.getParentIssue().getLabels();
            if(labels.size() == 0){
                issueLabels.setVisibility(View.GONE);
                return;
            } else {
                issueLabels.setVisibility(View.VISIBLE);
            }
            issueLabels.setText(ViewUtils.getLabelsSpan(context, labels));
        }
    }

    class CommentViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.comment_desc) TextView commentDesc;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar, R.id.user_name})
        public void onUserClicked() {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                ProfileActivity.show((Activity) context, userAvatar, data.get(getAdapterPosition()).getUser().getLogin(),
                        data.get(getAdapterPosition()).getUser().getAvatarUrl());
            }
        }

        void setData(IssueEvent model) {
            GlideApp.with(fragment)
                    .load(model.getUser().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(userAvatar);
            userName.setText(model.getUser().getLogin());
            time.setText(StringUtils.getNewsTimeStr(context, model.getCreatedAt()));
            if (!StringUtils.isBlank(model.getBodyHtml())) {
                commentDesc.setText(Html.fromHtml(model.getBodyHtml()));
            } else if (!StringUtils.isBlank(model.getBody())) {
                commentDesc.setText(model.getBody());
            } else {
                commentDesc.setText(R.string.no_description);
            }
            //cause android:ellipsize="end" invalid
//        commentDesc.setMovementMethod(LinkMovementMethod.getInstance());
            //TODO text bottom spacing too large
        }
    }

    class EventViewHolder extends BaseViewHolder{
        @BindView(R.id.root_lay) LinearLayout rootLay;
        @BindView(R.id.event_icon) CircleBackgroundImageView eventIcon;
        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.desc) TextView desc;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick({R.id.user_avatar})
        public void onUserClicked() {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                ProfileActivity.show((Activity) context, userAvatar, data.get(getAdapterPosition()).getActor().getLogin(),
                        data.get(getAdapterPosition()).getActor().getAvatarUrl());
            }
        }

        void setData(IssueEvent model, int position) {
            GlideApp.with(fragment)
                .load(model.getActor().getAvatarUrl())
                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                .into(userAvatar);
            setEventIcon(model);
            setDesc(model);
            setMargin(model, position);
        }

        void setDesc(IssueEvent model){
            SpannableStringBuilder text = new SpannableStringBuilder(model.getActor().getLogin());
            text.append(" ");
            String info;
            switch (model.getType()){
                case reopened:
                    text.append(getString(R.string.issue_reopened));
                    break;
                case closed:
                    text.append(getString(R.string.issue_close));
                    break;
                case renamed:
                    text.append(getString(R.string.issue_modified));
                    break;
                case locked:
                    text.append(getString(R.string.issue_locked_conversation));
                    break;
                case unlocked:
                    text.append(getString(R.string.issue_unlocked_conversation));
                    break;
                case crossReferenced:
                    if(model.getSource().getType() != null){
                        info = String.format(getString(R.string.issue_referenced),
                                "#" + model.getSource().getIssue().getTitle());
                        text.append(info);
                    }
                    break;
                case assigned:
                    info = String.format(getString(R.string.issue_assigned), model.getAssignee().getLogin());
                    text.append(info);
                    break;
                case unassigned:
                    text.append(getString(R.string.issue_unassigned));
                    break;
                case milestoned:
                    info = String.format(getString(R.string.issue_added_to_milestone),
                            model.getMilestone().getTitle());
                    text.append(info);
                    break;
                case demilestoned:
                    info = String.format(getString(R.string.issue_removed_from_milestone),
                            model.getMilestone().getTitle());
                    text.append(info);
                    break;
                case commentDeleted:
                    text.append(getString(R.string.issue_delete_comment));
                    break;
                case labeled:
                    info = String.format(getString(R.string.issue_add_label), "[label]");
                    text.append(info);
                    break;
                case unlabeled:
                    info = String.format(getString(R.string.issue_remove_label), "[label]");
                    text.append(info);
                    break;
                default:
//                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                    break;
            }

            int labelPos = text.toString().indexOf("[label]");
            Label label = model.getLabel();
            if(label != null && labelPos >= 0){
                text.replace(labelPos, labelPos + 7, label.getName());
                text.setSpan(new IssueLabelSpan(context, label),
                        labelPos, labelPos + label.getName().length(), 0);
            }

            String timeStr = StringUtils.getNewsTimeStr(context, model.getCreatedAt());
            text.append(" ").append(timeStr);
            desc.setText(text);
        }

        void setMargin(IssueEvent model, int position){
            int topMargin = 0;
            int bottomMargin = 0;
            if(position - 1 > 0 && data.get(position - 1).getType().equals(IssueEvent.Type.commented)){
                topMargin = (int) context.getResources().getDimension(R.dimen.spacing_mini);
            }
            if(position + 1 < data.size() && data.get(position +1).getType().equals(IssueEvent.Type.commented)){
                bottomMargin = (int) context.getResources().getDimension(R.dimen.spacing_mini);
            }
            if(data.get(position).getType().equals(IssueEvent.Type.closed)){
                bottomMargin = (int) context.getResources().getDimension(R.dimen.spacing_normal);
            }
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) rootLay.getLayoutParams();
            layoutParams.setMargins(0, topMargin, 0, bottomMargin);
            rootLay.setLayoutParams(layoutParams);
        }

        void setEventIcon(IssueEvent model){
            switch (model.getType()){
                case reopened:
                    eventIcon.setImageResource(R.drawable.ic_dot);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.commit_file_added_color));
                    setEventIconPadding(8);
                    break;
                case closed:
                    eventIcon.setImageResource(R.drawable.ic_block);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_red_800));
                    recoverEventIconPadding();
                    break;
                case renamed:
                    eventIcon.setImageResource(R.drawable.ic_edit);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;
                case locked:
                    eventIcon.setImageResource(R.drawable.ic_lock);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.black_light));
                    recoverEventIconPadding();
                    break;
                case unlocked:
                    eventIcon.setImageResource(R.drawable.ic_unlock);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.commit_file_added_color));
                    recoverEventIconPadding();
                    break;
                case crossReferenced:
                    eventIcon.setImageResource(R.drawable.ic_quote);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;
                case assigned:
                case unassigned:
                    eventIcon.setImageResource(R.drawable.ic_menu_person);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;
                case milestoned:
                case demilestoned:
                    eventIcon.setImageResource(R.drawable.ic_milestone);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;
                case commentDeleted:
                    eventIcon.setImageResource(R.drawable.ic_delete);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;
                case labeled:
                case unlabeled:
                    eventIcon.setImageResource(R.drawable.ic_label);
                    eventIcon.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.material_grey_500));
                    recoverEventIconPadding();
                    break;

                default:
                    eventIcon.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                    break;
            }
        }

        void recoverEventIconPadding(){
            setEventIconPadding(4);
        }

        void setEventIconPadding(int padding){
            int paddingInPx = WindowUtil.dipToPx(context, padding);
            eventIcon.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
        }

    }

}
