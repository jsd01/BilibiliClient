package com.jsd.blibiliclient.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.app.data.entity.recommend.RecommendIndexData;
import com.jsd.blibiliclient.app.utils.ConstantUtil;
import com.jsd.blibiliclient.app.utils.TextHandleUtil;
import com.jsd.blibiliclient.app.widget.banner.BannerEntity;
import com.jsd.blibiliclient.app.widget.banner.BannerView;
import com.jsd.blibiliclient.mvp.ui.adapter.entity.RecommendMultiItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 项目名称：BlibiliClient
 * 类描述：
 * 创建人：贾少东
 * 创建时间：2018-07-05 17:35
 * 修改人：jsd
 * 修改时间：2018-07-05 17:35
 * 修改备注：
 */
public class RecommendMultiitemAtapter extends BaseMultiItemQuickAdapter<RecommendMultiItem, BaseViewHolder> {

    private AppComponent mAppComponent;

    public RecommendMultiitemAtapter(List<RecommendMultiItem> data) {
        super(data);
        addItemType(RecommendMultiItem.BANNER, R.layout.item_banner_recommend);
        addItemType(RecommendMultiItem.STREAMER, R.layout.item_streamer_recommend);
        addItemType(RecommendMultiItem.ITEM_AV, R.layout.item_item_av_recommend);
        addItemType(RecommendMultiItem.ITEM_AV_RCMD_REASON, R.layout.item_item_av_rcmd_reason_recommend);
        addItemType(RecommendMultiItem.ITEM_BANGUMI, R.layout.item_item_bangumi_recommend);
        addItemType(RecommendMultiItem.ITEM_LOGIN, R.layout.item_item_login_recommend);
        addItemType(RecommendMultiItem.ITEM_AD_WEB_S, R.layout.item_item_ad_web_s_recommend);
        addItemType(RecommendMultiItem.ITEM_ARTICLE_S, R.layout.item_item_article_s_recommend);
        addItemType(RecommendMultiItem.PRE_HERE_CLICK_TO_REFRESH, R.layout.item_pre_here_click_to_refresh_recommend);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendMultiItem item) {
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(mContext);
        }
        RecommendIndexData.DataBean itemBean = item.getIndexDataBean();
        switch (item.getItemType()){
            case RecommendMultiItem.BANNER:
                BannerView bannerView = helper.getView(R.id.banner);
                List<RecommendIndexData.DataBean.BannerItemBean> bannerItemBeans = itemBean.getBanner_item();
                List<BannerEntity> images = new ArrayList<>();
                Observable.fromIterable(bannerItemBeans).forEach(bannerItemBean -> images.add(new BannerEntity("","",bannerItemBean.getImage())));
                bannerView.delayTime(5)
                    .build(images);
                break;
            case RecommendMultiItem.STREAMER:
                mAppComponent.imageLoader().loadImage(mContext,
                        ImageConfigImpl.builder()
                                .imageView(helper.getView(R.id.iv_streamer))
                                .url(itemBean.getCover())
                                .build());
                break;
            case RecommendMultiItem.ITEM_AV:
                setItemPaddingAndImage(helper, item, itemBean);
                helper.setText(R.id.tv_play, TextHandleUtil.handleCount2TenThousand(itemBean.getPlay()))
                        .setText(R.id.tv_danmaku, TextHandleUtil.handleCount2TenThousand(itemBean.getDanmaku()))
                        .setText(R.id.tv_duration, TextHandleUtil.handleDurationSecond(itemBean.getDuration()))
                        .setText(R.id.tv_title, itemBean.getTitle())
                        .setText(R.id.tv_tname_tag_name, itemBean.getTname() + "‧" + (itemBean.getTag() == null ? "" : itemBean.getTag().getTag_name()));

                break;
            case RecommendMultiItem.ITEM_AV_RCMD_REASON:
                setItemPaddingAndImage(helper, item, itemBean);
                helper.setText(R.id.tv_play, TextHandleUtil.handleCount2TenThousand(itemBean.getPlay()))
                        .setText(R.id.tv_danmaku, TextHandleUtil.handleCount2TenThousand(itemBean.getDanmaku()))
                        .setText(R.id.tv_duration, TextHandleUtil.handleDurationSecond(itemBean.getDuration()))
                        .setText(R.id.tv_title, itemBean.getTitle())
                        .setText(R.id.tv_rcmd_reason, itemBean.getRcmd_reason().getContent());

                break;
            case RecommendMultiItem.ITEM_BANGUMI:
                setItemPaddingAndImage(helper, item, itemBean);
                helper.setText(R.id.tv_play, TextHandleUtil.handleCount2TenThousand(itemBean.getPlay()))
                        .setText(R.id.tv_favorite, itemBean.getFavorite() + "")
                        .setText(R.id.tv_title, itemBean.getTitle())
                        .setText(R.id.tv_last_index, mContext.getResources().getString(R.string.recommend_home_update_to_last_index, itemBean.getLast_index()))
                        .setText(R.id.tv_badge, itemBean.getBadge());
                break;
            case RecommendMultiItem.ITEM_LOGIN:
                break;
            case RecommendMultiItem.ITEM_AD_WEB_S:
                setItemPaddingAndImage(helper, item, itemBean);
                helper.setText(R.id.tv_title, itemBean.getTitle())
                        .setText(R.id.tv_desc, itemBean.getDesc());
                break;
            case RecommendMultiItem.ITEM_ARTICLE_S:
                setItemPaddingAndImage(helper, item, itemBean);
                helper.setText(R.id.tv_play, TextHandleUtil.handleCount2TenThousand(itemBean.getPlay()))
                        .setText(R.id.tv_favorite, itemBean.getFavorite() + "")
                        .setText(R.id.tv_title, itemBean.getTitle())
                        .setText(R.id.tv_desc, itemBean.getDesc());
                break;
            case RecommendMultiItem.PRE_HERE_CLICK_TO_REFRESH:
                break;
        }
    }

    private void setItemPaddingAndImage(BaseViewHolder helper, RecommendMultiItem item, RecommendIndexData.DataBean itemBean) {
        int leftPadding = item.isOdd() ? ArmsUtils.dip2px(mContext, ConstantUtil.MAIN_HOME_ITEM_PADDING) : 0;
        int rightPadding = item.isOdd() ? 0 : ArmsUtils.dip2px(mContext, ConstantUtil.MAIN_HOME_ITEM_PADDING);
        helper.getView(R.id.fl_item).setPadding(leftPadding, 0, rightPadding, 0);
        mAppComponent.imageLoader().loadImage(mContext, ImageConfigImpl.builder()
                .imageView(helper.getView(R.id.iv_cover))
                .url(itemBean.getCover())
                .build());
    }
}
