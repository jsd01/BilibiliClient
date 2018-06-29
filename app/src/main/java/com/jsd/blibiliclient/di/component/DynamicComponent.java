package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.DynamicModule;

import com.jsd.blibiliclient.mvp.ui.fragment.main.DynamicFragment;

@ActivityScope
@Component(modules = DynamicModule.class, dependencies = AppComponent.class)
public interface DynamicComponent {
    void inject(DynamicFragment fragment);
}