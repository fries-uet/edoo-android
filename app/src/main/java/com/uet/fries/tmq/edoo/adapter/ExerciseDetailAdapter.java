package com.uet.fries.tmq.edoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uet.fries.tmq.edoo.R;
import com.uet.fries.tmq.edoo.app.AppController;
import com.uet.fries.tmq.edoo.helper.dao.DaoSession;
import com.uet.fries.tmq.edoo.helper.dao.User;
import com.uet.fries.tmq.edoo.helper.dao.UserDao;
import com.uet.fries.tmq.edoo.holder.AbstractHolder;
import com.uet.fries.tmq.edoo.holder.ItemCommentDetailHolder;
import com.uet.fries.tmq.edoo.holder.ItemEventDetailHolder;
import com.uet.fries.tmq.edoo.model.ItemComment;
import com.uet.fries.tmq.edoo.model.ItemTimeLineExercise;

import java.util.ArrayList;
import java.util.HashMap;

public class ExerciseDetailAdapter extends RecyclerView.Adapter<AbstractHolder> {

    private Context mContext;
    private ItemTimeLineExercise itemTimeline;
    private User user;
    private ItemEventDetailHolder eventDetail;

    public ExerciseDetailAdapter(Context mContext, ItemTimeLineExercise itemTimeLineExercise) {
        this.mContext = mContext;
        this.itemTimeline = itemTimeLineExercise;

        DaoSession daoSession = ((AppController) mContext.getApplicationContext()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        user = userDao.loadAll().get(0);
        daoSession.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AbstractHolder.TYPE_HEADER;
        } else {
            return AbstractHolder.TYPE_COMMENT;
        }
    }

    @Override
    public AbstractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AbstractHolder.TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_exercise_detail, parent, false);
            eventDetail = new ItemEventDetailHolder(view, itemTimeline, user.getId() + "", user.getCapability());
            return eventDetail;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_in_post, parent, false);
            return new ItemCommentDetailHolder(view, itemTimeline, this);
        }
    }

    @Override
    public void onBindViewHolder(AbstractHolder holder, int position) {
        if (position != 0) {
            final ItemCommentDetailHolder commentHolder = (ItemCommentDetailHolder) holder;
            final ItemComment itemComment = itemTimeline.getItemComments().get(position - 1);
            commentHolder.setItemComment(itemComment, user.getId() + "", user.getCapability());

            commentHolder.updateIvIsSolved();

        } else {
            ItemEventDetailHolder postDetailHolder = (ItemEventDetailHolder) holder;

            postDetailHolder.setTitle(itemTimeline.getTitle());
            postDetailHolder.setContentToWebview(itemTimeline.getContent());
            postDetailHolder.setAuthorName(itemTimeline.getNameAuthor());

            postDetailHolder.getTvCreateAt().setText(itemTimeline.getCreateAt());

            postDetailHolder.setDeadline(itemTimeline.getRemainingTime());
            postDetailHolder.setPercentSubmitted(itemTimeline.getPercentSubmitted());
            postDetailHolder.setIsSendFile(itemTimeline.getIsSendFile());
//            Log.i("set" + position, "percent = " + itemTimeline.getPercentSubmitted());
        }
    }


    @Override
    public int getItemCount() {
        if (itemTimeline == null) {
            return 0;
        } else {
            return itemTimeline.getItemComments().size() + 1;
        }
    }

    public void setItemTimeline(ItemTimeLineExercise itemTimeline) {
        this.itemTimeline = itemTimeline;
        notifyDataSetChanged();
    }

    public void setItemComments(ArrayList<ItemComment> commentArr) {
        this.itemTimeline.setItemComments(commentArr);
        notifyDataSetChanged();
    }

    public ItemEventDetailHolder getEventDetail() {
        return eventDetail;
    }
}
