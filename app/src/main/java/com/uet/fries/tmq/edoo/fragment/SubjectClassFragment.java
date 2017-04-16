package com.uet.fries.tmq.edoo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.activity.MainActivity;
import com.uet.fries.tmq.edoo.adapter.ClassAdapter;
import com.uet.fries.tmq.edoo.rest.model.ItemClass;

import java.util.ArrayList;

public class SubjectClassFragment extends ClassFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{
    private ArrayList<ItemClass> ItemClassArr;

    private boolean firstLoading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!firstLoading){
            return root;
        }
        root = inflater.inflate(R.layout.fragment_subject_class, null);
        mContext = getActivity();

        //Swipe refresh
        swipeRefresh = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        swipeRefresh.setOnRefreshListener(this);

        initAdapter();
        initViews();

        firstLoading = false;
        return root;
    }

    private void initAdapter() {
        ItemClassArr = new ArrayList<>();
        mAdapter = new ClassAdapter(mContext);

        // Get du lieu lop mon hoc tu server
        requestLopHoc(ItemClassArr);
    }

    private void initViews() {
        lvMain = (ListView) root.findViewById(R.id.lv_lopmonhoc);
        lvMain.setOnItemClickListener(this);
        lvMain.setAdapter(mAdapter);
    }

    @Override
    protected void onFail() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void onPostComplete() {
        mAdapter.setItemArr(ItemClassArr);

        if (swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        requestLopHoc(ItemClassArr);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!swipeRefresh.isRefreshing()){
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.goToTimeLine(ItemClassArr.get(position), ClassFragment.KEY_LOP_MON_HOC);
        }
    }
}
