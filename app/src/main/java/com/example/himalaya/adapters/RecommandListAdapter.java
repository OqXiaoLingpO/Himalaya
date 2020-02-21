package com.example.himalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommandListAdapter extends RecyclerView.Adapter<RecommandListAdapter.InnerHolder> {

    private List<Album> mData = new ArrayList<>();

    //创建每一个item
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recommand,viewGroup,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder innerHolder, int i) {

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
    }
}
