package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.ChannelModule;

import com.jsd.blibiliclient.mvp.ui.fragment.main.ChannelFragment;

@ActivityScope
@Component(modules = ChannelModule.class, dependencies = AppComponent.class)
public interface ChannelComponent {
    void inject(ChannelFragment fragment);
}