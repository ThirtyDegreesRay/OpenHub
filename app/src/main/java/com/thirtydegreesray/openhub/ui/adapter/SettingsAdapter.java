

package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.SettingModel;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/8/2.
 *
 * @author ThirtyDegreesRay
 */

public class SettingsAdapter extends BaseAdapter<SettingsAdapter.ViewHolder, SettingModel> {

    private ItemEventListener itemEventListener;
    private final int TAG_POSITION = R.id.position_tag;

    @Inject
    public SettingsAdapter(Context context) {
        super(context);
    }

    public void setItemEventListener(@NonNull ItemEventListener itemEventListener) {
        this.itemEventListener = itemEventListener;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_row;
    }

    @Override
    protected ViewHolder getViewHolder(final View itemView, int viewType) {
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.switchBn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag(TAG_POSITION);
                itemEventListener.onSwitchCheckedChanged(position, isChecked);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SettingModel model = data.get(position);
        holder.iconImage.setImageResource(model.getIconResId());
        holder.titleText.setText(model.getTitle());
        if(StringUtils.isBlank(model.getDesc())){
            holder.descText.setVisibility(View.GONE);
        }else{
            holder.descText.setVisibility(View.VISIBLE);
            holder.descText.setText(model.getDesc());
        }
        holder.switchBn.setTag(TAG_POSITION, position);
        holder.switchBn.setVisibility(model.isSwitchEnable() ? View.VISIBLE : View.GONE);
        holder.switchBn.setChecked(model.isSwitchChecked());
    }

    public class ViewHolder extends BaseViewHolder{

        @BindView(R.id.icon_image) AppCompatImageView iconImage;
        @BindView(R.id.title_text) TextView titleText;
        @BindView(R.id.desc_text) TextView descText;
        @BindView(R.id.switch_bn) Switch switchBn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface ItemEventListener{
        void onSwitchCheckedChanged(int position, boolean isChecked);
    }

}
