package com.jsd.blibiliclient.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;
import com.jess.arms.utils.DataHelper;
import com.jsd.blibiliclient.app.config.applyOptions.MyGlobalHttpHandler;
import com.jsd.blibiliclient.app.config.applyOptions.MyGsonConfiguration;
import com.jsd.blibiliclient.app.config.applyOptions.MyResponseErrorListener;
import com.jsd.blibiliclient.app.config.applyOptions.MyRetrofitConfiguration;
import com.jsd.blibiliclient.app.config.applyOptions.MyRxCacheConfiguration;
import com.jsd.blibiliclient.app.config.applyOptions.intercept.UserAgentInterceptor;
import com.jsd.blibiliclient.app.config.lifecyclesOptions.MyActivityLifecycle;
import com.jsd.blibiliclient.app.config.lifecyclesOptions.MyAppLifecycles;
import com.jsd.blibiliclient.app.data.api.Api;

import java.io.File;
import java.util.List;

/**
 * 项目名称：BlibiliClient
 * 类描述：
 * 创建人：贾少东
 * 创建时间：2018-06-27 16:02
 * 修改人：jsd
 * 修改时间：2018-06-27 16:02
 * 修改备注：
 */
public class GlobalConfiguration implements ConfigModule{
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        //使用builder可以为框架配置一些配置信息
        builder//.baseurl(Api.APP_DOMAIN)
                .retrofitConfiguration(new MyRetrofitConfiguration())
                // 使用统一UserAgent
//                .addInterceptor(new UserAgentInterceptor())
                .rxCacheConfiguration(new MyRxCacheConfiguration())
                .globalHttpHandler(new MyGlobalHttpHandler())
                .responseErrorListener(new MyResponseErrorListener())
                .cacheFile(new File(DataHelper.getCacheFile(context), "rxCache"))
                .gsonConfiguration(new MyGsonConfiguration());
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        //向Application的生命周期中注入一些自定义逻辑
        lifecycles.add(new MyAppLifecycles());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        //向Activity的生命周期中注入一些自定义逻辑
        lifecycles.add(new MyActivityLifecycle());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //向Fragment的生命周期中注入一些自定义逻辑
    }
}
