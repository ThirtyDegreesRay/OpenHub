package com.thirtydegreesray.openhub.ui.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.ui.adapter.LabelManageAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.ui.adapter.base.CatchableLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2018/1/23 14:28:05
 */

public class ChooseLabelsDialog implements BaseViewHolder.OnItemClickListener {

    private Activity mActivity;
    private ArrayList<Label> mLabels;
    private ChooseLabelsListener mListener;

    private RecyclerView recyclerView;
    private LabelManageAdapter adapter;
    private AlertDialog dialog;

    public ChooseLabelsDialog(@NonNull Activity activity, @NonNull ArrayList<Label> labels,
                              @NonNull ChooseLabelsListener listener){
        mActivity = activity;
        mLabels = labels;
        mListener = listener;

        recyclerView = new RecyclerView(activity);
        adapter = new LabelManageAdapter(activity, null);
        recyclerView.setLayoutManager(new CatchableLinearLayoutManager(activity));
        adapter.setOnItemClickListener(this);
        adapter.setData(labels);
        recyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.choose_labels)
                .setView(recyclerView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (dialog, which) ->
                    mListener.onChooseLabelsComplete(getChosenLabels())
                )
                .setNeutralButton(R.string.manage_labels, (dialog, which) ->
                    mListener.onShowManageLabels()
                );
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }


    @Override
    public void onItemClick(int position, @NonNull View view) {
        adapter.getData().get(position).setSelect(!adapter.getData().get(position).isSelect());
        adapter.notifyItemChanged(position);
    }
    
    public interface ChooseLabelsListener{
        void onChooseLabelsComplete(@NonNull ArrayList<Label> labels);
        void onShowManageLabels();
    }

    private ArrayList<Label> getChosenLabels(){
        ArrayList<Label> chosenLabels = new ArrayList<>();
        for(Label label : mLabels){
            if(label.isSelect()){
                chosenLabels.add(label);
            }
        }
        return chosenLabels;
    }
    
}
