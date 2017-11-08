

package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 15:53:21
 */

public class RepoFilesAdapter extends BaseAdapter<RepoFilesAdapter.ViewHolder, FileModel> {

    @Inject
    public RepoFilesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_file;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FileModel model = data.get(position);
        holder.fileName.setText(model.getName());
        if(model.isFile()){
            holder.fileType.setImageResource(R.drawable.ic_file);
            holder.fileSize.setText(StringUtils.getSizeString(model.getSize()));
        }else{
            holder.fileType.setImageResource(R.drawable.ic_folder);
            holder.fileSize.setText("");
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.file_type) AppCompatImageView fileType;
        @BindView(R.id.file_name) TextView fileName;
        @BindView(R.id.file_size) TextView fileSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

}
