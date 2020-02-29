package com.example.himalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.himalaya.DetaiActivity;
import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommandListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.presenters.RecommendPresenter;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener, RecommandListAdapter.onRecommendClickListener {

    private final String TAG = "RecommendFragment";
    private RecyclerView mRecommandRv;
    private RecommandListAdapter mRecommandListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;
    private View mRootView;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {

        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater,container);
            }
        };



//        //拿数据回来
//        getRecommandData();
        //获取到逻辑层对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先要设置通知接口的注册
        mRecommendPresenter.regiserViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        mUiLoader.setOnRetryClickListener(this);

        //返回view,给界面展示
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        //view已经加载完成
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
        //RecyclerView的使用
        //1、找到对应的空间
        mRecommandRv = mRootView.findViewById(R.id.recommand_list);
        //2、设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommandRv.setLayoutManager(linearLayoutManager);
        //给item设置分割线
        mRecommandRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),5);//特殊工具类，效果：px-->dp，dp-->px
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });
        //3、设置适配器
        mRecommandListAdapter = new RecommandListAdapter();
        mRecommandRv.setAdapter(mRecommandListAdapter);
        mRecommandListAdapter.setOnRecommendItemClickListener(this);
        return mRootView;
    }


//    private void upRecommandUI(List<Album> albumList) {
//        //把数据设置给适配器并更新ui
//        mRecommandListAdapter.setData(albumList);
//    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtil.d(TAG,"onRecommendListLoaded");
        //当我们获取到推荐内容的时候，这个方法就会被调用，（成功了）
        //数据回来后，更新ui
        mRecommandListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onMetworkError() {
        LogUtil.d(TAG,"onMetworkError");
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        LogUtil.d(TAG,"onEmpty");

        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG,"onLoading");

        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册，避免内存泄漏(此部分已经用不上了，但还是有运行持有，java的垃圾回收器回收不了，就溢出、泄漏了)
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        //表示网络不佳，用户点击了重试
        //重新获取数据即可
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position) {
        //item被点击了,跳转到详情界面
        Intent intent = new Intent(getContext(), DetaiActivity.class);
        startActivity(intent);
    }
}
