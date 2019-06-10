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

public class FavoriteAdapter extends RecyclerView.Adapter<com.minos.slaver.FavoriteAdapter.ViewHolder> {

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
    private com.minos.slaver.FavoriteAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(com.minos.slaver.FavoriteAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    private Context mContext;     //上下文对象

    private List<Favorite> mFavoriteList;




    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;     //卡片视图对象
        ImageView favoriteImage;       //菜谱封面
        TextView favoriteName;       //菜谱名称
        TextView ownerName;       //作者姓名
        ImageView ownerHead;      //作者头像

        public ViewHolder(View view) {     //对应控件进行绑定
            super(view);
            cardView = (CardView) view;
            favoriteImage = (ImageView) view.findViewById(R.id.favorite_image);     //实例化图片
//            favoriteName = (TextView) view.findViewById(R.id.favorite_name);    //实例化商品名称文本
//            ownerName = (TextView) view.findViewById(R.id.owner);    //作者用户名实例化
//            ownerHead = (ImageView) view.findViewById(R.id.owner_img);    //作者头像实例化
        }
    }


    public FavoriteAdapter(List<Favorite> favoriteList) {
        mFavoriteList = favoriteList;
//        getRandomHeight(mFavoriteList);
    }

    @Override
    public com.minos.slaver.FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item, parent, false);
        return new com.minos.slaver.FavoriteAdapter.ViewHolder(view);
    }


    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final com.minos.slaver.FavoriteAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
//        params.height =  heights.get(position);
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        Favorite favorite = mFavoriteList.get(position);

        /**
         * 根据返回的列表值讨论其作为菜谱或者课程,仅加载图片
         */
        if (favorite.getRno()!=0) {    //课程链接为空，则收藏类型为菜谱
            Glide.with(mContext).load("http://116.62.23.56:80/slaver_demo2/recipe_cover/"+favorite.getRno()+".png").into(holder.favoriteImage);   //加载菜谱封面
        } else {    //菜谱编号为0，收藏类型为课程
            String lesson_cover = favorite.getcLink().replace("mp4","png");
            lesson_cover = lesson_cover.replace("video","image");
            System.out.println(lesson_cover);
            Glide.with(mContext).load(lesson_cover).into(holder.favoriteImage);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //利用定义方法获取点击位置
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

//    private void getRandomHeight(List<Favorite> lists){//得到随机item的高度
//        heights = new ArrayList<>();
//        for (int i = 0; i < lists.size(); i++) {        //生成随机数
//            heights.add((new Random().nextInt(250)+650));
//        }
//    }


    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }


}