package com.jsd.blibiliclient.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jsd.blibiliclient.di.module.entity.live.LiveAppIndexInfo;
import com.jsd.blibiliclient.mvp.contract.TabCategoryContract;
import com.jsd.blibiliclient.mvp.model.api.LiveService;

import io.reactivex.Observable;


@ActivityScope
public class TabCategoryModel extends BaseModel implements TabCategoryContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TabCategoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<LiveAppIndexInfo> getLiveAppIndex() {
        return mRepositoryManager.obtainRetrofitService(LiveService.class)
                .getLiveAppIndex();
    }
}