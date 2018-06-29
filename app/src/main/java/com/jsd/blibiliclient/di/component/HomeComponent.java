package com.jsd.blibiliclient.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jsd.blibiliclient.di.module.HomeModule;

import com.jsd.blibiliclient.mvp.ui.fragment.main.HomeFragment;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeFragment fragment);
}