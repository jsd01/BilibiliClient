package com.jsd.blibiliclient.mvp.ui.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jess.arms.utils.ArmsUtils;
import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.app.data.entity.video.VideoDetail;
import com.jsd.blibiliclient.mvp.ui.fragment.video.ReplyFragment;
import com.jsd.blibiliclient.mvp.ui.fragment.video.SummaryFragment;

import java.util.ArrayList;

public class VideoDetailFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTabTitles;
    private Context  mContext;

    public VideoDetailFragmentAdapter(FragmentManager fm, Context context, VideoDetail videoDetail) {
        super(fm);
        mContext = context;
        String summaryStr = ArmsUtils.getString(mContext, R.string.v_detail_summary);
        String replyCountStr = mContext.getResources().getString(R.string.v_detail_evaluate, videoDetail.getSummary().getData().getStat() == null ? 0 : videoDetail.getSummary().getData().getStat().getReply());
        mTabTitles = new String[]{summaryStr, replyCountStr};
        mFragments.add(SummaryFragment.newInstance(videoDetail.getSummary()));
        mFragments.add(ReplyFragment.newInstance(videoDetail.getReply(), replyCountStr));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
