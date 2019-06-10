package com.minos.slaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {


    private int resourceId;   //资源识别号

    public CommentAdapter(Context context, int textViewResourceId, List<Comment> objects) {
        super(context, textViewResourceId, objects);
        resourceId  = textViewResourceId;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);
        View view;
        CommentAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new CommentAdapter.ViewHolder();
            viewHolder.comment_head = (CircleImageView) view.findViewById(R.id.comment_head);
            viewHolder.comment_username = (TextView) view.findViewById(R.id.comment_username);
            viewHolder.comment_content = (TextView) view.findViewById(R.id.comment_content);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder =(CommentAdapter.ViewHolder) view.getTag();
        }







        /**
         * 进行控件填充
         */
        Glide.with(getContext()).load(comment.getHead_src()).into(viewHolder.comment_head);    //将用户的头像填充进去
        viewHolder.comment_username.setText(comment.getUsername());      //填充用户的用户名
        viewHolder.comment_content.setText(comment.getComment());       //天聪航用户的评论内容

        return view;     //返回组件
    }



    class ViewHolder {
        CircleImageView comment_head;      //用户头像
        TextView comment_username;     //用户名
        TextView comment_content;     //用户的评论内容
    }
}
