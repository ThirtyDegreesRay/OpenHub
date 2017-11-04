package com.thirtydegreesray.openhub.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.http.Downloader;
import com.thirtydegreesray.openhub.mvp.model.DownloadSource;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.BaseViewHolder;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 15:53:46
 */

public class DownloadSourcesAdapter extends BaseAdapter<DownloadSourcesAdapter.ViewHolder, DownloadSource> {

    private String repoName;
    private String tagName;

    public DownloadSourcesAdapter(Context context, String repoName, String tagName) {
        super(context);
        this.repoName = repoName;
        this.tagName = tagName;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_download_source;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DownloadSource model = data.get(position);
        holder.icon.setImageResource(model.isSourceCode() ? R.drawable.ic_source : R.drawable.ic_collection);
        holder.name.setText(model.getName());
        if(model.getSize() != 0){
            holder.size.setVisibility(View.VISIBLE);
            holder.size.setText(StringUtils.getSizeString(model.getSize()));
        }else{
            holder.size.setVisibility(View.INVISIBLE);
        }
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.icon) AppCompatImageView icon;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.size) TextView size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.download_bn)
        public void onDownloadClick(){
            DownloadSource source = data.get(getAdapterPosition());
            String fileName = repoName.concat("-").concat(tagName)
                    .concat("-").concat(source.getName());
            AppOpener.startDownload(context, source.getUrl(), fileName);
        }

    }

}
