

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;

import java.util.ArrayList;


/**
 * 适配器基类
 * Created by ThirtyDegreesRay on 2016/7/27 19:49
 */
public abstract class BaseAdapter<VH extends BaseViewHolder, D extends Object>
        extends RecyclerView.Adapter<VH>
        implements BaseViewHolder.OnItemClickListener, BaseViewHolder.OnItemLongClickListener{

    //item点击回调
    private BaseViewHolder.OnItemClickListener mOnItemClickListener;

    //item长按回调
    private BaseViewHolder.OnItemLongClickListener mOnItemLongClickListener;

    /**
     * 数据列表
     */
    protected ArrayList<D> data;

    /**
     * 关联的activity
     */
    protected Context context;
    protected BaseFragment fragment;

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
    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(BaseViewHolder.OnItemLongClickListener onItemLongClickListener) {
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
            holder.setOnItemClickListener(this);
        }

        if(mOnItemLongClickListener != null){
            holder.setOnItemLongClickListener(this);
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



    protected void showShortToast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        mOnItemClickListener.onItemClick(position, view);
    }

    @Override
    public boolean onItemLongClick(int position, @NonNull View view) {
        return mOnItemLongClickListener.onItemLongClick(position, view);
    }

    @NonNull protected String getString(@StringRes int resId){
        return context.getString(resId);
    }

}
