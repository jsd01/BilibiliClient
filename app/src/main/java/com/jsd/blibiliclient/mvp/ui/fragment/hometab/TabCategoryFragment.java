package com.jsd.blibiliclient.mvp.ui.fragment.hometab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.jsd.blibiliclient.di.component.DaggerTabCategoryComponent;
import com.jsd.blibiliclient.di.module.TabCategoryModule;
import com.jsd.blibiliclient.mvp.contract.TabCategoryContract;
import com.jsd.blibiliclient.mvp.presenter.TabCategoryPresenter;

import com.jsd.blibiliclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TabCategoryFragment extends BaseFragment<TabCategoryPresenter> implements TabCategoryContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String type;
    private MyRecyclerViewAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public static TabCategoryFragment newInstance(String s) {
        TabCategoryFragment fragment = new TabCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTabCategoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .tabCategoryModule(new TabCategoryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_category, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        type = getArguments().getString("type");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyRecyclerViewAdapter();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
        private int[] list = {1,2,3,4,5,6,7,8,9,0,10,1,1,1,12,12,123,123,123,123,123,123,123,};

        public MyRecyclerViewAdapter() {
        }

        @Override
        public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_use, parent, false);
            MyRecyclerViewAdapter.ViewHolder viewHolder = new MyRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mText.setText(list[position]+"");
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mText;

            ViewHolder(View itemView) {
                super(itemView);
                mText = itemView.findViewById(R.id.item_tx);
            }

        }
    }
}
