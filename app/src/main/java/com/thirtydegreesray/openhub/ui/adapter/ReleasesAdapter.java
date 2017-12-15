package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 12:14:02
 */

public class ReleasesAdapter extends BaseAdapter<ReleasesAdapter.ViewHolder, Release> {

    @Inject
    public ReleasesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_release;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Release model = data.get(position);
        holder.releaseName.setText(model.getTagName());
        holder.time.setText(StringUtils.getNewsTimeStr(context, model.getPublishedAt()));
        if(!StringUtils.isBlank(model.getBodyHtml())){
            holder.body.setText(Html.fromHtml(model.getBodyHtml()));
            holder.body.setVisibility(View.VISIBLE);
        } else {
            holder.body.setText("");
            holder.body.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.release_name) TextView releaseName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.body) TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
