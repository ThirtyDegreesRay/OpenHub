

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

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;

import java.util.ArrayList;


/**
 * 适配器基类
 * Created by ThirtyDegreesRay on 2016/7/27 19:49
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, D extends Object>
        extends RecyclerView.Adapter<VH>
        implements View.OnClickListener, View.OnLongClickListener{

    //item点击回调
    private OnItemClickListener mOnItemClickListener;

    //item长按回调
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 数据列表
     */
    protected ArrayList<D> data;

    /**
     * 关联的activity
     */
    protected Context context;
    protected BaseFragment fragment;

    protected final int TAG_POSITION = R.id.position_tag;

    public BaseAdapter(Context context){
        this.context = context;
    }

    public BaseAdapter(Context context, BaseFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(ArrayList<D> data){
        this.data = data;
    }

    public ArrayList<D> getData() {
        return data;
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

        holder.itemView.setTag(TAG_POSITION, position);

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(this);
        }

        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
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

    protected void showShortToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(getPositionByView(v), v);
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener.onItemLongClick(getPositionByView(v), v);
    }

    protected int getPositionByView(View view){
        return (int) view.getTag(TAG_POSITION);
    }

}
