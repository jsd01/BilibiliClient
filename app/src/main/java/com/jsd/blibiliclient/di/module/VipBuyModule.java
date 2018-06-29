package com.jsd.blibiliclient.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.jsd.blibiliclient.mvp.contract.VipBuyContract;
import com.jsd.blibiliclient.mvp.model.VipBuyModel;


@Module
public class VipBuyModule {
    private VipBuyContract.View view;

    /**
     * 构建VipBuyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VipBuyModule(VipBuyContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VipBuyContract.View provideVipBuyView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VipBuyContract.Model provideVipBuyModel(VipBuyModel model) {
        return model;
    }
}