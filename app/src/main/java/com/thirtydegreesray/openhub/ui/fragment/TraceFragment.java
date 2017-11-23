package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.ITraceContract;
import com.thirtydegreesray.openhub.mvp.model.TraceExt;
import com.thirtydegreesray.openhub.mvp.presenter.TracePresenter;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.adapter.TraceAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.ItemTouchHelperCallback;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/23 10:56:40
 */

public class TraceFragment extends ListFragment<TracePresenter, TraceAdapter>
        implements ITraceContract.View, ItemTouchHelperCallback.ItemGestureListener {

    public static TraceFragment create(){
        return new TraceFragment();
    }

    private ItemTouchHelper itemTouchHelper;

    @Override
    public void showTraceList(ArrayList<TraceExt> traceList) {
        adapter.setData(traceList);
        postNotifyDataSetChanged();
    }

    @Override
    public void notifyItemAdded(int position) {
        if(adapter.getData().size() == 1){
            postNotifyDataSetChanged();
        } else {
            adapter.notifyItemInserted(position);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);

        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener((header, position, headerId) -> {
            //wrong position returned
//            recyclerView.smoothScrollToPosition(mPresenter.getFirstItemByDate((Long) header.getTag()));
        });
        recyclerView.addOnItemTouchListener(touchListener);

    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadTraceList(1);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadTraceList(page);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_trace);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        TraceExt trace = adapter.getData().get(position);
        if("user".equals(trace.getType())){
            View userAvatar = view.findViewById(R.id.avatar);
            ProfileActivity.show(getActivity(), userAvatar, trace.getUser().getLogin(),
                    trace.getUser().getAvatarUrl());
        } else {
            RepositoryActivity.show(getActivity(), trace.getRepository().getOwner().getLogin(),
                    trace.getRepository().getName());
        }
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        mPresenter.removeTrace(position);
        if(adapter.getData().size() == 0){
            postNotifyDataSetChanged();
        } else {
            adapter.notifyItemRemoved(position);
        }
        Snackbar.make(recyclerView, R.string.trace_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, v -> mPresenter.undoRemoveTrace() )
                .show();
    }
}
