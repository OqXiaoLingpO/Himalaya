package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 用于通知view层
 */
public interface IRecommendViewCallback {
    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 加载更多
     * @param
     */
    void onLoaderMore(List<Album> result);

    /**
     * 下接加载更多
     * @param result
     */
    void onRefreshMore(List<Album> result);
}
