package com.uet.fries.tmq.edoo.app;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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


    // Todo: remove Queue
    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}