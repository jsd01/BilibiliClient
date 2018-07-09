package com.jsd.blibiliclient.app.data.api.service;

import com.jsd.blibiliclient.app.data.entity.live.LiveAppIndexInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by hcc on 16/8/4 12:03
 * 100332338@qq.com
 * <p>
 * 直播相关api
 */
public interface LiveService {

    /**
     * 首页直播
     */
    @Headers({"Domain-Name: live"})
    @GET("AppIndex/home?_device=android&_hwid=51e96f5f2f54d5f9&_ulv=10000&access_key=563d6046f06289cbdcb472601ce5a761&appkey=c1b107428d337928&build=410000&platform=android&scale=xxhdpi&sign=fbdcfe141853f7e2c84c4d401f6a8758")
    Observable<LiveAppIndexInfo> getLiveAppIndex();

}
