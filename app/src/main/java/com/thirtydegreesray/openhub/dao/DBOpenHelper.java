package com.thirtydegreesray.openhub.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ThirtyDegreesRay on 2017/11/13 10:40:22
 */

public class DBOpenHelper extends DaoMaster.DevOpenHelper {

    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion == 2 && newVersion == 3){
            TraceUserDao.dropTable(db, true);
            TraceRepoDao.dropTable(db, true);
            AuthUserDao.createTable(db, true);
            TraceUserDao.createTable(db, false);
            TraceRepoDao.createTable(db, false);
        } else {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }

}
