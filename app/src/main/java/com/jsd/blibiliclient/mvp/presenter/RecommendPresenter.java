package com.jsd.blibiliclient.mvp.presenter;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.app.ARouterPaths;
import com.jsd.blibiliclient.app.data.entity.recommend.RecommendIndexData;
import com.jsd.blibiliclient.app.widget.CustomLoadMoreView;
import com.jsd.blibiliclient.mvp.contract.RecommendContract;
import com.jsd.blibiliclient.mvp.ui.adapter.RecommendMultiitemAtapter;
import com.jsd.blibiliclient.mvp.ui.adapter.entity.RecommendMultiItem;

import java.util.List;


@ActivityScope
public class RecommendPresenter extends BasePresenter<RecommendContract.Model, RecommendContract.View> implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener  {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private RecommendMultiitemAtapter mAdapter;

    @Inject
    public RecommendPresenter(RecommendContract.Model model, RecommendContract.View rootView) {
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

    public int getIdx(boolean refresh) {
        if (mAdapter == null){
            return 0;
        }
        List<RecommendMultiItem> data = mAdapter.getData();
        if (data == null || data.size() == 0) {
            return 0;
        }
        int index = refresh ? 0 : data.size() - 1;
        return data.get(index).getIndexDataBean() == null ? 0 : data.get(index).getIndexDataBean().getIdx();
    }

    public void loadData(int index, boolean refresh, boolean clearCache) {
        mModel.getRecommendIndexData(index,refresh,clearCache)
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mRootView.showLoading())
                .doFinally(() -> mRootView.hideLoading())
                .observeOn(Schedulers.io())
                .map(recommendIndexData -> mModel.parseIndexData(recommendIndexData))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<RecommendMultiItem>>(mErrorHandler) {
                    @Override
                    public void onNext(List<RecommendMultiItem> recommendMultiItems) {
                        setAdapter(recommendMultiItems, refresh);
                    }

                    @Override
                    public void onError(Throwable t) {/*处理*/
                        super.onError(t);/*全局处理后如果不包含(全局的网络设置弹出框等)，接下来自己处理（展示异常页面）*/
                        if (mAdapter == null)mRootView.showEmptyView();
                    }
                });
       /* mModel.getRecommendIndexData(index, refresh, clearCache)
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(Schedulers.io())*//*之前*//*
                .observeOn(AndroidSchedulers.mainThread())*//*之后，直到observeOn在此出现*//*
                .doOnSubscribe(new Consumer<Disposable>() {*//*与create相同被之后的subscribe控制*//*
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (refresh || clearCache) mRootView.showLoading();
                    }
                })
                .doFinally(() -> mRootView.hideLoading())
                .observeOn(Schedulers.io())
                .map(recommendIndexData -> mModel.parseIndexData(recommendIndexData))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))*//*实现一系列操作符的复用*//*
                .subscribe(new ErrorHandleSubscriber<List<RecommendMultiItem>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull List<RecommendMultiItem> recommendMultiItems) {
                        if (recommendMultiItems != null) {
                            setAdapter(recommendMultiItems, refresh);
                        }
                    }
                });*/
    }


    public void setAdapter(List<RecommendMultiItem> recommendMultiItems, boolean refresh) {
        if (mAdapter == null) {
            mRootView.initEmptyView();
            mAdapter = new RecommendMultiitemAtapter(recommendMultiItems);
            mRootView.setAdapter(mAdapter);
            mAdapter.setEnableLoadMore(true);
            mAdapter.setPreLoadNumber(1);
            mAdapter.setLoadMoreView(new CustomLoadMoreView());
            mAdapter.setOnLoadMoreListener(this);
            mAdapter.setOnItemClickListener(this);

        } else {
            if (refresh) {
                removePreRefreshItem();
                addPreRefreshItem();
                mAdapter.addData(0, recommendMultiItems);
                mRootView.recyclerScrollToPosition(0);
            } else {
                mAdapter.addData(recommendMultiItems);
                mAdapter.loadMoreComplete();
            }
        }

    }


    public void removePreRefreshItem() {
        List<RecommendMultiItem> data = mAdapter.getData();
        if (data == null || data.size() == 0) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getItemType() == RecommendMultiItem.PRE_HERE_CLICK_TO_REFRESH) {
                data.remove(i);
                break;
            }
        }
    }

    public void addPreRefreshItem() {
        RecommendMultiItem item = new RecommendMultiItem();
        item.setItemType(RecommendMultiItem.PRE_HERE_CLICK_TO_REFRESH);
        mAdapter.addData(0, item);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(getIdx(false), false, false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<RecommendMultiItem> data = adapter.getData();
        RecommendMultiItem item = data.get(position);
        if (RecommendMultiItem.isVideoItem(item.getItemType())){
            ARouter.getInstance().build(ARouterPaths.VIDEO_DETAIL).withString("aid", item.getIndexDataBean().getParam()).navigation();
        }
    }
}
