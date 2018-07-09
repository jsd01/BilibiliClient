package com.jsd.blibiliclient.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.jsd.blibiliclient.R;
import com.jsd.blibiliclient.app.widget.CircleImageView;
import com.jsd.blibiliclient.app.widget.banner.BannerEntity;
import com.jsd.blibiliclient.app.widget.banner.BannerView;
import com.jsd.blibiliclient.app.data.entity.live.LiveAppIndexInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * 项目名称：BlibiliClient
 * 类描述：
 * 创建人：贾少东
 * 创建时间：2018-06-30 11:13
 * 修改人：jsd
 * 修改时间：2018-06-30 11:13
 * 修改备注：直播页适配器
 */
public class HomeLiveAdapter extends RecyclerView.Adapter{

    private final Context context;
    private AppComponent mAppComponent;
    private LiveAppIndexInfo mLiveAppIndexInfo;
    private int entranceSize;

    private List<BannerEntity> bannerEntitys = new ArrayList<>();
    private List<Integer> liveSizes = new ArrayList<>();

    //直播分类入口
    private static final int TYPE_LIVE_ENTRANCE = 0;
    //直播Item
    private static final int TYPE_LIVE_ITEM = 1;
    //直播分类Title
    private static final int TYPE_LIVE_PARTITION = 2;
    //直播页Banner
    private static final int TYPE_LIVE_BANNER = 3;

    private int[] entranceIconRes = new int[]{
            R.drawable.live_home_follow_anchor,
            R.drawable.live_home_live_center,
            R.drawable.live_home_search_room,
            R.drawable.live_home_all_category
    };
    private String[] entranceTitles = new String[]{
            "关注主播", "直播中心",
            "搜索直播", "全部分类"
    };

    public HomeLiveAdapter(Context context) {
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_LIVE_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_banner, null);
                return new LiveBannerViewHolder(view);
            case TYPE_LIVE_ENTRANCE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_entrance, null);
                return new LiveEntranceViewHolder(view);
            case TYPE_LIVE_PARTITION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_partition_title, null);
                return new LivePartitionViewHolder(view);
            case TYPE_LIVE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_partition, null);
                return new LiveItemViewHolder(view);
        }
        return null;
    }

    public void setLiveInfo(LiveAppIndexInfo mLiveAppIndexInfo){
        this.mLiveAppIndexInfo = mLiveAppIndexInfo;
        entranceSize = 4;
        liveSizes.clear();
        bannerEntitys.clear();
        int tempSize = 0;
        int partitionSize = mLiveAppIndexInfo.getData().getPartitions().size();

        List<LiveAppIndexInfo.DataBean.BannerBean> banner = mLiveAppIndexInfo.getData().getBanner();
        Observable.fromIterable(banner).forEach(bannerBean -> bannerEntitys.add(new BannerEntity(
                bannerBean.getLink(), bannerBean.getTitle(), bannerBean.getImg())));

        for (int i = 0; i < partitionSize; i++) {
            liveSizes.add(tempSize);
            tempSize += mLiveAppIndexInfo.getData().getPartitions().get(i).getLives().size();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        position -= 1;
        final LiveAppIndexInfo.DataBean.PartitionsBean.LivesBean livesBean;
        if (holder instanceof LiveEntranceViewHolder) {

            LiveEntranceViewHolder liveEntranceViewHolder = (LiveEntranceViewHolder) holder;
            liveEntranceViewHolder.title.setText(entranceTitles[position]);
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(entranceIconRes[position])
                    .apply(options)
                    .into(((LiveEntranceViewHolder) holder).image);
        } else if (holder instanceof LiveItemViewHolder) {

            LiveItemViewHolder liveItemViewHolder = (LiveItemViewHolder) holder;

            livesBean = mLiveAppIndexInfo.getData().getPartitions().get(getItemPosition(position))
                    .getLives().get(position - 1 - entranceSize - getItemPosition(position) * 5);
            mAppComponent.imageLoader()
                    .loadImage(context, ImageConfigImpl.builder()
                    .url((livesBean.getCover().getSrc()))
                    .imageView(liveItemViewHolder.itemLiveCover)
                    .build());
            mAppComponent.imageLoader()
                    .loadImage(context, ImageConfigImpl.builder()
                            .url((livesBean.getOwner().getFace()))
                            .imageView(liveItemViewHolder.itemLiveUserCover)
                            .build());

            liveItemViewHolder.itemLiveTitle.setText(livesBean.getTitle());
            liveItemViewHolder.itemLiveUser.setText(livesBean.getOwner().getName());
            liveItemViewHolder.itemLiveCount.setText(String.valueOf(livesBean.getOnline()));
            /*liveItemViewHolder.itemLiveLayout.setOnClickListener(v -> LivePlayerActivity.
                    launch((Activity) context, livesBean.getRoom_id(),
                            livesBean.getTitle(), livesBean.getOnline(), livesBean.getOwner().getFace(),
                            livesBean.getOwner().getName(), livesBean.getOwner().getMid()));*/
        } else if (holder instanceof LivePartitionViewHolder) {

            LivePartitionViewHolder livePartitionViewHolder = (LivePartitionViewHolder) holder;
            LiveAppIndexInfo.DataBean.PartitionsBean.PartitionBean partition = mLiveAppIndexInfo.
                    getData().getPartitions().get(getItemPosition(position)).getPartition();
            mAppComponent.imageLoader()
                    .loadImage(context, ImageConfigImpl.builder()
                            .url(partition.getSub_icon().getSrc())
                            .imageView(livePartitionViewHolder.itemIcon)
                            .build());
            livePartitionViewHolder.itemTitle.setText(partition.getName());
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
                    "当前" + partition.getCount() + "个直播");
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                    context.getResources().getColor(R.color.colorPrimary));
            stringBuilder.setSpan(foregroundColorSpan, 2,
                    stringBuilder.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            livePartitionViewHolder.itemCount.setText(stringBuilder);
        } else if (holder instanceof LiveBannerViewHolder) {
            LiveBannerViewHolder liveBannerViewHolder = (LiveBannerViewHolder) holder;
            liveBannerViewHolder.mBannerView
                    .delayTime(5)
                    .build(bannerEntitys);

        }
    }

    @Override
    public int getItemCount() {
        if (mLiveAppIndexInfo != null) {
            return 1 + entranceIconRes.length
                    + mLiveAppIndexInfo.getData().getPartitions().size() * 5;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_LIVE_BANNER;
        }
        position -= 1;
        if (position < entranceSize){
            return TYPE_LIVE_ENTRANCE;
        }else if(isPartitionTitle(position)){
            return TYPE_LIVE_PARTITION;
        }else{
            return TYPE_LIVE_ITEM;
        }
    }

    /**
     * 获取当前Item在第几组中
     */
    private int getItemPosition(int pos) {
        pos -= entranceSize;
        return pos / 5;
    }

    private boolean isPartitionTitle(int position) {
        position -= entranceSize;//等于去除banner与入口数剩余item总数
        return (position % 5 == 0);
    }

    public int getSpanSize(int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_LIVE_ENTRANCE:
                return 3;
            case TYPE_LIVE_ITEM:
                return 6;
            case TYPE_LIVE_PARTITION:
                return 12;
            case TYPE_LIVE_BANNER:
                return 12;
        }
        return 0;
    }

    /*广告栏*/
    public class LiveBannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_live_banner_view)
        BannerView mBannerView;

        public LiveBannerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public int getEntranceSize() {
            return entranceSize;
        }
    }
    /*直播入口*/
    public class LiveEntranceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.live_entrance_title)
        public TextView title;
        @BindView(R.id.live_entrance_image)
        public ImageView image;

        public LiveEntranceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    /*直播分类*/
    public class LivePartitionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_live_partition_icon)
        ImageView itemIcon;
        @BindView(R.id.item_live_partition_title)
        TextView itemTitle;
        @BindView(R.id.item_live_partition_count)
        TextView itemCount;

        public LivePartitionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    /*直播item*/
    public class LiveItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_live_cover)
        ImageView itemLiveCover;
        @BindView(R.id.item_live_user)
        TextView itemLiveUser;
        @BindView(R.id.item_live_title)
        TextView itemLiveTitle;
        @BindView(R.id.item_live_user_cover)
        CircleImageView itemLiveUserCover;
        @BindView(R.id.item_live_count)
        TextView itemLiveCount;
        @BindView(R.id.item_live_layout)
        CardView itemLiveLayout;

        public LiveItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
