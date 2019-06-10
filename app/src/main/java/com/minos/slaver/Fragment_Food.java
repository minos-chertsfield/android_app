package com.minos.slaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Food extends Fragment implements ViewPager.OnPageChangeListener{
/*
功能：继承自Fragment类的碎片类，用于初始化和管理“美食页面”
 */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载碎片对应的布局文件，到预设的容器中
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        return view;
    }

    Activity mActivity;

    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private String[] contentDescs;
    private TextView tv_desc;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {    //重写碎片的启动方法，加载布局中的组件
        /*
        加载碎片逻辑
         */

        ImageView today_recommend = (ImageView) getView().findViewById(R.id.today_recommend);     //今日推荐图片
        Glide.with(getContext()).load("http://116.62.23.56:80/slaver_demo2/image/25.jpg").into(today_recommend);


        ImageButton shop = (ImageButton) getView().findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                startActivity(intent);
            }
        });


        ImageButton go = (ImageButton) getView().findViewById(R.id.btn_community);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FavoritesActivity.class);
                startActivity(intent);
            }
        });


//        Button work = (Button) getView().findViewById(R.id.recipe);
//        work.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),RecipeActivity.class);
//                startActivity(intent);
//            }
//        });

        ImageButton show = (ImageButton) getView().findViewById(R.id.recipe);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecipeShowActivity.class);
                startActivity(intent);
            }
        });

//        TextView textView = (TextView)getView().findViewById(R.id.text_c);
//        textView.setText("\"在服务器安装好操作系统后，正式启用之前，就应该完成各种补丁的安装。服务器的补丁安装方法与我们使用的XP系统类似，这里就不再赘述。\n" +
//                "\n" +
//                "做好了基本的补丁安装，更重要的就是设置可访问的端口了，通常服务器只需要开放提供Web服务的必需端口，其他不必要的端口都可以禁止。不过需要注意的是，千万不要把管理服务器的远程端口3389也禁止了。\n" +
//                "\n" +
//                "删除默认共享也是必须做的一项步骤，服务器开启共享后很有可能被病毒或黑客入侵，从而进一步提权或者删除文件，因此我们要尽量关闭文件共享。删除默认共享可以采用多种方式，例如使用net share c$ /delete命令，就可以把c盘的默认共享功能关闭。\n" +
//                "\n" +
//                "权限分配 防止病毒木马入侵\n" +
//                "\n" +
//                "好的服务器权限设置可以将危害减少到最低，如果每个IIS站点的权限设置都不同，黑客就很难通过旁注攻击等方式入侵整个服务器。这里就简单介绍一下权限设置的方法。\n" +
//                "\n" +
//                "在系统中权限是按照用户的方式来划分的，要管理用户，可以在服务器中依次打开“开始→程序→管理工具→计算机管理→本地用户和组”，就可以看到管理服务器中所有的系统用户和用户组了。\n" +
//                "\n" +
//                "在为服务器分区的时候需要把所有的硬盘都分为NTFS分区，然后就可以设置每个分区对每个用户或组开放的权限。方法是在需要设置权限的文件夹上点击右键，选择“属性→安全”，即可设置文件或文件夹的权限了。\"");

// 初始化布局 View视图
        initViews();

        // Model数据
        initData();

        // Controller 控制器
        initAdapter();

        // 开启轮询
        new Thread(){
            public void run() {
                isRunning = true;
                while(isRunning){
                    try {
                        Thread.sleep(5500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getActivity() == null)
                        return;
                    // 往下跳一位
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + viewPager.getCurrentItem());
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            };
        }.start();

        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initViews() {
        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);// 设置页面更新监听
//		viewPager.setOffscreenPageLimit(1);// 左右各保留几个对象
        ll_point_container = (LinearLayout) getView().findViewById(R.id.ll_point_container);

        tv_desc = (TextView) getView().findViewById(R.id.tv_desc);

    }
    private void initData() {
        // 初始化要显示的数据

        // 图片资源id数组
        imageResIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

        // 文本描述
        contentDescs = new String[]{
                "马卡龙",
                "麻婆豆腐",
                "叻沙",
                "北京烤鸭",
                "巴西烤肉"
        };

        // 初始化要展示的5个ImageView
        imageViewList = new ArrayList<ImageView>();

        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);

            // 加小圆点, 指示器
            pointView = new View(getContext());
            if (!pointView.isActivated()){

            }
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            layoutParams = new LinearLayout.LayoutParams(20, 20);
            if(i != 0)
                layoutParams.leftMargin = 20 ;

            // 设置默认所有都不可用
            pointView.setEnabled(false);
            ll_point_container.addView(pointView, layoutParams);
        }

    }
    private void initAdapter() {
        ll_point_container.getChildAt(0).setEnabled(true);
        tv_desc.setText(contentDescs[0]);
        previousSelectedPosition = 0;

        // 设置适配器
        viewPager.setAdapter(new MyAdapter());

        // 默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        viewPager.setCurrentItem(5000000); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
//			System.out.println("isViewFromObject: "+(view == object));
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem初始化: " + position);
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

//			newPosition = position % 5
            int newPosition = position % imageViewList.size();

            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常
        }

        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
            System.out.println("destroyItem销毁: " + position);
            container.removeView((View)object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 滚动时调用
    }
    @Override
    public void onPageSelected(int position) {
        // 新的条目被选中时调用
        System.out.println("onPageSelected: " + position);
        int newPosition = position % imageViewList.size();

        //设置文本
        tv_desc.setText(contentDescs[newPosition]);

//		for (int i = 0; i < ll_point_container.getChildCount(); i++) {
//			View childAt = ll_point_container.getChildAt(position);
//			childAt.setEnabled(position == i);
//		}
        // 把之前的禁用, 把最新的启用, 更新指示器
        ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);

        // 记录之前的位置
        previousSelectedPosition  = newPosition;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        // 滚动状态变化时调用
    }
}
