package com.jsd.blibiliclient.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * 项目名称：BlibiliClient
 * 类描述：
 * 创建人：贾少东
 * 创建时间：2018-06-28 16:49
 * 修改人：jsd
 * 修改时间：2018-06-28 16:49
 * 修改备注：
 */
public class MainHomeTabViewPagerAdapter extends FragmentPagerAdapter {
    private  String[] mTabs;
    private List<Fragment> mFragments;

    public MainHomeTabViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments,  String[] mTabs) {
        super(fm);
        this.mFragments = mFragments;
        this.mTabs = mTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {/*TabLayout与Viewpager关联后，tab不显示文字*/
        return mTabs[position];
    }
}
