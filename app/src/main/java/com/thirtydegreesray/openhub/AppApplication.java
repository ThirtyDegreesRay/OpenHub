

package com.thirtydegreesray.openhub;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerAppComponent;
import com.thirtydegreesray.openhub.inject.module.AppModule;
import com.thirtydegreesray.openhub.service.NetBroadcastReceiver;
import com.thirtydegreesray.openhub.ui.activity.AboutActivity;
import com.thirtydegreesray.openhub.ui.activity.LoginActivity;
import com.thirtydegreesray.openhub.ui.activity.MainActivity;
import com.thirtydegreesray.openhub.ui.widget.UpgradeDialog;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.NetHelper;

/**
 * AppApplication
 * Created by ThirtyDegreesRay on 2016/7/13 14:01
 */
public class AppApplication extends Application {

    private final String TAG = AppApplication.class.getSimpleName();

    private static AppApplication application;
    private AppComponent mAppComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //init application
        long startTime = System.currentTimeMillis();
        AppData.INSTANCE.getSystemDefaultLocal();
        //apply language for application context, bugly used it
        AppUtils.updateAppLanguage(getApplicationContext());
        initLogger();
        Logger.t(TAG).i("startTime:" + startTime);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        initNetwork();
        initBugly();
        startTime = System.currentTimeMillis();
        Logger.t(TAG).i("application ok:" + (System.currentTimeMillis() - startTime));


    }

    private void initLogger(){
        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                        .showThreadInfo(false)
                        .methodCount(0)
                        .methodOffset(0)
                        .tag("OpenHub_Logger")
                        .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }

            @Override
            public void log(int priority, String tag, String message) {
                super.log(priority, tag, message);
            }
        });
    }

    private void initBugly(){

        Beta.initDelay = 6 * 1000;
        Beta.enableHotfix = false;
        Beta.canShowUpgradeActs.add(LoginActivity.class);
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(AboutActivity.class);
        Beta.upgradeListener = UpgradeDialog.INSTANCE;

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(BuildConfig.VERSION_NAME);
        strategy.setAppChannel(getAppChannel());
        strategy.setAppReportDelay(10 * 1000);
        Bugly.init(getApplicationContext(), AppConfig.BUGLY_APPID, BuildConfig.DEBUG, strategy);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);

    }

    private void initNetwork(){
        NetBroadcastReceiver receiver = new NetBroadcastReceiver();
        IntentFilter filter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        } else {
            filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        registerReceiver(receiver, filter);

        NetHelper.INSTANCE.init(this);
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    private String getAppChannel(){
        String channel = "normal";
        try {
            ApplicationInfo applicationInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            channel = applicationInfo.metaData.getString("BUGLY_APP_CHANNEL", "normal");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
