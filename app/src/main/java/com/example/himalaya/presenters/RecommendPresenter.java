package com.example.himalaya.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.himalaya.adapters.RecommandListAdapter;
import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();

    //将主持人私有化
    private RecommendPresenter() {
    }

    private static RecommendPresenter sInstance = null;


    /**
     * 懒汉式单例
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取推荐内容，
     * 接口3.10.6
     */
    @Override
    public void getRecommendList() {
        //获取推荐内容
        //封装参数
        updateLoading();//加载中
        Map<String, String> map = new HashMap<>();
        //此接口表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");

        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {

            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //获取成功后应更新ui
                    //upRecommandUI(albumList);
                    handlerRecommendResult(albumList);
                }
            }

            private void handlerRecommendResult(List<Album> albumList) {
                //通知ui更新
                if (albumList != null) {
                    if (albumList.size()==0) {
                        for (IRecommendViewCallback callback : mCallbacks) {
                            callback.onEmpty();
                        }
                    }else{
                        for (IRecommendViewCallback callback : mCallbacks) {
                            callback.onRecommendListLoaded(albumList);
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                //失败
                Log.d(TAG, "errorCode ----=-=>" + i);
                Log.d(TAG, "errorMsg  ---=--->" + s);
                handlerError();
            }

            private void handlerError() {
                if (mCallbacks != null) {
                    for (IRecommendViewCallback callback : mCallbacks) {
                        callback.onMetworkError();
                    }
                }
            }
        });

    }
    private void updateLoading(){
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }


    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void regiserViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(mCallbacks);
        }
    }
}
