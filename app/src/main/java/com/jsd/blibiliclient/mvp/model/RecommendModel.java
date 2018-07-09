package com.jsd.blibiliclient.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jsd.blibiliclient.app.data.api.Api;
import com.jsd.blibiliclient.app.data.api.cache.RecommendCache;
import com.jsd.blibiliclient.app.data.api.service.RecommendService;
import com.jsd.blibiliclient.app.data.entity.recommend.RecommendIndexData;
import com.jsd.blibiliclient.mvp.contract.RecommendContract;
import com.jsd.blibiliclient.mvp.ui.adapter.entity.RecommendMultiItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.Reply;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


@ActivityScope
public class RecommendModel extends BaseModel implements RecommendContract.Model {
    private boolean isOdd = true;
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RecommendModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        RetrofitUrlManager.getInstance().putDomain("recommend", Api.RECOMMEND_BASE_URL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<RecommendIndexData> getRecommendIndexData(int index, boolean refresh, boolean clearCache) {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(RecommendService.class)
                .getRecommendIndexData(index, refresh ? "true" : "false", clearCache ? 1 : 0))
                .flatMap((Function<Observable<RecommendIndexData>, ObservableSource<RecommendIndexData>>) recommendIndexDataObservable ->
                        mRepositoryManager
                                .obtainCacheService(RecommendCache.class)
                                .getRecommendIndexData(recommendIndexDataObservable, new DynamicKey(index), new EvictProvider(clearCache))
                                .map(recommendIndexDataReply -> recommendIndexDataReply.getData())
                );
    }

    @Override
    public List<RecommendMultiItem> parseIndexData(RecommendIndexData indexData) {
        List<RecommendMultiItem> list = new ArrayList<>();
        List<RecommendIndexData.DataBean> data = indexData.getData();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                RecommendIndexData.DataBean dataBean = data.get(i);
                if (dataBean != null) {
                    String gotoX = dataBean.getGotoX();
                    if (RecommendMultiItem.isWeNeed(gotoX)) {
                        RecommendMultiItem item = new RecommendMultiItem();
                        item.setItemTypeWithGoto(gotoX, dataBean.getRcmd_reason() == null);
                        item.setIndexDataBean(dataBean);
                        if (RecommendMultiItem.isItemData(gotoX)) {
                            item.setOdd(isOdd);
                            isOdd = !isOdd;
                        } else {
                            isOdd = true;
                        }
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }
}