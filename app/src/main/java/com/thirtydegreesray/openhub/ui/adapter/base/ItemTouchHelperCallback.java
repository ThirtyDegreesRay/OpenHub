package com.thirtydegreesray.openhub.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 17:10:50
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private ItemGestureListener listener;


    public ItemTouchHelperCallback(int dragDirs, int swipeDirs, ItemGestureListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        return listener.onItemMoved(from, to);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwiped(viewHolder.getAdapterPosition(), direction);
    }

    public interface ItemGestureListener{
        boolean onItemMoved(int fromPosition, int toPosition);
        void onItemSwiped(int position, int direction);
    }

}
