package com.jsd.blibiliclient.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jsd.blibiliclient.app.utils.RxUtils;
import com.jsd.blibiliclient.di.module.entity.live.LiveAppIndexInfo;
import com.jsd.blibiliclient.mvp.contract.TabCategoryContract;
import com.jsd.blibiliclient.mvp.ui.adapter.HomeLiveAdapter;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class TabCategoryPresenter extends BasePresenter<TabCategoryContract.Model, TabCategoryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private HomeLiveAdapter mAdapter;
    private List<LiveAppIndexInfo> mData = new ArrayList<>();
    private int preEndIndex;
    private LiveAppIndexInfo mLiveAppIndexInfo;

    @Inject
    public TabCategoryPresenter(TabCategoryContract.Model model, TabCategoryContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /*获取直播数据,true获取或者刷新数据， false加载更多或其他*/
    public void requestLiveData(boolean pullToRefresh){
        if (mAdapter == null) {
            mAdapter = new HomeLiveAdapter(mApplication);
            mRootView.setAdapter(mAdapter);//设置Adapter
        }

        mModel.getLiveAppIndex()//Observable<GankEntity>
                .subscribeOn(Schedulers.io())//gank操作在io中
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示上拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示下拉加载更多的进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏上拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏下拉加载更多的进度条
                })
//                .compose(RxUtils.bindToLifecycle(mRootView))//使用Rxlifecycle,使Disposable和Activity一起销毁
                .subscribe(new ErrorHandleSubscriber<LiveAppIndexInfo>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull LiveAppIndexInfo liveAppIndexInfo) {
                        mLiveAppIndexInfo = liveAppIndexInfo;
                        mAdapter.setLiveInfo(liveAppIndexInfo);
                        preEndIndex = mAdapter.getItemCount();
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, liveAppIndexInfo.getData().getPartitions().size() * 5);
                    }
                });
    }
}
