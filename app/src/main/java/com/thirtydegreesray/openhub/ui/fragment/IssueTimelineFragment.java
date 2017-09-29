package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.IIssueTimelineContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;
import com.thirtydegreesray.openhub.mvp.presenter.IssueTimelinePresenter;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;
import com.thirtydegreesray.openhub.ui.adapter.IssueTimelineAdapter;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/27 16:32:49
 */

public class IssueTimelineFragment extends ListFragment<IssueTimelinePresenter, IssueTimelineAdapter>
        implements IIssueTimelineContract.View{

    public static IssueTimelineFragment create(@NonNull Issue issue){
        IssueTimelineFragment fragment = new IssueTimelineFragment();
        fragment.setArguments(BundleBuilder.builder().put("issue", issue).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
    }

    @Override
    public void showTimeline(ArrayList<IssueEvent> events) {
        adapter.setData(events);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
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
        mPresenter.loadTimeline(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_comments);
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadTimeline(page, false);
    }

    @Override
    protected int getHeaderSize() {
        return 1;
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        ViewerActivity.showMdSource(getActivity(), getString(R.string.comment),
                adapter.getData().get(position).getBodyHtml());
    }

    public void addComment(IssueEvent event){
        mPresenter.getTimeline().add(event);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

//    @Override
//    public boolean onItemLongClick(int position, @NonNull View view) {
//        final IssueEvent issueEvent = adapter.getData().get(position);
//        String[] actions = new String[]{
//                getString(R.string.share),
//                getString(R.string.edit),
//                getString(R.string.delete)
//        };
//        new AlertDialog.Builder(getActivity())
//                .setItems(actions, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        if(which == 1){
//                            MarkdownEditorActivity.show(getActivity(), R.string.comment, 101, issueEvent.getBodyHtml());
//                        }
//                    }
//                })
//                .show();
//        return true;
//    }
}
