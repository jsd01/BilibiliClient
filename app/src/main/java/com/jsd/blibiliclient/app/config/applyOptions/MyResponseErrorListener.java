package com.jsd.blibiliclient.app.config.applyOptions;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.jess.arms.utils.ArmsUtils;

import org.json.JSONException;
import org.simple.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import retrofit2.HttpException;


public class MyResponseErrorListener implements ResponseErrorListener {
    @Override
    public void handleResponseError(Context context, Throwable t) {
        /* 用来提供处理所有错误的监听
           rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
        String msg = "未知错误";
        Exception e = (Exception) t.getCause();
        Throwable topThrowable = e.getCause();
        if (topThrowable instanceof UnknownHostException) {/*去设置*/
            msg = "网络不可用";
        } else if (topThrowable instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (topThrowable instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (topThrowable instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }else {
            EventBus.getDefault().post(msg, "未知错误");
        }

        ArmsUtils.snackbarText(msg);
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
