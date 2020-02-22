package com.example.himalaya.fragments;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommandListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.presenters.RecommendPresenter;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback {

    private final String TAG = "RecommendFragment";
    private RecyclerView mRecommandRv;
    private RecommandListAdapter mRecommandListAdapter;
    private RecommendPresenter mRecommendPresenter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //view已经加载完成
        View mRootView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
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
//        //拿数据回来
//        getRecommandData();
        //获取到逻辑层对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先要设置通知接口的注册
        mRecommendPresenter.regiserViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        //返回view,给界面展示
        return mRootView;
    }


//    private void upRecommandUI(List<Album> albumList) {
//        //把数据设置给适配器并更新ui
//        mRecommandListAdapter.setData(albumList);
//    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //当我们获取到推荐内容的时候，这个方法就会被调用，（成功了）
        //数据回来后，更新ui
        mRecommandListAdapter.setData(result);
    }

    @Override
    public void onLoaderMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册，避免内存泄漏(此部分已经用不上了，但还是有运行持有，java的垃圾回收器回收不了，就溢出、泄漏了)
        if (mRecommendPresenter != null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }
}
