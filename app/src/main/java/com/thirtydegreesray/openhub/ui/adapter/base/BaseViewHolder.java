

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * BasicViewHolder
 * Created by ThirtyDegreesRay on 2016/7/27 20:20
 */
public class BaseViewHolder extends RecyclerView.ViewHolder{

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
