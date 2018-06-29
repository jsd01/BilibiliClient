package com.jsd.blibiliclient.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.jsd.blibiliclient.mvp.contract.ChannelContract;
import com.jsd.blibiliclient.mvp.model.ChannelModel;


@Module
public class ChannelModule {
    private ChannelContract.View view;

    /**
     * 构建ChannelModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChannelModule(ChannelContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChannelContract.View provideChannelView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChannelContract.Model provideChannelModel(ChannelModel model) {
        return model;
    }
}