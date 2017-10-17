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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.util.ViewHelper;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 22:50:29
 */

public class BranchesAdapter extends BaseAdapter<BranchesAdapter.ViewHolder, Branch> {

    private String curBranch;

    public BranchesAdapter(Context context, String curBranch) {
        super(context);
        this.curBranch = curBranch;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_branch;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Branch branch = data.get(position);
        holder.icon.setImageResource(branch.isBranch() ? R.drawable.ic_branch : R.drawable.ic_tag);
        holder.name.setText(branch.getName());
        if(branch.getName().equals(curBranch)){
            holder.rootLayout.setBackgroundColor(ViewHelper.getSelectedColor(context));
        }else{
            holder.rootLayout.setBackground(null);
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.root_layout) LinearLayout rootLayout;
        @BindView(R.id.icon) AppCompatImageView icon;
        @BindView(R.id.name) TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
