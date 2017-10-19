package com.thirtydegreesray.openhub.http;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.util.AppHelper;
import com.thirtydegreesray.openhub.util.Logger;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 18:09:03
 */

public class Downloader {

    private DownloadManager downloadManager;
    private Context mContext;
    private long downloadId;

    private String url;
    private String fileName;

    public static Downloader create(Context context){
        return new Downloader(context);
    }

    private Downloader(Context context){
        this.mContext = context;
        AppHelper.updateAppLanguage(context);
    }

    public void start(String url, String fileName) {
        try{
            if(StringUtils.isBlank(url) || StringUtils.isBlank(fileName)){
                Toasty.error(mContext, mContext.getString(R.string.download_empty_tip)).show();
                return;
            }
            this.url = url;
            this.fileName = fileName;

            if(!AppHelper.checkDownloadServiceEnabled(mContext)){
                Toasty.warning(mContext, mContext.getString(R.string.enable_download_service_tip),
                        Toast.LENGTH_LONG).show();
                AppHelper.showDownloadServiceSetting(mContext);
                return ;
            }

            AndPermission.with(mContext)
                    .permission(Permission.STORAGE)
                    .callback(new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                            start();
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            Toasty.error(mContext, mContext.getString(R.string.permission_storage_denied),
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .start();
        }catch (Exception e){
            Toasty.error(mContext, e.getMessage()).show();
        }
    }

    private void start() {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String title = mContext.getString(R.string.downloading);
        if(fileName.contains("/")){
            title = title.concat(" ").concat(fileName.substring(fileName.lastIndexOf("/") + 1));
        }else{
            title = title.concat(" ").concat(fileName);
        }
        request.setTitle(title);
//        request.setDescription("Apk Downloading");
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalPublicDir("Download", fileName);

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
        //cause SQLiteException at 乐视 LE X820 Android 6.0.1,level 23
        try{
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
                        String tip = mContext.getString(R.string.download_complete)
                                .concat("\n").concat(getFilePath());
                        Toasty.success(mContext, tip).show();
                        unregister();
                        break;
                    case DownloadManager.STATUS_FAILED:
                        Toasty.error(mContext, mContext.getString(R.string.download_failed)).show();
                        unregister();
                        break;
                }
            }
            c.close();
        }catch (SQLiteException e){
            Logger.e(e);
            unregister();
        }

    }

    private String getFilePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                .concat("/Download").concat("/").concat(fileName);
    }

    private void unregister(){
        mContext.unregisterReceiver(receiver);
        mContext = null;
    }

}
