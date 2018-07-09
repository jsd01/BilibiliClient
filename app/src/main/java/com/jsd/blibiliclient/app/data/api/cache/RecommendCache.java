package com.jsd.blibiliclient.app.data.api.cache;


import com.jsd.blibiliclient.app.data.entity.recommend.RecommendIndexData;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

//@EncryptKey(ConstantUtil.ENCRYPT_KEY)
public interface RecommendCache {

    //    @Encrypt
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<RecommendIndexData>> getRecommendIndexData(Observable<RecommendIndexData> data, DynamicKey dynamicKey, EvictProvider evictProvider);
}
