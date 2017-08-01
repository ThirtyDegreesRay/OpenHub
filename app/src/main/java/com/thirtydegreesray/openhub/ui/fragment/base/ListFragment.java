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

package com.thirtydegreesray.openhub.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.IBaseView;
import com.thirtydegreesray.openhub.mvp.presenter.BasePresenter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created on 2017/7/20.
 *
 * @author ThirtyDegreesRay
 */

public abstract class ListFragment <P extends BasePresenter, A extends BaseAdapter>
        extends BaseFragment<P> implements IBaseView,
        BaseAdapter.OnItemClickListener,
        BaseAdapter.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener{

    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @Inject protected A adapter;
    private RecyclerView.AdapterDataObserver observer;

    @BindView(R.id.lay_tip) LinearLayout layTip;
    @BindView(R.id.tv_tip) TextView tvTip;

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        layTip.setVisibility(View.GONE);
        layTip.setOnClickListener(this);

        //adapter 数据观察者，当数据为空时，显示空提示
        observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int itemCount = adapter.getItemCount();
                if (itemCount == 0) {
                    refreshLayout.setVisibility(View.GONE);
                    layTip.setVisibility(View.VISIBLE);
                    tvTip.setText(getEmptyTip());
                } else {
                    refreshLayout.setVisibility(View.VISIBLE);
                    layTip.setVisibility(View.GONE);
                }
            }
        };
        adapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        reLoadData();
    }

    @Override
    public void onClick(@NonNull View view) {
        if(view.getId() == R.id.lay_tip){
            refreshLayout.setVisibility(View.VISIBLE);
            layTip.setVisibility(View.GONE);
            refreshLayout.setRefreshing(true);
            reLoadData();
        }
    }

    protected void setErrorTip(String errorTip){
        refreshLayout.setVisibility(View.GONE);
        layTip.setVisibility(View.VISIBLE);
        tvTip.setText(errorTip);
    }

    protected abstract void reLoadData();

    protected abstract String getEmptyTip();

}
