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

public class FoodAdapter extends RecyclerView.Adapter<com.minos.slaver.FoodAdapter.ViewHolder> {


    private List<Integer> heights;
    /**
     * 内部接口，声明recyclerView的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 设置相应点击方法的监听器和监听设置方法
     */
    private com.minos.slaver.FoodAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(com.minos.slaver.FoodAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    private Context mContext;

    private List<Food> mFoodList;

    /**
     *
     */
//    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//
//        private int spanCount;
//        private int space;
//        private boolean includeEdge;
//
//
//        public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//
//            private int spanCount;
//            private int space;
//            private boolean includeEdge;
//
//
//            public SpaceItemDecoration(int spanCount, int space) {
//                this.spanCount = spanCount;
//                this.space = space;
//
//            }
//
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = parent.getChildAdapterPosition(view); // item position
//                outRect.left = space;
//                outRect.right = space;
//                if(position!=0 && position!=1){
//                    outRect.top = 2*space;
//                }else{
//                    outRect.top = space;
//                }
//
//
//            }
//        }

    /**
     *
     */


        static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            foodImage = (ImageView) view.findViewById(R.id.food_image);
            foodName = (TextView) view.findViewById(R.id.food_name);
        }
    }

    public FoodAdapter(List<Food> fruitList) {
        mFoodList = fruitList;
        getRandomHeight(mFoodList);
    }

    @Override
    public com.minos.slaver.FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_item, parent, false);
        return new com.minos.slaver.FoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.minos.slaver.FoodAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height =  heights.get(position);
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        Food food = mFoodList.get(position);
        holder.foodName.setText(food.getFoodName());
        Glide.with(mContext).load(food.getFoodId()).into(holder.foodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //利用定义方法获取点击位置
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }



    private void getRandomHeight(List<Food> lists){//得到随机item的高度
        heights = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {        //生成随机数
            heights.add((new Random().nextInt(250)+600));
        }
    }



    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}



