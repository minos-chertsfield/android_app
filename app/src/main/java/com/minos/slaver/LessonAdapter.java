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

import java.util.List;

/*
课程类适配器
 */
public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {


    /**
     *内部接口，声明recyclerView的点击事件
     */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    /**
     *设置相应点击方法的监听器和监听设置方法
     */
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }





    private Context mContext;

    private List<Lesson> mLessonList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView lessonImage;
        TextView lessonName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            lessonImage = (ImageView) view.findViewById(R.id.lesson_image);
            lessonName = (TextView) view.findViewById(R.id.lesson_name);
        }
    }

    public LessonAdapter(List<Lesson> fruitList) {
        mLessonList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.lesson_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Lesson lesson = mLessonList.get(position);
        holder.lessonName.setText(lesson.getLessonName());
        Glide.with(mContext).load(lesson.getLessonId()).into(holder.lessonImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    //利用定义方法获取点击位置
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLessonList.size();
    }
}
