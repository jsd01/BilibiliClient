package com.jsd.blibiliclient.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.jsd.blibiliclient.mvp.contract.TabCategoryContract;
import com.jsd.blibiliclient.mvp.model.TabCategoryModel;


@Module
public class TabCategoryModule {
    private TabCategoryContract.View view;

    /**
     * 构建TabCategoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TabCategoryModule(TabCategoryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TabCategoryContract.View provideTabCategoryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TabCategoryContract.Model provideTabCategoryModel(TabCategoryModel model) {
        return model;
    }
}