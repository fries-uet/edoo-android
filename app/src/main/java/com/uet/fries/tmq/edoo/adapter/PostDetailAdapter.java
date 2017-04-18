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
import com.uet.fries.tmq.edoo.holder.ItemPostDetailHolder;
import com.uet.fries.tmq.edoo.model.ITimelineBase;
import com.uet.fries.tmq.edoo.model.ItemComment;
import com.uet.fries.tmq.edoo.model.ItemTimeLinePost;

import java.util.ArrayList;

public class PostDetailAdapter extends RecyclerView.Adapter<AbstractHolder> {

    private Context mContext;
    private ItemTimeLinePost itemTimeline;
    private User user;

    public PostDetailAdapter(Context mContext, ItemTimeLinePost itemTimeline) {
        this.mContext = mContext;
        this.itemTimeline = itemTimeline;

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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_detail, parent, false);
            return new ItemPostDetailHolder(view, itemTimeline, user.getId() + "");
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
            ItemPostDetailHolder postDetailHolder = (ItemPostDetailHolder) holder;

            postDetailHolder.setTitle(itemTimeline.getTitle());
            postDetailHolder.setContentToWebview(itemTimeline.getContent());
            postDetailHolder.setAuthorName(itemTimeline.getNameAuthor());
            postDetailHolder.setComment(itemTimeline.getItemComments().size() + "");
            postDetailHolder.setLike(itemTimeline.getLike() + "");
            if (itemTimeline.getLike() >= 0) {
                postDetailHolder.getIvLike().setImageResource(R.drawable.ic_vote_up);
            } else {
                postDetailHolder.getIvLike().setImageResource(R.drawable.ic_vote_down);
            }

            postDetailHolder.getTvCreateAt().setText(itemTimeline.getCreateAt());

            postDetailHolder.setCbIsVote();

            setResourceTypePost(postDetailHolder, itemTimeline.getType());
        }
    }

    private void setResourceTypePost(ItemPostDetailHolder postDetailHolder, String type) {
        int idDrawable = android.R.color.white;
        if (type.equals(ITimelineBase.TYPE_POST_NOTE)) idDrawable = R.drawable.ic_type_post_note;
        else if (type.equals(ITimelineBase.TYPE_POST_QUESTION))
            idDrawable = R.drawable.ic_type_post_question;
        else if (type.equals(ITimelineBase.TYPE_POST_POLL))
            idDrawable = R.drawable.ic_type_post_poll;
        else if (type.equals(ITimelineBase.TYPE_POST_NOTIFICATION))
            idDrawable = R.drawable.ic_type_post_notification;
        postDetailHolder.getIvTypePost().setImageResource(idDrawable);
    }

    @Override
    public int getItemCount() {
        if (itemTimeline == null){
            return 0;
        } else {
            return itemTimeline.getItemComments().size() + 1;
        }
    }

    public void setItemTimeline(ItemTimeLinePost itemTimeline) {
        this.itemTimeline = itemTimeline;
        notifyDataSetChanged();
    }

    public void setItemComments(ArrayList<ItemComment> commentArr) {
        this.itemTimeline.setItemComments(commentArr);
        notifyDataSetChanged();
    }

    public void setSolveCmt(String cmtId) {
        ArrayList<ItemComment> cmts = itemTimeline.getItemComments();
        for (int i = 0; i < cmts.size(); i++) {
            if (cmts.get(i).getIdComment().equalsIgnoreCase(cmtId)) {
                cmts.get(i).setIsSolved(true);
            } else {
                cmts.get(i).setIsSolved(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setUnsolveCmt() {
        ArrayList<ItemComment> cmts = itemTimeline.getItemComments();
        for (int i = 0; i < cmts.size(); i++) {
            cmts.get(i).setIsSolved(false);
        }
        notifyDataSetChanged();
    }
}
