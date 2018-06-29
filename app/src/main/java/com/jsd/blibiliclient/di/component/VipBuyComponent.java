package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.VipBuyModule;

import com.jsd.blibiliclient.mvp.ui.fragment.main.VipBuyFragment;

@ActivityScope
@Component(modules = VipBuyModule.class, dependencies = AppComponent.class)
public interface VipBuyComponent {
    void inject(VipBuyFragment fragment);
}