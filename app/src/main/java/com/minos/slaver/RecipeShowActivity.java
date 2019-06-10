package com.minos.slaver;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeShowActivity extends BaseActivity {
/*
菜谱加载页面
 */




    private Recipe[] recipes;      //声明菜谱的数组

    private List<Recipe> recipeList = new ArrayList<Recipe>();     //菜谱列表
    private RecipeAdapter adapter;   //适配器
    private SwipeRefreshLayout swipeRefresh;    //下拉刷新布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_show);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        myrefresh();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_recipe);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);   //交错布局管理器
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(recipeList);
        adapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {    //使用适配器绑定点击事件，进行跳转
                Toast.makeText(RecipeShowActivity.this, "click" + recipeList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecipeShowActivity.this, RecipeContentActivity.class);
                intent.putExtra("recipe", String.valueOf(recipeList.get(position).getRno()));
                startActivity(intent);

            }
        });


        recyclerView.setAdapter(adapter);



        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_recipe);
        swipeRefresh.setColorSchemeResources(R.color.colorOrange);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecipes();
            }
        });


    }







        private void refreshRecipes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myrefresh();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }




    //实现返回按钮功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(RecipeShowActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void myrefresh() {    //进行数据连接



        Gson gson = new Gson();    //创建Gson对象


        String encoding = "UTF-8";
        try {

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_recipe02);    //实例化为url对象, 请求加载页面，返回所有数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();    //使用Http协议打开对应的url
            conn.setRequestMethod("POST");   //以post的方式发送请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
            conn.setConnectTimeout(5 * 1000);   //设置连接超时时间
            OutputStream outStream = conn.getOutputStream();
            outStream.flush();
            outStream.close();
            System.out.println(conn.getResponseCode()); //响应代码 200表示成功




            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response =  reader.readLine();
            if(response.equals("fail"))   //返回值为"success"
            {
                Toast.makeText(RecipeShowActivity.this, "失败", Toast.LENGTH_SHORT).show();


            }
            else{    //返回列表
                System.out.println(response);     //服务器端应答返回对象列表的Json字符串

                Type type = new TypeToken<List<Recipe>>(){}.getType();

                List<Recipe> list = new ArrayList<Recipe>();

                list = new Gson().fromJson(response,type);

                System.out.println("list:" + list.getClass());


                recipes = list.toArray(new Recipe[list.size()]);

                recipeList.clear();


                for (int i=0;i<50;i++) {
                    Random random = new Random();
                    int index = random.nextInt(recipes.length);

                    recipeList.add(recipes[index]);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
