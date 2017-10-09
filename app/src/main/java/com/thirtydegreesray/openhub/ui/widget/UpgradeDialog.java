package com.thirtydegreesray.openhub.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.util.PrefHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * Created by ThirtyDegreesRay on 2017/8/30 14:27:27
 */

public enum UpgradeDialog implements UpgradeListener {
    @SuppressLint("StaticFieldLeak")INSTANCE;

    private Activity activity;

    public void setShowDialogActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onUpgrade(int i, UpgradeInfo upgradeInfo, boolean isManual, boolean isSilence) {
        if (upgradeInfo != null) {
            showUpgradeDialog(upgradeInfo, isManual);
        } else if (isManual) {
            Toasty.success(AppApplication.get().getApplicationContext(),
                    getTempActivity().getString(R.string.no_upgrade_tip)).show();
        }
    }

    private void showUpgradeDialog(UpgradeInfo upgradeInfo, boolean isManual) {
        if (BaseActivity.getCurActivity() == null) return;
        if (!checkPop(upgradeInfo, isManual)) return;

        Activity tempActivity = getTempActivity();
        String title = tempActivity.getString(R.string.upgrade)
                .concat("(").concat(upgradeInfo.versionName).concat(")");
        View content = tempActivity.getLayoutInflater().inflate(R.layout.layout_update_dialog, null);
        TextView versionText = findView(content, R.id.version_text);
        TextView sizeText = findView(content, R.id.size_text);
        TextView timeText = findView(content, R.id.time_text);
        TextView infoText = findView(content, R.id.upgrade_info);

        String publishTime = StringUtils.getDateStr(new Date(Beta.getUpgradeInfo().publishTime));
        String fileSize = StringUtils.getSizeString(Beta.getUpgradeInfo().fileSize);
        versionText.setText(versionText.getText().toString().concat(" ").concat(Beta.getUpgradeInfo().versionName));
        sizeText.setText(sizeText.getText().toString().concat(" ").concat(fileSize));
        timeText.setText(timeText.getText().toString().concat(" ").concat(publishTime));
        infoText.setText(Beta.getUpgradeInfo().newFeature);

        int confirmTextId = Beta.getStrategyTask().getStatus() == DownloadTask.COMPLETE ?
                R.string.install : R.string.upgrade;

        new AlertDialog.Builder(tempActivity)
                .setCancelable(true)
                .setTitle(title)
                .setView(content)
                .setNegativeButton(R.string.next_time, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Beta.cancelDownload();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(confirmTextId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadTask task = Beta.startDownload();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private Activity getTempActivity() {
        return this.activity == null ? BaseActivity.getCurActivity() : this.activity;
    }

    private <T extends View> T findView(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    private boolean checkPop(UpgradeInfo upgradeInfo, boolean isManual) {
        if(isManual) return true;

        int localPopTimes = PrefHelper.getPopTimes();
        long localPopVersionTime = PrefHelper.getPopVersionTime();
        long localLastPopTime = PrefHelper.getLastPopTime();

        int serverMaxPopTimes = upgradeInfo.popTimes;
        long serverPopVersionTime = upgradeInfo.publishTime;
        long serverPopInterval = upgradeInfo.popInterval;

        if(serverPopVersionTime != localPopVersionTime){
            localPopVersionTime = serverPopVersionTime;
            localPopTimes = 0;
            localLastPopTime = 0;
        }

        if(localPopTimes < serverMaxPopTimes &&
                System.currentTimeMillis() - localLastPopTime >= serverPopInterval){
            localPopTimes++;
            localLastPopTime = System.currentTimeMillis();
            PrefHelper.set(PrefHelper.POP_TIMES, localPopTimes);
            PrefHelper.set(PrefHelper.POP_VERSION_TIME, localPopVersionTime);
            PrefHelper.set(PrefHelper.LAST_POP_TIME, localLastPopTime);
            return true;
        }

        return false;
    }

}
