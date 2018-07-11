package com.jsd.blibiliclient.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.app.ARouterPaths;
import com.jsd.blibiliclient.app.base.BaseSupportActivity;
import com.jsd.blibiliclient.di.component.DaggerVideoDetailComponent;
import com.jsd.blibiliclient.di.module.VideoDetailModule;
import com.jsd.blibiliclient.mvp.contract.VideoDetailContract;
import com.jsd.blibiliclient.mvp.presenter.VideoDetailPresenter;

import com.jsd.blibiliclient.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 视频详情
 */
@Route(path = ARouterPaths.VIDEO_DETAIL)
public class VideoDetailActivity extends BaseSupportActivity<VideoDetailPresenter> implements VideoDetailContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoDetailModule(new VideoDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_video_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
}
