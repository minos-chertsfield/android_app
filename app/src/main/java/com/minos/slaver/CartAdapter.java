package com.minos.slaver;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 购物车列表的适配器
 */
public class CartAdapter extends ArrayAdapter<Cart> {

    private int resourceId;   //资源识别号

    public CartAdapter(Context context, int textViewResourceId, List<Cart> objects) {
        super(context, textViewResourceId, objects);
        resourceId  = textViewResourceId;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Cart cart = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
           view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
           viewHolder = new ViewHolder();
           viewHolder.cart_image = (ImageView) view.findViewById(R.id.cart_image);   //获取item中的图片
           viewHolder.cart_name = (TextView) view.findViewById(R.id.cart_name);    //获取item中的名称文本控件
           viewHolder.cart_price = (TextView) view.findViewById(R.id.cart_price);   //获取item中的价格文本控件
           viewHolder.cart_num = (TextView) view.findViewById(R.id.cart_num);    //获取item中的数量文本控件
           viewHolder.delete = (ImageView) view.findViewById(R.id.delete_from_cart);    //删除按钮
           viewHolder.choose = (CheckBox) view.findViewById(R.id.choose_cart);    //选中按钮
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder =(ViewHolder) view.getTag();
        }







        /**
         * 进行空间填充
         */
        Glide.with(getContext()).load(cart.getcImg()).into(viewHolder.cart_image);    //图片控件内容加载
        viewHolder.cart_name.setText(cart.getcName());    //填充商品的名称
        viewHolder.cart_price.setText(String.valueOf(cart.getPrice()) + "元/份");    //填充商品的价格
        viewHolder.cart_num.setText("数量："+ String.valueOf(cart.getNum()));      //填充商品的数量
        viewHolder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;     //返回组件
    }


    class ViewHolder {
        ImageView cart_image;
        TextView cart_name;
        TextView cart_price;
        TextView cart_num;
        ImageView delete;
        CheckBox choose;
    }
}
