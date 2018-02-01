package com.thirtydegreesray.openhub.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.widget.Toast;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.http.Downloader;
import com.thirtydegreesray.openhub.mvp.model.GitHubName;
import com.thirtydegreesray.openhub.service.CopyBroadcastReceiver;
import com.thirtydegreesray.openhub.service.ShareBroadcastReceiver;
import com.thirtydegreesray.openhub.ui.activity.CommitDetailActivity;
import com.thirtydegreesray.openhub.ui.activity.IssueDetailActivity;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.ReleaseInfoActivity;
import com.thirtydegreesray.openhub.ui.activity.ReleasesActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.activity.ViewerActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by ThirtyDegreesRay on 2017/10/30 11:18:57
 */

public class AppOpener {

    public static void openInCustomTabsOrBrowser(@NonNull Context context, @NonNull String url){
        if(StringUtils.isBlank(url)){
            Toasty.warning(context, context.getString(R.string.invalid_url), Toast.LENGTH_LONG).show();
            return;
        }
        //check http prefix
        if(!url.contains("//")){
            url = "http://".concat(url);
        }

        String customTabsPackageName ;
        if (PrefUtils.isCustomTabsEnable() &&
                (customTabsPackageName = CustomTabsHelper.INSTANCE.getBestPackageName(context) ) != null) {
            Bitmap backIconBitmap = ViewUtils.getBitmapFromResource(context, R.drawable.ic_arrow_back_title);
            Intent shareIntent = new Intent(context.getApplicationContext(), ShareBroadcastReceiver.class);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent sharePendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(), 0, shareIntent, 0);
            Intent copyIntent = new Intent(context.getApplicationContext(), CopyBroadcastReceiver.class);
            copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent copyPendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(), 0, copyIntent, 0);

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(ViewUtils.getPrimaryColor(context))
                    .setCloseButtonIcon(backIconBitmap)
                    .setShowTitle(true)
                    .addMenuItem(context.getString(R.string.share), sharePendingIntent)
                    .addMenuItem(context.getString(R.string.copy_url), copyPendingIntent)
//                    .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
//                    .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .build();
            customTabsIntent.intent.setPackage(customTabsPackageName);
            customTabsIntent.launchUrl(context, Uri.parse(url));

            if(PrefUtils.isCustomTabsTipsEnable()){
                Toasty.info(context, context.getString(R.string.use_custom_tabs_tips), Toast.LENGTH_LONG).show();
                PrefUtils.set(PrefUtils.CUSTOM_TABS_TIPS_ENABLE, false);
            }

        } else {
            openInBrowser(context,url);
        }

    }

    public static void openInBrowser(@NonNull Context context, @NonNull String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE);
        if(intent != null){
            context.startActivity(intent);
        } else {
            Toasty.warning(context, context.getString(R.string.no_browser_clients), Toast.LENGTH_LONG).show();
        }
    }

    public static void startDownload(@NonNull Context context, @NonNull String url, String fileName) {
        if(PrefUtils.isSystemDownloader()){
            Downloader.create(context.getApplicationContext()).start(url, fileName);
        }else{
            openDownloader(context, url);
        }
    }

    public static void openDownloader(@NonNull Context context, @NonNull String url) {
        if(StringUtils.isBlank(url)){
            Toasty.warning(context, context.getString(R.string.invalid_url), Toast.LENGTH_LONG).show();
            return;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE);
        if(intent != null){
            context.startActivity(intent);
        } else {
            Toasty.warning(context, context.getString(R.string.no_download_clients), Toast.LENGTH_LONG).show();
        }
    }

    public static void shareText(@NonNull Context context, @NonNull String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }catch (ActivityNotFoundException e){
            Toasty.warning(context, context.getString(R.string.no_share_clients)).show();
        }
    }

    public static void launchEmail(@NonNull Context context, @NonNull String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }catch (ActivityNotFoundException e){
            Toasty.warning(context, context.getString(R.string.no_email_clients)).show();
        }
    }

    public static void openInMarket(@NonNull Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_in_market))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }catch (ActivityNotFoundException e){
            Toasty.warning(context, context.getString(R.string.no_market_clients)).show();
        }
    }

    public static void launchUrl(@NonNull Context context, @NonNull Uri uri){
        if(StringUtils.isBlank(uri.toString())) return;
        String url = uri.toString();
        if(GitHubHelper.isImage(url)){
            ViewerActivity.showImage(context, url);
            return;
        }
        GitHubName gitHubName = GitHubName.fromUrl(url);
        String userName ;
        String repoName ;
        if(gitHubName == null){
            openInCustomTabsOrBrowser(context, uri.toString());
            return;
        } else {
            userName = gitHubName.getUserName();
            repoName = gitHubName.getRepoName();
        }

        if(GitHubHelper.isUserUrl(url)){
            ProfileActivity.show((Activity) context, userName);
        } else if(GitHubHelper.isRepoUrl(url)){
            RepositoryActivity.show(context, userName, repoName);
        } else if (GitHubHelper.isIssueUrl(url)) {
            IssueDetailActivity.show((Activity) context, url);
        } else if (GitHubHelper.isReleasesUrl(url)) {
            ReleasesActivity.show((Activity) context, userName, repoName);
        } else if (GitHubHelper.isReleaseTagUrl(url)) {
            ReleaseInfoActivity.show((Activity) context, userName, repoName, gitHubName.getReleaseTagName());
        } else if (GitHubHelper.isCommitUrl(url)) {
            CommitDetailActivity.show((Activity) context, url);
        } else {
            openInCustomTabsOrBrowser(context, url);
        }
    }

    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                return true;
            }
        }
        return false;
    }

    private static Intent createActivityChooserIntent(Context context, Intent intent,
                                                      Uri uri, List<String> ignorPackageList) {
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        final ArrayList<Intent> chooserIntents = new ArrayList<>();
        final String ourPackageName = context.getPackageName();

        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo resInfo : activities) {
            ActivityInfo info = resInfo.activityInfo;
            if (!info.enabled || !info.exported) {
                continue;
            }
            if (info.packageName.equals(ourPackageName)) {
                continue;
            }
            if (ignorPackageList != null && ignorPackageList.contains(info.packageName)) {
                continue;
            }

            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(info.packageName);
            targetIntent.setDataAndType(uri, intent.getType());
            chooserIntents.add(targetIntent);
        }

        if (chooserIntents.isEmpty()) {
            return null;
        }

        final Intent lastIntent = chooserIntents.remove(chooserIntents.size() - 1);
        if (chooserIntents.isEmpty()) {
            // there was only one, no need to showImage the chooser
            return lastIntent;
        }

        Intent chooserIntent = Intent.createChooser(lastIntent, null);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                chooserIntents.toArray(new Intent[chooserIntents.size()]));
        return chooserIntent;
    }

    private static final List<String> VIEW_IGNORE_PACKAGE = Arrays.asList(
            "com.gh4a", "com.fastaccess", "com.taobao.taobao"
    );

}
