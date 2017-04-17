package com.uet.fries.tmq.edoo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.activity.LoginActivity;
import com.uet.fries.tmq.edoo.helper.PrefManager;
import com.uet.fries.tmq.edoo.rest.ErrorResponse;
import com.uet.fries.tmq.edoo.rest.RestClient;
import com.uet.fries.tmq.edoo.rest.model.ItemClass;
import com.uet.fries.tmq.edoo.rest.model.ItemLesson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableFragment extends Fragment {
    private static final String TAG = TimetableFragment.class.getSimpleName();
    private static final int TT_WIDTH = 6;
    private static final int TT_HEIGHT = 12;
    private Context mContext;
    private View rootView;
    private ArrayList<ItemLesson> listLessons;
    private LinearLayout[] columns;
    private int[][] listSubjectInTable;

    private boolean firstLoading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!firstLoading) {
            startAnim();
            return rootView;
        }

        firstLoading = false;
        rootView = inflater.inflate(R.layout.fragment_timetable, null);
        mContext = getActivity();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        rootView.setLayoutParams(params);

        listLessons = new ArrayList<>();
        listSubjectInTable = new int[TT_WIDTH][TT_HEIGHT];
        columns = new LinearLayout[6];

        initViews();

        getDataFromServer();

        return rootView;
    }

    private void initViews() {
        columns[0] = (LinearLayout) rootView.findViewById(R.id.tt_col_2);
        columns[1] = (LinearLayout) rootView.findViewById(R.id.tt_col_3);
        columns[2] = (LinearLayout) rootView.findViewById(R.id.tt_col_4);
        columns[3] = (LinearLayout) rootView.findViewById(R.id.tt_col_5);
        columns[4] = (LinearLayout) rootView.findViewById(R.id.tt_col_6);
        columns[5] = (LinearLayout) rootView.findViewById(R.id.tt_col_7);
    }

    private void showDialogInfo(final ItemLesson item, int bgItem) {
        final Dialog dialogInfo = new Dialog(mContext, R.style.DialogNoActionBar);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_item_subject_info, null);
        dialogInfo.setContentView(view);
        view.setBackgroundResource(bgItem);

        // InitViews
        TextView ten = (TextView) dialogInfo.findViewById(R.id.dialogTenMH);
        TextView maLMH = (TextView) dialogInfo.findViewById(R.id.dialogMaMH);
        TextView gv = (TextView) dialogInfo.findViewById(R.id.dialogGiangVien);
        TextView tgian = (TextView) dialogInfo.findViewById(R.id.dialogThoiGian);
        TextView diaDiem = (TextView) dialogInfo.findViewById(R.id.dialogDiaDiem);
        Button directToClass = (Button) dialogInfo.findViewById(R.id.btn_direct_to_class_dialog);

        // Set for Views
        ten.setText(item.getName());
        maLMH.setText(item.getCode());
        gv.setText(item.getTeacherName());
        diaDiem.setText(item.getAddress());
        tgian.setText("Thứ " + item.getDayOfWeek() + "\t\tTiết " + item.getPeriod());
        directToClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo
//                MainActivity mainActivity = (MainActivity) mContext;
//                ItemLop itemLop = new ItemLop(item.getName(), item.getClassId(), item.getCode(), item.getTeacherName(), item.getStudentCount());
//                mainActivity.goToTimeLine(itemLop, LopFragment.KEY_LOP_MON_HOC);
                Toast.makeText(mContext, "Go to LopFragment", Toast.LENGTH_SHORT).show();
                dialogInfo.dismiss();
            }
        });

        dialogInfo.show();
    }

    //----------------------------------------------------------------------------------------------

    private void getDataFromServer() {
        listLessons.clear();

        RestClient restClient = new RestClient(mContext, "classes");
        restClient.getApiService().listSubjects(PrefManager.getTokenLogin()).enqueue(new Callback<List<ItemClass>>() {
            @Override
            public void onResponse(Call<List<ItemClass>> call, Response<List<ItemClass>> response) {
                if (!response.isSuccessful()) {
                    ErrorResponse.handleError(response, mContext);
                    return;
                }
                List<ItemClass> list = response.body();
                Toast.makeText(mContext, "size = " + list.size(), Toast.LENGTH_SHORT).show();
                for (ItemClass itemClass : list) {
                    List<ItemLesson> lessons = itemClass.getLessons();
                    for (ItemLesson lesson : lessons) {
                        listLessons.add(lesson);
                    }
                }
                setDataForTimeTable();
            }

            @Override
            public void onFailure(Call<List<ItemClass>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void setDataForTimeTable() {
        convertSubjectToTable();
        TypedArray bgCell = getResources().obtainTypedArray(R.array.arr_bg_cell_background);
        TypedArray bgItem = getResources().obtainTypedArray(R.array.arr_background_item);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < listSubjectInTable[0].length; j++) {
                int posSubject = listSubjectInTable[i][j];
                int space = 1;
                while (j + space < listSubjectInTable[0].length && listSubjectInTable[i][j + space] == posSubject) {
                    space++;
                }
                if (posSubject >= 0) {
                    addView(columns[i], bgCell.getResourceId(idColorForItem(posSubject), 0),
                            bgItem.getResourceId(idColorForItem(posSubject), 0), space, listLessons.get(posSubject));
                } else {
                    addEmptyView(columns[i], space);
                }
                j += (space - 1);
            }
        }
        bgCell.recycle();
        bgItem.recycle();
    }

    ArrayList<View> listViewSubject = new ArrayList();

    private void startAnim() {
        Animation myAni = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_item_subject);
        for (View view : listViewSubject) {
            view.startAnimation(myAni);
        }
    }

    private void addView(LinearLayout col, final int bgCellId, final int bgItem, int weight, final ItemLesson lmh) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cell_timetable, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogInfo(lmh, bgItem);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(10);
        }
        view.setLayoutParams(params);
        view.findViewById(R.id.bg_cell).setBackgroundResource(bgCellId);

        TextView name = (TextView) view.findViewById(R.id.tv_name_subject);
        TextView timeStart = (TextView) view.findViewById(R.id.tv_time_start_subject);
        TextView timeEnd = (TextView) view.findViewById(R.id.tv_time_end_subject);

        name.setText(lmh.getAcronymOfName());
        int pos = lmh.getPosOfPeriod();
        pos = (pos < 6 ? pos : pos + 1) + 6;
        timeStart.setText(pos + ":00");
        timeEnd.setText((pos + lmh.getLengthOfPeriod()) + ":00");

        Animation myAni = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_item_subject);
        view.startAnimation(myAni);

        listViewSubject.add(view);
        col.addView(view);
    }

    private void addEmptyView(LinearLayout col, int weight) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cell_empty_timetable, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);
        view.setLayoutParams(params);
        col.addView(view);
    }

    private void convertSubjectToTable() {
        for (int i = 0; i < listSubjectInTable.length; i++) {
            for (int j = 0; j < listSubjectInTable[0].length; j++) {
                listSubjectInTable[i][j] = -1;
            }
        }
        for (int i = 0; i < listLessons.size(); i++) {
            ItemLesson item = listLessons.get(i);
            int day = item.getDayOfWeek();
            int posOfPeriod = item.getPosOfPeriod();
            int periodLength = item.getLengthOfPeriod();

            for (int j = 0; j < periodLength; j++) {
                int x = day - 2;
                int y = posOfPeriod + j - 1;
                if (y >= 5) y++;

                listSubjectInTable[x][y] = i;
            }
        }
    }

    private int idColorForItem(int indexLesson) {
        for (int i = 0; i < listLessons.size(); i++) {
            if (listLessons.get(indexLesson).getCode().equalsIgnoreCase(listLessons.get(i).getCode())) {
                return i;
            }
        }
        return indexLesson;
    }

}
