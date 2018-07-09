package com.jsd.blibiliclient.app.data.api;

/**
 * 项目名称：BlibiliClient
 * 类描述：
 * 创建人：贾少东
 * 创建时间：2018-07-03 10:57
 * 修改人：jsd
 * 修改时间：2018-07-03 10:57
 * 修改备注：
 */
public interface Api {

    String LIVE_BASE_URL = "http://api.live.bilibili.com/";
    String RECOMMEND_BASE_URL = "https://app.bilibili.com/";
    String VIDEO_DETAIL_SUMMARY_BASE_URL = "https://app.bilibili.com/";
    String VIDEO_DETAIL_REPLY_BASE_URL = "https://api.bilibili.com/";

    // 没有登录的情况下，使用这个User-Agent
    String COMMON_UA_STR = "Mozilla/5.0 BiliDroid/5.15.0 (bbcallen@gmail.com)";
}
