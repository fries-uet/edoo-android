package com.uet.fries.tmq.edoo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.uet.fries.tmq.edoo.adapter.ClassAdapter;
import com.uet.fries.tmq.edoo.app.AppController;
import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;
import com.uet.fries.tmq.edoo.helper.dao.UserDao;
import com.uet.fries.tmq.edoo.rest.RestClient;
import com.uet.fries.tmq.edoo.rest.model.ItemClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class ClassFragment extends Fragment {
    private static final String TAG = ClassFragment.class.getSimpleName();
    protected View root;
    protected ListView lvMain;
    protected ClassAdapter mAdapter;
    protected Context mContext;

    protected SwipeRefreshLayout swipeRefresh;

    public static final String KEY_LOP_MON_HOC = "classSubject";
    public static final String KEY_LOP_KHOA_HOC = "class_xes";
    public static final String KEY_NHOM = "nhom";

    protected long uid;

    private boolean isRefreshing;

    protected void requestLopHoc(final ArrayList<ItemClass> itemArr) {
        isRefreshing = true;

        // get user information
        DaoSession daoSession = AppController.getInstance().getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        uid = userDao.loadAll().get(0).getId();
        daoSession.clear();

        RestClient requestServices = new RestClient(getActivity(), "classes");
        requestServices.getApiService().listClasses(PrefManager.getTokenLogin()).enqueue(new Callback<List<ItemClass>>() {
            @Override
            public void onResponse(Call<List<ItemClass>> call, Response<List<ItemClass>> response) {
                if (response.isSuccessful()) {
                    itemArr.clear();
                    itemArr.addAll(response.body());

                    Message msg = new Message();
                    msg.setTarget(mHandler);
                    msg.sendToTarget();
                }
            }

            @Override
            public void onFailure(Call<List<ItemClass>> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                isRefreshing = false;
                onFail();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isRefreshing = false;
            onPostComplete();
        }
    };

    public boolean isRefreshing() {
        return isRefreshing;
    }

    protected abstract void onFail();

    protected abstract void onPostComplete();
}
