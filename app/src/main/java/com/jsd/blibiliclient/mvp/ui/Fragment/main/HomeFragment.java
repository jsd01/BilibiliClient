package com.jsd.blibiliclient.mvp.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.di.component.DaggerHomeComponent;
import com.jsd.blibiliclient.di.module.HomeModule;
import com.jsd.blibiliclient.mvp.contract.HomeContract;
import com.jsd.blibiliclient.mvp.presenter.HomePresenter;

import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.mvp.ui.adapter.MainHomeTabViewPagerAdapter;
import com.jsd.blibiliclient.mvp.ui.fragment.hometab.TabCategoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 首頁
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {
    @BindView(R.id.toolbar_title)
    TextView mToolBarTitle;
    @BindView(R.id.sliding_tabs)
    TabLayout mTabs;
    @BindView(R.id.main_home_viewpager)
    ViewPager mViewPager;
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mToolBarTitle.setText(R.string.main_tab_home);
        /*for (int i = 0; i < main_sliding_tabs.length; i++) {
            mTabs.addTab(mTabs.newTab().setText(main_sliding_tabs[i]));
        }*/
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(TabCategoryFragment.newInstance("1"));
            mFragments.add(TabCategoryFragment.newInstance("2"));
            mFragments.add(TabCategoryFragment.newInstance("3"));
            mFragments.add(TabCategoryFragment.newInstance("4"));
            mFragments.add(TabCategoryFragment.newInstance("5"));
        }
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mMainHomeTabViewPagerAdapter = new MainHomeTabViewPagerAdapter(getChildFragmentManager(),mFragments, main_sliding_tabs);
        mViewPager.setAdapter(mMainHomeTabViewPagerAdapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
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
