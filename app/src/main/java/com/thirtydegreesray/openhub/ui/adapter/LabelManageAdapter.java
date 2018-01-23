package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2018/1/11 10:54:08
 */

public class LabelManageAdapter extends BaseAdapter<LabelManageAdapter.ViewHolder, Label> {

    @Inject
    public LabelManageAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_label;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Label model = data.get(position);
        if(model.isSelect()){
            holder.labelBg.setBackgroundColor(model.getColorValue());
            holder.name.getPaint().setFakeBoldText(true);
            holder.name.setTextColor(ViewUtils.getLabelTextColor(context, model.getColorValue()));
        } else {
            holder.labelBg.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.name.getPaint().setFakeBoldText(false);
            holder.name.setTextColor(ViewUtils.getPrimaryTextColor(context));
        }
        holder.color.setBackgroundColor(model.getColorValue());
        holder.name.setText(model.getName());
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.color) AppCompatImageView color;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.label_bg) LinearLayout labelBg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
