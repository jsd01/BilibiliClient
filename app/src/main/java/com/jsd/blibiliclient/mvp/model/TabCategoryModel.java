package com.jsd.blibiliclient.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jsd.blibiliclient.app.data.api.Api;
import com.jsd.blibiliclient.app.data.entity.live.LiveAppIndexInfo;
import com.jsd.blibiliclient.mvp.contract.TabCategoryContract;
import com.jsd.blibiliclient.app.data.api.service.LiveService;

import io.reactivex.Observable;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


@ActivityScope
public class TabCategoryModel extends BaseModel implements TabCategoryContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TabCategoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        RetrofitUrlManager.getInstance().putDomain("live", Api.LIVE_BASE_URL);
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