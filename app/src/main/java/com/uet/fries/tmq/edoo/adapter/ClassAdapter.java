package com.uet.fries.tmq.edoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.rest.model.ItemClass;

import java.util.ArrayList;
import java.util.Random;

public class ClassAdapter extends BaseAdapter {

    private ArrayList<ItemClass> itemArr = new ArrayList<>();
    private Context mContext;
    private LayoutInflater lf;

    public ClassAdapter(Context mContext) {
        this.mContext = mContext;
        lf = LayoutInflater.from(mContext);
    }

    public void setItemArr(ArrayList<ItemClass> itemArr) {
        this.itemArr = itemArr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemArr.size();
    }

    @Override
    public ItemClass getItem(int position) {
        return itemArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Random rand = new Random();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lf.inflate(R.layout.item_subject_class, null);
        }

        Animation myAni = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_item_listview);
        convertView.startAnimation(myAni);


        TextView tvTen = (TextView) convertView.findViewById(R.id.tv_tenlopmonhoc);
        TextView tvId = (TextView) convertView.findViewById(R.id.tv_idlopmonhoc);
        TextView tvGiangVien = (TextView) convertView.findViewById(R.id.tv_giangvienlopmonhoc);
        TextView tvSoNguoi = (TextView) convertView.findViewById(R.id.tv_songuoilopmonhoc);

        tvTen.setText(itemArr.get(position).getName());
        tvId.setText(itemArr.get(position).getId());
        tvGiangVien.setText(itemArr.get(position).getTeacher_name());

        tvSoNguoi.setText("Số người: " + itemArr.get(position).getStudent_count());

        return convertView;
    }
}
