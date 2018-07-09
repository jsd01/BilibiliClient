package com.jsd.blibiliclient.mvp.ui.fragment.hometab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.app.EventBusTags;
import com.jsd.blibiliclient.app.base.BaseSupportFragment;
import com.jsd.blibiliclient.app.widget.CustomEmptyView;
import com.jsd.blibiliclient.di.component.DaggerRecommendComponent;
import com.jsd.blibiliclient.di.module.RecommendModule;
import com.jsd.blibiliclient.mvp.contract.RecommendContract;
import com.jsd.blibiliclient.mvp.presenter.RecommendPresenter;

import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.mvp.ui.adapter.RecommendMultiitemAtapter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 推荐
 */
public class RecommendFragment extends BaseSupportFragment<RecommendPresenter> implements RecommendContract.View {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    public static RecommendFragment newInstance(String s) {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerRecommendComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .recommendModule(new RecommendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(ArmsUtils.getColor(_mActivity, R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            //刷新数据
            mPresenter.loadData(mPresenter.getIdx(true), true, false);
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //初始化数据
        mPresenter.loadData(0, false, true);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void setAdapter(RecommendMultiitemAtapter adapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 上拉加载更多视图
                if (position == adapter.getData().size()) {
                    return 1;
                }
                return adapter.getData().get(position).getSpanSize();
            }
        });
    }

    @Override
    public void recyclerScrollToPosition(int position) {
        mRecyclerView.scrollToPosition(position);
    }
}
