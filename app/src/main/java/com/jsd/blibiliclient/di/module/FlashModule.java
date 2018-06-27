package com.jsd.blibiliclient.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.jsd.blibiliclient.mvp.contract.FlashContract;
import com.jsd.blibiliclient.mvp.model.FlashModel;


@Module
public class FlashModule {
    private FlashContract.View view;

    /**
     * 构建FlashModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FlashModule(FlashContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FlashContract.View provideFlashView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FlashContract.Model provideFlashModel(FlashModel model) {
        return model;
    }
}