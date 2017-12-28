package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/12/28 14:06:39
 */

public enum  CustomTabsHelper {
    INSTANCE;

    private final String ACTION_CUSTOM_TABS_CONNECTION =
            "android.support.customtabs.action.CustomTabsService";

    private final String CHROME_STABLE_PACKAGE = "com.android.chrome";
    private final String CHROME_BETA_PACKAGE = "com.chrome.beta";
    private final String CHROME_DEV_PACKAGE = "com.chrome.dev";
    private final String CHROME_LOCAL_PACKAGE = "com.google.android.apps.chrome";

    private final String FIREFOX_STABLE_PACKAGE = "org.mozilla.firefox";
    private final String FIREFOX_CN_STABLE_PACKAGE = "cn.mozilla.firefox";
    private final String FIREFOX_FOCUS_PACKAGE = "org.mozilla.focus";
    private final String FIREFOX_BETA_PACKAGE = "org.mozilla.firefox_beta";
    private final String FIREFOX_DEV_PACKAGE = "org.mozilla.fennec_aurora";

    //order for choose best package
    private final List<String> ORDERED_PACKAGES = Arrays.asList(
            CHROME_STABLE_PACKAGE, CHROME_BETA_PACKAGE, CHROME_DEV_PACKAGE, CHROME_LOCAL_PACKAGE,
            FIREFOX_STABLE_PACKAGE, FIREFOX_CN_STABLE_PACKAGE, FIREFOX_FOCUS_PACKAGE,
            FIREFOX_BETA_PACKAGE, FIREFOX_DEV_PACKAGE
    );

    private String bestPackageName ;

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is <strong>not</strong> threadsafe.
     *
     * @param context {@link Context} to use for accessing {@link PackageManager}.
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    public  String getBestPackageName(Context context) {
        if (bestPackageName != null) return bestPackageName;

        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));
        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
        String defaultViewHandlerPackageName = null;
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        // Get all apps that can handle VIEW intents.
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            bestPackageName = null;
        } else if (packagesSupportingCustomTabs.size() == 1) {
            bestPackageName = packagesSupportingCustomTabs.get(0);
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            bestPackageName = defaultViewHandlerPackageName;
        } else if ((bestPackageName = getFirstMatchedPackage(packagesSupportingCustomTabs)) != null) {
            //do nothing
        } else {
            packagesSupportingCustomTabs.get(0);
        }
        return bestPackageName;
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        try {
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> handlers = pm.queryIntentActivities(intent,
                    PackageManager.GET_RESOLVED_FILTER);
            if (handlers == null || handlers.size() == 0) {
                return false;
            }
            for (ResolveInfo resolveInfo : handlers) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) continue;
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue;
                if (resolveInfo.activityInfo == null) continue;
                return true;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compare local custom tabs packages with ORDERED_PACKAGES, return the first matched
     */
    private String getFirstMatchedPackage(List<String> packagesSupportingCustomTabs){
        for(String packageName : ORDERED_PACKAGES){
            if(packagesSupportingCustomTabs.contains(packageName)){
                return packageName;
            }
        }
        return null;
    }

}
