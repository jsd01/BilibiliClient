package com.jsd.blibiliclient.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.app.utils.FragmentUtils;
import com.jsd.blibiliclient.di.component.DaggerMainComponent;
import com.jsd.blibiliclient.di.module.MainModule;
import com.jsd.blibiliclient.mvp.contract.MainContract;
import com.jsd.blibiliclient.mvp.presenter.MainPresenter;

import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.mvp.ui.fragment.main.HomeFragment;
import com.jsd.blibiliclient.mvp.ui.fragment.main.ChannelFragment;
import com.jsd.blibiliclient.mvp.ui.fragment.main.DynamicFragment;
import com.jsd.blibiliclient.mvp.ui.fragment.main.VipBuyFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.jsd.blibiliclient.app.ARouterPaths.MAIN;
import static com.jsd.blibiliclient.app.EventBusTags.ACTIVITY_FRAGMENT_REPLACE;

@Route(path = MAIN)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_nav)
    NavigationView mNavigationView;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.app_main_icon_user)
    ImageView mUserIcon;


    private List<Fragment> mFragments;
    private List<Integer> mNavIds;
    private int mMainPageReplace = 0;
    private int mTabPageReplace = 0;

    private RxPermissions mRxPermissions;

    private OnTabSelectListener mOnTabSelectListener = tabId -> {
        switch (tabId) {
            case R.id.main_tab_home:
                mMainPageReplace = 0;
                break;
            case R.id.main_tab_channel:
                mMainPageReplace = 1;
                break;
            case R.id.main_tab_dynamic:
                mMainPageReplace = 2;
                break;
            case R.id.main_tab_vipbuy:
                mMainPageReplace = 3;
                break;
        }
        FragmentUtils.hideAllShowFragment(mFragments.get(mMainPageReplace));
    };

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

       /* if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add(R.string.main_tab_home);
            mTitles.add(R.string.main_tab_channel);
            mTitles.add(R.string.main_tab_dynamic);
            mTitles.add(R.string.main_tab_vipbuy);
        }*/
        mPresenter.requestPermissions();
        if (mNavIds == null) {
            mNavIds = new ArrayList<>();
            mNavIds.add(R.id.main_tab_home);
            mNavIds.add(R.id.main_tab_channel);
            mNavIds.add(R.id.main_tab_dynamic);
            mNavIds.add(R.id.main_tab_vipbuy);
        }
        mNavigationView.setNavigationItemSelectedListener(this);
        HomeFragment homeFragment;
        ChannelFragment channelFragment;
        DynamicFragment dynamicFragment;
        VipBuyFragment vipbuyFragment;
        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            channelFragment = ChannelFragment.newInstance();
            dynamicFragment = DynamicFragment.newInstance();
            vipbuyFragment = VipBuyFragment.newInstance();
        } else {
            mMainPageReplace = savedInstanceState.getInt(ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = (HomeFragment) FragmentUtils.findFragment(fm, HomeFragment.class);
            channelFragment = (ChannelFragment) FragmentUtils.findFragment(fm, ChannelFragment.class);
            dynamicFragment = (DynamicFragment) FragmentUtils.findFragment(fm, DynamicFragment.class);
            vipbuyFragment = (VipBuyFragment) FragmentUtils.findFragment(fm, VipBuyFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(homeFragment);
            mFragments.add(channelFragment);
            mFragments.add(dynamicFragment);
            mFragments.add(vipbuyFragment);
        }
        FragmentUtils.addFragments(getSupportFragmentManager(), mFragments, R.id.main_frame, 0);
        mBottomBar.setOnTabSelectListener(mOnTabSelectListener);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前Activity显示的Fragment索引
        outState.putInt(ACTIVITY_FRAGMENT_REPLACE, mMainPageReplace);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_historyrecord:
                break;
            case R.id.nav_offlinecach:
                break;
            case R.id.nav_collection:
                break;
            case R.id.nav_follow:
                break;
            case R.id.nav_waitlook:
                break;


            case R.id.nav_onlivecenter:
                break;
            case R.id.nav_vip:
                break;
            case R.id.nav_freeflow:
                break;
            case R.id.nav_vipbuy:
                break;
            case R.id.nav_custom:
                break;
        }
        mDrawerLayout.closeDrawer(mNavigationView);
        return true;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }
}
