package com.minos.slaver;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 商品适配器
 */

public class CommodityAdapter extends RecyclerView.Adapter<com.minos.slaver.CommodityAdapter.ViewHolder> {

    private List<Integer> heights;     //获取控件条目的高度
    /**
     * 内部接口，声明recyclerView的点击事件
     */
    public interface OnItemClickListener {      //内部接口，条目的点击事件
        void onItemClick(View view, int position);
    }

    /**
     * 设置相应点击方法的监听器和监听设置方法
     */
    private com.minos.slaver.CommodityAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(com.minos.slaver.CommodityAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    private Context mContext;     //上下文对象

    private List<Commodity> mCommodityList;




    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;     //卡片视图对象
        ImageView commodityImage;       //商品图片
        TextView commodityName;       //商品名称
        TextView price;       //商品价格

        public ViewHolder(View view) {     //对应控件进行绑定
            super(view);
            cardView = (CardView) view;
            commodityImage = (ImageView) view.findViewById(R.id.commodity_image);     //实例化图片
            commodityName = (TextView) view.findViewById(R.id.commodity_name);    //实例化商品名称文本
            price = (TextView) view.findViewById(R.id.price);    //商品价格文本实例化
        }
    }


    public CommodityAdapter(List<Commodity> commodityList) {
        mCommodityList = commodityList;
        getRandomHeight(mCommodityList);
    }

    @Override
    public com.minos.slaver.CommodityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.commodity_item, parent, false);
        return new com.minos.slaver.CommodityAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final com.minos.slaver.CommodityAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height =  heights.get(position);
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        Commodity commodity = mCommodityList.get(position);
        holder.commodityName.setText(commodity.getcName());     //获取商品名称
        holder.price.setText(String.valueOf(commodity.getPrice()) + "元/份");    //获取商品价格
        Glide.with(mContext).load(commodity.getcImg()).into(holder.commodityImage);    //图片加载

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //利用定义方法获取点击位置
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    private void getRandomHeight(List<Commodity> lists){//得到随机item的高度
        heights = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {        //生成随机数
            heights.add((new Random().nextInt(250)+650));
        }
    }


    @Override
    public int getItemCount() {
        return mCommodityList.size();
    }


}
