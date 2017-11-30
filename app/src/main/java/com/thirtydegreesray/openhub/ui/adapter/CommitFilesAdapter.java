

package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.CommitFile;
import com.thirtydegreesray.openhub.mvp.model.CommitFilesPathModel;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.adapter.base.DoubleTypesModel;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/10/18 15:09:19
 */

public class CommitFilesAdapter extends BaseAdapter<BaseViewHolder,
        DoubleTypesModel<CommitFilesPathModel, CommitFile>> {

    @Inject
    public CommitFilesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.layout_item_commit_files_path;
        } else {
            return R.layout.layout_item_commit_files;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if(viewType == 0){
            return new PathViewHolder(itemView);
        }else{
            return new FileViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getTypePosition();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (viewHolder instanceof PathViewHolder) {
            PathViewHolder holder = (PathViewHolder) viewHolder;
            CommitFilesPathModel model = data.get(position).getM1();
            holder.path.setText(model.getPath());
        } else {
            FileViewHolder holder = (FileViewHolder) viewHolder;
            CommitFile model = data.get(position).getM2();
            switch (model.getStatus()) {
                case added:
                    holder.statusType.setImageResource(R.drawable.ic_addition);
                    holder.statusType.setImageTintList(ColorStateList
                            .valueOf(ContextCompat.getColor(context, R.color.commit_file_added_color)));
                    break;
                case removed:
                    holder.statusType.setImageResource(R.drawable.ic_deletion);
                    holder.statusType.setImageTintList(ColorStateList
                            .valueOf(ContextCompat.getColor(context, R.color.commit_file_removed_color)));
                    break;
                case modified:
                    holder.statusType.setImageResource(R.drawable.ic_modify);
                    holder.statusType.setImageTintList(ColorStateList
                            .valueOf(ContextCompat.getColor(context, R.color.commit_file_modified_color)));
                    break;
                case renamed:
                    holder.statusType.setImageResource(R.drawable.ic_rename);
                    holder.statusType.setImageTintList(ColorStateList
                            .valueOf(ContextCompat.getColor(context, R.color.commit_file_renamed_color)));
                    break;
                default:
                    break;
            }
            holder.fileName.setText(model.getShortFileName());
            holder.additionsCount.setText(String.format(Locale.getDefault(), "+%d", model.getAdditions()));
            holder.deletionsCount.setText(String.format(Locale.getDefault(), "-%d", model.getDeletions()));
        }
    }

    class PathViewHolder extends BaseViewHolder {
        @BindView(R.id.path) TextView path;
        public PathViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class FileViewHolder extends BaseViewHolder {
        @BindView(R.id.status_type) AppCompatImageView statusType;
        @BindView(R.id.file_name) TextView fileName;
        @BindView(R.id.additions_count) TextView additionsCount;
        @BindView(R.id.deletions_count) TextView deletionsCount;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
