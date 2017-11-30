

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * BasicViewHolder
 * Created by ThirtyDegreesRay on 2016/7/27 20:20
 */
public class BaseViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener{

    //item点击回调
    private OnItemClickListener mOnItemClickListener;

    //item长按回调
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
            mOnItemClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mOnItemLongClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
            return mOnItemLongClickListener.onItemLongClick(getAdapterPosition(), v);
        }
        return false;
    }

    /**
     * RecyclerView item点击监听
     */
    public interface OnItemClickListener{
        /**
         * RecyclerView item点击
         * @param position 位置
         */
        void onItemClick(int position, @NonNull View view);
    }

    /**
     * RecyclerView item长按监听
     */
    public interface OnItemLongClickListener{
        /**
         * RecyclerView item长按
         * @param position 位置
         * @return
         */
        boolean onItemLongClick(int position, @NonNull View view);
    }

}
