package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class SearchAutoCompleteAdapter extends ArrayAdapter<String> {

    private OnSearchItemClick mClickListener;
    private OnSearchItemLongClick mLongClickListener;

    public SearchAutoCompleteAdapter(@NonNull Context context, int resource,
                                     @NonNull List<String> objects, OnSearchItemLongClick longClickListener,
                                     OnSearchItemClick clickListener) {
        super(context, resource, objects);
        this.mClickListener = clickListener;
        this.mLongClickListener = longClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setOnClickListener((v) -> mClickListener.onClick(getItem(position)));
        view.setOnLongClickListener((v) -> {
            mLongClickListener.onLongClick(this, getItem(position));
            return true;
        });
        return view;
    }

    public interface OnSearchItemClick {
        void onClick(@Nullable String item);
    }

    public interface OnSearchItemLongClick {
        void onLongClick(SearchAutoCompleteAdapter adapter, @Nullable String item);
    }
}
