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
