package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.FlashModule;

import com.jsd.blibiliclient.mvp.ui.activity.FlashActivity;

@ActivityScope
@Component(modules = FlashModule.class, dependencies = AppComponent.class)
public interface FlashComponent {
    void inject(FlashActivity activity);
}