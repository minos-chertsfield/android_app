package com.minos.slaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Fragment_Collection extends Fragment {
/*
功能：继承自Fragment类的收藏活动碎片类，对相应的组件功能进行初始化和管理
 */

    private DrawerLayout mDrawerLayout;

    private Food[] foods = {new Food("果味奶油蛋糕\n20元/份",R.drawable.food2), new Food("精致糕点\n15元/份", R.drawable.food3),
            new Food("巧克力泡芙\n30元/份" ,R.drawable.food4), new Food("新疆大盘鸡\n80元/份", R.drawable.food6),new Food("宜宾燃面\n12元/份", R.drawable.food8),
            new Food("黄桥烧饼\n25元/份", R.drawable.food9), new Food("丁骨牛排\n45元/份", R.drawable.food10), new Food("椒盐羊排\n50元/份", R.drawable.food12)};

    private List<Food> foodList = new ArrayList<>();     //课程列表
    private FoodAdapter adapter;   //适配器
    private SwipeRefreshLayout swipeRefresh;    //下拉刷新布局


@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_collection, container, false);
    return  view;
}

    @Override
    public void onStart() {
     /*
            添加组件功能逻辑




         */



        initLessons();

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_collection);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);   //交错布局管理器
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FoodAdapter(foodList);
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {    //使用适配器绑定点击事件，进行跳转
                Toast.makeText(getActivity(), "click" + foodList.get(position).getFoodName(), Toast.LENGTH_SHORT).show();
                switch(foodList.get(position).getFoodName()) {      //跳转至详情页面
                    case "巧克力泡芙":
                        Intent intent1 = new Intent(getActivity(), DetailsActivity.class);
                        intent1.putExtra("c", "1");
                        startActivity(intent1);
                        break;
                    case "佩罗娜":
                        Intent intent2 = new Intent(getActivity(), DetailsActivity.class);
                        intent2.putExtra("id", "2");
                        startActivity(intent2);
                        break;
                    case "卡兹":
                        Intent intent3 = new Intent(getActivity(), DetailsActivity.class);
                        intent3.putExtra("id", "3");
                        startActivity(intent3);
                        break;
                    case "更木剑八":
                        Intent intent4 = new Intent(getActivity(), DetailsActivity.class);
                        intent4.putExtra("id", "4");
                        startActivity(intent4);
                        break;
                    case "托雷斯":
                        Intent intent5 = new Intent(getActivity(), DetailsActivity.class);
                        intent5.putExtra("id", "5");
                        startActivity(intent5);
                        break;
                    case "市丸银":
                        Intent intent6 = new Intent(getActivity(), DetailsActivity.class);
                        intent6.putExtra("id", "6");
                        startActivity(intent6);
                        break;
                }
            }
        });


        recyclerView.setAdapter(adapter);


        swipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_collection);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLessons();
            }
        });

        super.onStart();
    }



    private void refreshLessons() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initLessons();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }



    /*
    课程初始化
     */
    private void initLessons() {
        foodList.clear();
        for (int i=0;i<50;i++) {
            Random random = new Random();
            int index = random.nextInt(foods.length);
            foodList.add(foods[index]);
        }
    }

/**
 * 使用TextView进行随机创建瀑布流
 */
//    private String getRandomLengthName(String name) {
//            Random random = new Random();
//            int length = random.nextInt(20) + 1;
//            StringBuilder builder = new StringBuilder();
//            for (int i = 0; i < length; i++) {
//                builder.append(name);
//            }
//            return builder.toString();
//    }


}
