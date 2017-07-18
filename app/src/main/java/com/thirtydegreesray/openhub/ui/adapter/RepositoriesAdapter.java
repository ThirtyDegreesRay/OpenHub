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

import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class RepositoriesAdapter extends BaseAdapter<RepositoriesAdapter.ViewHolder, Repository> {


    public RepositoriesAdapter(BaseActivity activity) {
        super(activity);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_repository;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    public class ViewHolder extends BaseViewHolder{

        @BindView(R.id.name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.name.setText(mData.get(position).getName());
    }
}
