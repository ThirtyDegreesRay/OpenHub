

package com.thirtydegreesray.openhub.inject.module;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.dao.DBOpenHelper;
import com.thirtydegreesray.openhub.dao.DaoMaster;
import com.thirtydegreesray.openhub.dao.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * AppModule
 * Created by ThirtyDegreesRay on 2016/8/30 13:52
 */
@Module
public class AppModule {

    private AppApplication application;

    public AppModule(AppApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public AppApplication provideApplication() {
        return application;
    }

    @NonNull
    @Provides
    @Singleton
    public DaoSession provideDaoSession() {
        DBOpenHelper helper = new DBOpenHelper(application, AppConfig.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }


}
