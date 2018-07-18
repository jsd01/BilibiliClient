package com.jsd.blibiliclient.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jsd.blibiliclient.app.data.api.service.VideoDetailService;
import com.jsd.blibiliclient.app.data.entity.video.PlayUrl;
import com.jsd.blibiliclient.app.data.entity.video.Reply;
import com.jsd.blibiliclient.app.data.entity.video.Summary;
import com.jsd.blibiliclient.mvp.contract.VideoDetailContract;

import io.reactivex.Observable;


@ActivityScope
public class VideoDetailModel extends BaseModel implements VideoDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VideoDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<Summary> getSummaryData(String aid) {
        return mRepositoryManager.obtainRetrofitService(VideoDetailService.class).getSummaryData(aid);
    }

    @Override
    public Observable<Reply> getReplyData(String oid, int pn, int ps) {
        return mRepositoryManager.obtainRetrofitService(VideoDetailService.class).getReplyData(oid, pn, ps);
    }

    @Override
    public Observable<PlayUrl> getPlayurl(String aid) {
        return mRepositoryManager.obtainRetrofitService(VideoDetailService.class).getPlayurl(aid);
    }
}