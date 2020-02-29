package com.example.himalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himalaya.R;
import com.example.himalaya.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommandListAdapter extends RecyclerView.Adapter<RecommandListAdapter.InnerHolder> {

    private List<Album> mData = new ArrayList<>();
    private String TAG = "RecommandListAdapter";
    private onRecommendClickListener mItemClickListener = null;

    //创建每一个item
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //找到view
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommand,viewGroup,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        //设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag());
                }
                LogUtil.d(TAG,"itemView click--------->" +position);
            }
        });
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        //返回显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList){
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);

        }
        //更新ui
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到各个控件，设置数据
            //专辑封面
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            //title
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            //专辑内容数量
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);

            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount()+"");
            albumContentCountTv.setText(album.getIncludeTrackCount()+"");

            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);

        }
    }

    //用于外部注册接口，找到程序
    public void setOnRecommendItemClickListener(onRecommendClickListener listener){
        this.mItemClickListener = listener;
    }

    //暴露接口，方便外部程序处理
    public interface onRecommendClickListener{
        void onItemClick(int position);
    }
}
