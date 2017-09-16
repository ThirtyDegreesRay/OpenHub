package com.thirtydegreesray.openhub.http;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.util.StringUtils;

import es.dmoral.toasty.Toasty;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 18:09:03
 */

public class Downloader {

    private DownloadManager downloadManager;
    private Context mContext;
    private long downloadId;

    private String fileName;

    public static Downloader create(Context context){
        return new Downloader(context);
    }

    private Downloader(Context context){
        this.mContext = context;
    }

    public void start(String url, String fileName) {
        if(StringUtils.isBlank(url) || StringUtils.isBlank(fileName)){
            Toasty.error(mContext, mContext.getString(R.string.download_empty_tip)).show();
            return;
        }
        this.fileName = fileName;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        String title = mContext.getString(R.string.downloading);
        if(fileName.contains("/")){
            title = title.concat(" ").concat(fileName.substring(fileName.lastIndexOf("/") + 1));
        }else{
            title = title.concat(" ").concat(fileName);
        }
        request.setTitle(title);
//        request.setDescription("Apk Downloading");
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalPublicDir("OpenHub/Download", fileName);

        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        Toasty.success(mContext, mContext.getString(R.string.download_start)).show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    String tip = mContext.getString(R.string.download_complete).concat("\n").concat(getFilePath());
                    Toasty.success(mContext, tip, Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toasty.error(mContext, mContext.getString(R.string.download_failed)).show();
                    break;
            }
        }
        c.close();
    }

    private String getFilePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                .concat("/OpenHub/Download").concat("/").concat(fileName);
    }


}
