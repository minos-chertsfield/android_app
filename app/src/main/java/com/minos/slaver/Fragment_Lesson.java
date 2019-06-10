package com.minos.slaver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fragment_Lesson extends Fragment {
/*
功能：继承自Fragment类的课程碎片类，对课程活动的碎片进行初始化和管理
 */
    private DrawerLayout mDrawerLayout;

    private Lesson[] lessons = {new Lesson("扇贝蒸粉丝",R.drawable.food88), new Lesson("干煎土豆鸡丁", R.drawable.food90),
            new Lesson("早餐饼" ,R.drawable.food84), new Lesson("芒果慕斯", R.drawable.food86),new Lesson("草莓牛奶芝士熔岩", R.drawable.food89),
            new Lesson("珍珠燕麦奶茶", R.drawable.food87), new Lesson("尖椒杏鲍菇", R.drawable.food83), new Lesson("奶酪蛋糕卷", R.drawable.food85)};

    private List<Lesson> lessonList = new ArrayList<>();     //课程列表
    private LessonAdapter adapter;   //适配器
    private SwipeRefreshLayout swipeRefresh;    //下拉刷新布局



@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_lesson, container, false);
    return  view;
}

    @Override
    public void onStart() {
        /*
            添加组件功能逻辑


         */


        initLessons();

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_lesson);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LessonAdapter(lessonList);
        adapter.setOnItemClickListener(new LessonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {    //使用适配器绑定点击事件，进行跳转
//                Toast.makeText(getActivity(), "click" + lessonList.get(position).getLessonName(), Toast.LENGTH_SHORT).show();
                switch(lessonList.get(position).getLessonName()) {
                    case "草莓牛奶芝士熔岩":
                        Intent intent1 = new Intent(getActivity(), VideoActivity.class);
                        intent1.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/89.mp4");
                        intent1.putExtra("page","lesson1");     //页面编号
                        intent1.putExtra("title","草莓牛奶芝士熔岩");
                        startActivity(intent1);
                        break;
                    case "干煎土豆鸡丁":
                        Intent intent2 = new Intent(getActivity(), VideoActivity.class);
                        intent2.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/90.mp4");
                        intent2.putExtra("page","lesson2");     //页面编号
                        intent2.putExtra("title","干煎土豆鸡丁");
                        startActivity(intent2);
                        break;
                    case "芒果慕斯":
                        Intent intent3 = new Intent(getActivity(), VideoActivity.class);
                        intent3.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/86.mp4");
                        intent3.putExtra("page","lesson3");     //页面编号
                        intent3.putExtra("title","芒果慕斯");
                        startActivity(intent3);
                        break;
                    case "珍珠燕麦奶茶":
                        Intent intent4 = new Intent(getActivity(), VideoActivity.class);
                        intent4.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/87.mp4");
                        intent4.putExtra("page","lesson4");     //页面编号
                        intent4.putExtra("title","珍珠燕麦奶茶");
                        startActivity(intent4);
                        break;
                    case "奶酪蛋糕卷":
                        Intent intent5 = new Intent(getActivity(), VideoActivity.class);
                        intent5.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/85.mp4");
                        intent5.putExtra("page","lesson5");     //页面编号
                        intent5.putExtra("title","奶酪蛋糕卷");
                        startActivity(intent5);
                        break;
                    case "扇贝蒸粉丝":
                        Intent intent6 = new Intent(getActivity(), VideoActivity.class);
                        intent6.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/88.mp4");
                        intent6.putExtra("page","lesson6");     //页面编号
                        intent6.putExtra("title","扇贝蒸粉丝");
                        startActivity(intent6);
                        break;
                    case "早餐饼":
                        Intent intent7 = new Intent(getActivity(), VideoActivity.class);
                        intent7.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/84.mp4");
                        intent7.putExtra("page","lesson7");     //页面编号
                        intent7.putExtra("title","早餐饼");
                        startActivity(intent7);
                        break;
                    case "尖椒杏鲍菇":
                        Intent intent8 = new Intent(getActivity(), VideoActivity.class);
                        intent8.putExtra("id", "http://116.62.23.56:80/slaver_demo2/video/83.mp4");
                        intent8.putExtra("page","lesson7");     //页面编号
                        intent8.putExtra("title","尖椒杏鲍菇");
                        startActivity(intent8);
                        break;
                }
            }
        });


        recyclerView.setAdapter(adapter);


        swipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_lesson);
        swipeRefresh.setColorSchemeResources(R.color.colorOrange);
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
        lessonList.clear();
        for (int i=0;i<50;i++) {
            Random random = new Random();
            int index = random.nextInt(lessons.length);
            lessonList.add(lessons[index]);
        }
    }




}
