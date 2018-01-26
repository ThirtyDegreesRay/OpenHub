package com.thirtydegreesray.openhub.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.GlideApp;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.TraceExt;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.LanguageColorsHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/11/23 10:35:13
 */

public class TraceAdapter extends BaseAdapter<BaseViewHolder, TraceExt>
        implements StickyRecyclerHeadersAdapter<TraceAdapter.HeadViewHolder>{

    @Inject
    public TraceAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        if(viewType == 0){
            return R.layout.layout_item_user;
        } else {
            return R.layout.layout_item_repository;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TraceExt model = data.get(position);
        if(getItemViewType(position) == 0){
            User user = model.getUser();
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            GlideApp.with(fragment)
                    .load(user.getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(userViewHolder.avatar);
            userViewHolder.name.setText(user.getLogin());
            userViewHolder.name.setTextColor(ViewUtils.getAccentColor(context));
        } else {
            Repository repository = model.getRepository();
            RepoViewHolder repoViewHolder = (RepoViewHolder) holder;
            repoViewHolder.tvRepoName.setText(repository.getName());
            repoViewHolder.tvRepoDescription.setText(repository.getDescription());
            repoViewHolder.tvStarNum.setText(String.valueOf(repository.getStargazersCount()));
            repoViewHolder.tvForkNum.setText(String.valueOf(repository.getForksCount()));
            repoViewHolder.tvOwnerName.setText(repository.getOwner().getLogin());
            GlideApp.with(fragment)
                    .load(repository.getOwner().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(repoViewHolder.ivUserAvatar);
            repoViewHolder.forkMark.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);

            if(StringUtils.isBlank(repository.getLanguage())){
                repoViewHolder.tvLanguage.setText("");
                repoViewHolder.languageColor.setVisibility(View.INVISIBLE);
            } else {
                repoViewHolder.languageColor.setVisibility(View.VISIBLE);
                repoViewHolder.tvLanguage.setText(repository.getLanguage());
                int languageColor = LanguageColorsHelper.INSTANCE.getColor(context, repository.getLanguage());
                repoViewHolder.languageColor.setImageTintList(ColorStateList.valueOf(languageColor));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if("user".equals(data.get(position).getType())){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if(viewType == 0){
            return new UserViewHolder(itemView);
        } else {
            return new RepoViewHolder(itemView);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return data.get(position).getLatestDate().getTime();
    }

    @Override
    public HeadViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_trace_head, parent, false);
        return new HeadViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeadViewHolder holder, int position) {
        Date date = data.get(position).getLatestDate();
        String dateStr ;
        Date today = StringUtils.getTodayDate();
        long oneDay = 24 * 60 * 60 * 1000;
        if(date.getTime() == today.getTime()){
            dateStr = getString(R.string.today);
        } else if(date.getTime() == today.getTime() - oneDay){
            dateStr = getString(R.string.yesterday);
        } else {
            dateStr = StringUtils.getDateStr(date);
        }
        holder.headText.setText(dateStr);
        holder.itemView.setTag(date.getTime());
    }

    class HeadViewHolder extends BaseViewHolder{
        @BindView(R.id.head_text) TextView headText;
        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class UserViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.name) TextView name;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class RepoViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_user_avatar) ImageView ivUserAvatar;
        @BindView(R.id.language_color) ImageView languageColor;
        @BindView(R.id.tv_repo_name) TextView tvRepoName;
        @BindView(R.id.tv_language) TextView tvLanguage;
        @BindView(R.id.tv_repo_description) TextView tvRepoDescription;
        @BindView(R.id.tv_star_num) TextView tvStarNum;
        @BindView(R.id.tv_fork_num) TextView tvForkNum;
        @BindView(R.id.tv_owner_name) TextView tvOwnerName;
        @BindView(R.id.fork_mark) View forkMark;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.iv_user_avatar)
        public void onUserClick(){
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                ProfileActivity.show((Activity) context, ivUserAvatar,
                        data.get(getAdapterPosition()).getRepository().getOwner().getLogin(),
                        data.get(getAdapterPosition()).getRepository().getOwner().getAvatarUrl());
            }
        }
    }

}
