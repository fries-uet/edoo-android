package com.uet.fries.tmq.edoo.app;

import android.support.multidex.MultiDexApplication;

import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.helper.dao.DaoMaster;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class AppController extends MultiDexApplication {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        mInstance = this;

        new PrefManager(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"edoo-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}