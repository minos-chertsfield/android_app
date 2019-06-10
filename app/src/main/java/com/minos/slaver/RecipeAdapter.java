package com.minos.slaver;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
菜谱功能加载所使用的适配器
 */
public class RecipeAdapter extends RecyclerView.Adapter<com.minos.slaver.RecipeAdapter.ViewHolder> {

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
    private com.minos.slaver.RecipeAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(com.minos.slaver.RecipeAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    private Context mContext;     //上下文对象

    private List<Recipe> mRecipeList;




    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;     //卡片视图对象
        ImageView recipeImage;       //菜谱封面
        TextView recipeName;       //菜谱名称
        TextView ownerName;       //作者姓名
        ImageView ownerHead;      //作者头像

        public ViewHolder(View view) {     //对应控件进行绑定
            super(view);
            cardView = (CardView) view;
            recipeImage = (ImageView) view.findViewById(R.id.recipe_image);     //实例化图片
            recipeName = (TextView) view.findViewById(R.id.recipe_name);    //实例化商品名称文本
            ownerName = (TextView) view.findViewById(R.id.owner_name);    //作者用户名实例化
            ownerHead = (ImageView) view.findViewById(R.id.owner_head);    //作者头像实例化
        }
    }


    public RecipeAdapter(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        getRandomHeight(mRecipeList);
    }

    @Override
    public com.minos.slaver.RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);
        return new com.minos.slaver.RecipeAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final com.minos.slaver.RecipeAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height =  heights.get(position);
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        Recipe recipe = mRecipeList.get(position);
        holder.recipeName.setText(recipe.getTitle());     //获取商品名称
        holder.ownerName.setText(recipe.getUsername());    //获取商品价格
//        byte[] bytes1 = Base64.decode(recipe.getCover(), Base64.DEFAULT);
//        byte[] bytes2 = Base64.decode(recipe.getHead_src(), Base64.DEFAULT);
//        Glide.with(mContext).load(BitmapFactory.decodeByteArray(bytes1,0,bytes1.length)).into(holder.recipeImage);    //图片加载
//        Glide.with(mContext).load(BitmapFactory.decodeByteArray(bytes2,0,bytes2.length)).into(holder.ownerHead);
        Glide.with(mContext).load(recipe.getHead_src()).into(holder.ownerHead);
        Glide.with(mContext).load(recipe.getCover()).into(holder.recipeImage);    //图片加载


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //利用定义方法获取点击位置
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    private void getRandomHeight(List<Recipe> lists){//得到随机item的高度
        heights = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {        //生成随机数
            heights.add((new Random().nextInt(250)+650));
        }
    }


    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }


}
