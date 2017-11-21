package com.thirtydegreesray.openhub.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

/**
 * Created by ThirtyDegreesRay on 2017/11/20 16:36:19
 */

public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerViewContextMenuInfo contextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return contextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int position = getChildLayoutPosition(originalView);
        if (position == NO_POSITION) {
            return false;
        }
        final long id = getAdapter().getItemId(position);
        contextMenuInfo = new RecyclerViewContextMenuInfo(id, position);
        return super.showContextMenuForChild(originalView);
    }

    public class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {
        private long id;
        private int position;

        RecyclerViewContextMenuInfo(long id, int position) {
            this.id = id;
            this.position = position;
        }

        public long getId() {
            return id;
        }

        public int getPosition() {
            return position;
        }
    }

}
