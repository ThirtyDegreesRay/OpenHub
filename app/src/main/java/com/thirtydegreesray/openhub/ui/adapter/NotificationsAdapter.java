

package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Notification;
import com.thirtydegreesray.openhub.mvp.model.NotificationSubject;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.adapter.base.DoubleTypesModel;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.ui.widget.ToastAbleImageButton;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;
import com.thirtydegreesray.openhub.util.WindowUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:05:32
 */

public class NotificationsAdapter extends BaseAdapter<BaseViewHolder,
        DoubleTypesModel<Repository, Notification>> {

    private NotificationAdapterListener listener;

    @Inject
    public NotificationsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    public void setListener(NotificationAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.layout_item_notification_repo;
        } else {
            return R.layout.layout_item_notification;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if(viewType == 0){
            return new RepoViewHolder(itemView);
        } else {
            return new NotificationViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getTypePosition();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if(viewHolder instanceof RepoViewHolder){
            RepoViewHolder holder = (RepoViewHolder) viewHolder;
            Repository model = data.get(position).getM1();
            holder.repoName.setText(model.getFullName());
            GlideApp.with(fragment)
                    .load(model.getOwner().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(holder.userAvatar);
        } else {
            NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
            Notification model = data.get(position).getM2();
            holder.title.setText(model.getSubject().getTitle());
            if(model.isUnread()){
                holder.status.setImageResource(R.drawable.ic_mark_unread);
                holder.status.setImageTintList(ColorStateList.valueOf(ViewUtils.getAccentColor(context)));
            } else {
                holder.status.setVisibility(View.INVISIBLE);
//                holder.status.setImageResource(R.drawable.ic_mark_readed);
//                holder.status.setImageTintList(ColorStateList.valueOf(ViewUtils.getSecondaryTextColor(context)));
            }
            holder.time.setText(StringUtils.getNewsTimeStr(context, model.getUpdateAt()));

            int padding = WindowUtil.dipToPx(context, 2);
            if(NotificationSubject.Type.Issue.equals(model.getSubject().getType())){
                holder.typeIcon.setImageResource(R.drawable.ic_issues);
                padding = 0;
            } else if(NotificationSubject.Type.Commit.equals(model.getSubject().getType())){
                holder.typeIcon.setImageResource(R.drawable.ic_commit);
            } else {
                holder.typeIcon.setImageResource(R.drawable.ic_pull);
            }
            holder.typeIcon.setPadding(padding, padding, padding, padding);

        }
    }

    class NotificationViewHolder extends BaseViewHolder {

        @BindView(R.id.type_icon) AppCompatImageView typeIcon;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.status) AppCompatImageView status;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class RepoViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.repo_name) TextView repoName;
        @BindView(R.id.mark_as_read_bn) ToastAbleImageButton markAsReadBn;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.user_avatar)
        public void onUserClicked() {
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                ProfileActivity.show((Activity) context, userAvatar,
                        getRepository().getOwner().getLogin(), getRepository().getOwner().getAvatarUrl());
            }
        }

        @OnClick(R.id.repo_name)
        public void onRepoClicked() {
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                RepositoryActivity.show(context, getRepository().getOwner().getLogin(),
                        getRepository().getName());
            }
        }

        @OnClick(R.id.mark_as_read_bn)
        public void onMarkAsReadClicked() {
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onRepoMarkAsReadClicked(data.get(getAdapterPosition()).getM1());
            }
        }

        private Repository getRepository(){
            return data.get(getAdapterPosition()).getM1();
        }

    }

    public interface NotificationAdapterListener{
        void onRepoMarkAsReadClicked(@NonNull Repository repository);
    }

}
