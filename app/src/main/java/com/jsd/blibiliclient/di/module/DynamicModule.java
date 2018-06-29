package com.jsd.blibiliclient.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.jsd.blibiliclient.mvp.contract.DynamicContract;
import com.jsd.blibiliclient.mvp.model.DynamicModel;


@Module
public class DynamicModule {
    private DynamicContract.View view;

    /**
     * 构建DynamicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DynamicModule(DynamicContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DynamicContract.View provideDynamicView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DynamicContract.Model provideDynamicModel(DynamicModel model) {
        return model;
    }
}