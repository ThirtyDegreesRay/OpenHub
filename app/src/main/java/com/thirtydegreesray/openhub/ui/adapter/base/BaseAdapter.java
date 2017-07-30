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

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * 适配器基类
 * Created by ThirtyDegreesRay on 2016/7/27 19:49
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, D extends Object> extends RecyclerView.Adapter<VH>{

    //item点击回调
    private OnItemClickListener mOnItemClickListener;

    //item长按回调
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 数据列表
     */
    protected ArrayList<D> mData ;

    /**
     * 关联的activity
     */
    protected Context mContext;

    public BaseAdapter(Context context){
        mContext = context;
        handler = getHandler();
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(ArrayList<D> data){
        mData = data;
    }

    /**
     * 设置item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(getLayoutId(viewType), parent, false);
        VH viewHolder = getViewHolder(itemView, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }

        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 获取item布局文件id
     * @param viewType 类别
     * @return
     */
    protected abstract int getLayoutId(int viewType);

    /**
     * 获取ViewHolder
     * @param itemView
     * @param viewType
     * @return
     */
    protected abstract VH getViewHolder(View itemView, int viewType);

    /**
     * RecyclerView item点击监听
     */
    public interface OnItemClickListener{
        /**
         * RecyclerView item点击
         * @param position 位置
         */
        void onItemClick(int position);
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
        boolean onItemLongClick(int position);
    }

    private Handler handler ;

    private Handler getHandler(){
        return new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        notifyDataSetChanged();
                        break;
                    case 1:
                        notifyItemChanged((Integer) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    protected void postNotifyDataSetChanged(){
        Message message = handler.obtainMessage(0);
        message.sendToTarget();
    }

    protected void postNotifyItemChanged(int position){
        Message message = handler.obtainMessage(1, position);
        message.sendToTarget();
    }

    protected void showShortToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}
