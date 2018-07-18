package com.jsd.blibiliclient.mvp.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.di.component.DaggerFlashComponent;
import com.jsd.blibiliclient.di.module.FlashModule;
import com.jsd.blibiliclient.mvp.contract.FlashContract;
import com.jsd.blibiliclient.mvp.presenter.FlashPresenter;

import com.jsd.blibiliclient.R;


import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.jsd.blibiliclient.app.ARouterPaths.MAIN;


public class FlashActivity extends BaseActivity<FlashPresenter> implements FlashContract.View {
    @BindView(R.id.imageView)
    ImageView imageView;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFlashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .flashModule(new FlashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_flash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"alpha", 0, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ARouter.getInstance().build(MAIN)
                        .navigation();
                killMyself();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
