package com.jsd.blibiliclient.mvp.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.app.EventBusTags;
import com.jsd.blibiliclient.app.base.BaseSupportFragment;
import com.jsd.blibiliclient.di.component.DaggerHomeComponent;
import com.jsd.blibiliclient.di.module.HomeModule;
import com.jsd.blibiliclient.mvp.contract.HomeContract;
import com.jsd.blibiliclient.mvp.presenter.HomePresenter;

import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.mvp.ui.adapter.MainHomeTabViewPagerAdapter;
import com.jsd.blibiliclient.mvp.ui.fragment.hometab.LiveFragment;
import com.jsd.blibiliclient.mvp.ui.fragment.hometab.RecommendFragment;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 首頁
 */
public class HomeFragment extends BaseSupportFragment<HomePresenter> implements HomeContract.View {
    @BindView(R.id.toolbar_title)
    TextView mToolBarTitle;
    @BindView(R.id.sliding_tabs)
    TabLayout mTabs;
    @BindView(R.id.main_home_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @OnClick(R.id.toolbar)
    void openDrawer(){
        EventBus.getDefault().post(new Object(), EventBusTags.FRAGMENT_TOOLBAR_DRAWER);
    }
    @BindArray(R.array.main_sliding_tabs)
    String[] tabStrs;

    @BindArray(R.array.main_sliding_tabs)
    String[] main_sliding_tabs;

    private List<Fragment> mFragments;
    private MainHomeTabViewPagerAdapter mMainHomeTabViewPagerAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ((AppCompatActivity)_mActivity).setSupportActionBar(toolbar);
        ((AppCompatActivity) _mActivity).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_home_nemu, menu);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mToolBarTitle.setText(R.string.main_tab_home);
        /*for (int i = 0; i < main_sliding_tabs.length; i++) {
            mTabs.addTab(mTabs.newTab().setText(main_sliding_tabs[i]));
        }*/
        initBottomTabViewPager();
    }

    private void initBottomTabViewPager() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(LiveFragment.newInstance("1"));
            mFragments.add(RecommendFragment.newInstance("2"));
            mFragments.add(LiveFragment.newInstance("3"));
            mFragments.add(LiveFragment.newInstance("4"));
            mFragments.add(LiveFragment.newInstance("5"));
        }
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mMainHomeTabViewPagerAdapter = new MainHomeTabViewPagerAdapter(getChildFragmentManager(),mFragments, main_sliding_tabs);
        mViewPager.setAdapter(mMainHomeTabViewPagerAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void setData(@Nullable Object data) {

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

    }
}
