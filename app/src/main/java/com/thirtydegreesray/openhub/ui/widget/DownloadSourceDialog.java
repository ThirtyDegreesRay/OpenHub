package com.thirtydegreesray.openhub.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.DownloadSource;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.model.ReleaseAsset;
import com.thirtydegreesray.openhub.ui.adapter.DownloadSourcesAdapter;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 23:21:22
 */

public class DownloadSourceDialog {

    public static void show(Context context, String repoName, String tagName, Release release){
        DownloadSourcesAdapter adapter = new DownloadSourcesAdapter(context, repoName, tagName);
        adapter.setData(getDownloadSources(context, release));

        final RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(R.string.download)
                .setView(recyclerView)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private static ArrayList<DownloadSource> getDownloadSources(Context context, Release release){
        ArrayList<DownloadSource> sources = new ArrayList<>();
        for(ReleaseAsset asset : release.getAssets()){
            sources.add(new DownloadSource(asset.getDownloadUrl(), false, asset.getName(), asset.getSize()));
        }
        sources.add(new DownloadSource(release.getZipballUrl(), true,
                context.getString(R.string.source_code_zip)));
        sources.add(new DownloadSource(release.getTarballUrl(), true,
                context.getString(R.string.source_code_tar)));
        return sources;
    }

}
