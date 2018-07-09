package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.TabCategoryModule;

import com.jsd.blibiliclient.mvp.ui.fragment.hometab.LiveFragment;

@ActivityScope
@Component(modules = TabCategoryModule.class, dependencies = AppComponent.class)
public interface TabCategoryComponent {
    void inject(LiveFragment fragment);
}