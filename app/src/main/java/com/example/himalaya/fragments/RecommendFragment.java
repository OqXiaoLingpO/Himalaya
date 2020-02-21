package com.example.himalaya.fragments;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommandListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {

    private final String TAG = "RecommendFragment";
    private RecyclerView mRecommandRv;
    private RecommandListAdapter mRecommandListAdapter;

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
        //3、设置适配器
        mRecommandListAdapter = new RecommandListAdapter();
        mRecommandRv.setAdapter(mRecommandListAdapter);
        //拿数据回来
        getRecommandData();

        //返回view
        return mRootView;    }
/**
 * 获取推荐内容，
 * 接口3.10.6
 */

    private void getRecommandData() {
        //封装参数
        Map<String, String> map = new HashMap<>();
        //此接口表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, "3");

        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //获取成功后应更新ui
                    upRecommandUI(albumList);
                }
            }

            private void upRecommandUI(List<Album> albumList) {
                //把数据设置给适配器并更新ui
                mRecommandListAdapter.setData(albumList);
            }

            @Override
            public void onError(int i, String s) {
                //失败
                Log.d(TAG,"errorCode ----=-=>"+i);
                Log.d(TAG,"errorMsg  ---=--->"+s);
            }
        });
    }
}
