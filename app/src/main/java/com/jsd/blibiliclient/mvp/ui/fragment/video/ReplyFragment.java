package com.jsd.blibiliclient.mvp.ui.fragment.video;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.app.base.BaseSupportFragment;
import com.jsd.blibiliclient.app.data.entity.video.Reply;
import com.jsd.blibiliclient.mvp.ui.adapter.ReplyMultiItemAdapter;
import com.jsd.blibiliclient.mvp.ui.adapter.entity.ReplyMultiItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @创建者 CSDN_LQR
 * @描述 评论
 */
@SuppressLint("ValidFragment")
public class ReplyFragment extends BaseSupportFragment {

    private List<ReplyMultiItem> mData = new ArrayList<>();
    private Reply  mReply;
    private String mReplyCountStr;
    private View   mRootView;

    @BindView(R.id.rv_reply)
    RecyclerView mRvReply;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;

    public ReplyFragment(Reply reply, String replyCountStr) {
        mReply = reply;
        mReplyCountStr = replyCountStr;
    }

    public static ReplyFragment newInstance(Reply reply, String replyCountStr) {
        return new ReplyFragment(reply, replyCountStr);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_reply_video_detail, container, false);
        }
        return mRootView;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mReply == null || mReply.getData() == null) {
            return;
        }
        Reply.DataBean data = mReply.getData();
        initVideoReply(data);
        initRecyclerView();
    }

    private void initVideoReply(Reply.DataBean replyDataBean) {
        List<Reply.DataBean.RepliesBean> hots = replyDataBean.getHots();
        if (hots != null && hots.size() > 0) {
            for (int i = 0; i < hots.size(); i++) {
                mData.add(new ReplyMultiItem(ReplyMultiItem.ITEM, hots.get(i)));
            }
            mData.add(new ReplyMultiItem(ReplyMultiItem.TITLE_HOTS));
        }
        List<Reply.DataBean.RepliesBean> replies = replyDataBean.getReplies();
        if (replies != null && replies.size() > 0) {
            for (int i = 0; i < replies.size(); i++) {
                mData.add(new ReplyMultiItem(ReplyMultiItem.ITEM, replies.get(i)));
            }
        }
    }

    private void initRecyclerView() {
        View headerView = View.inflate(_mActivity, R.layout.item_title_reply_video_detail, null);
        ((TextView) headerView.findViewById(R.id.tv_reply_count)).setText(mReplyCountStr);

        ReplyMultiItemAdapter adapter = new ReplyMultiItemAdapter(mData);
        adapter.addHeaderView(headerView);

        mRvReply.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRvReply.setAdapter(adapter);
    }

    @Override
    public void setData(Object data) {

    }
}
