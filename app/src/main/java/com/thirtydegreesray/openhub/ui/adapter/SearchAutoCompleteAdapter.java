package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class SearchAutoCompleteAdapter extends ArrayAdapter<String> {

    private OnSearchItemLongClick mLongClickListener;

    public SearchAutoCompleteAdapter(@NonNull Context context, int resource,
                                     @NonNull List<String> objects, OnSearchItemLongClick listener) {
        super(context, resource, objects);
        this.mLongClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setOnLongClickListener((v) -> {
            mLongClickListener.onLongClick(this, getItem(position));
            return true;
        });
        return view;
    }

    public interface OnSearchItemLongClick {
        void onLongClick(SearchAutoCompleteAdapter adapter, @Nullable String item);
    }
}
