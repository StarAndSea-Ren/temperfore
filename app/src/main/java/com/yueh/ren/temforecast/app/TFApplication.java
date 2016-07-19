package com.yueh.ren.temforecast.app;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.yueh.ren.temforecast.model.database.DaoMaster;
import com.yueh.ren.temforecast.model.database.DaoSession;
import com.yueh.ren.temforecast.util.Constants;

/**
 * Created by 任跃华 on 2016/4/17 0017.
 */
public class TFApplication extends Application{

    private DaoMaster.OpenHelper helper;
    private DaoSession daoSession;
    private static TFApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Intent startIntent = new Intent(this, SaveDataService.class);
        startService(startIntent);
        initDB();
        initStetho();
    }

    /**
     * Stetho可使得你的android设备在chrome上调试
     */
    private void initStetho(){
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private void initDB() {
        helper = new DaoMaster.DevOpenHelper(this.getApplicationContext(), Constants.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static TFApplication getInstance(){
        return instance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
