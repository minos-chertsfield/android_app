package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

/**
 * 功能：用户的收藏界面
 */
public class FavoritesActivity extends BaseActivity {

    private Favorite[] favorites;      //声明收藏的数组

    private List<Favorite> favoriteList = new ArrayList<Favorite>();     //收藏类型的集合类
    private FavoriteAdapter adapter;   //声明适配器




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        myrefresh();    //将数据进行加载

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_favorite);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);   //网格布局管理器，设置为两列并排
        recyclerView.setLayoutManager(layoutManager);   //进行RecyclerView的布局管理
        adapter = new FavoriteAdapter(favoriteList);
        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {    //设置点击监听
            @Override
            public void onItemClick(View view, int position) {    //使用适配器绑定点击事件，进行跳转

                if (favoriteList.get(position).getRno()!=0) {     //当前类型为菜谱
                    Intent intent1 =  new Intent(FavoritesActivity.this, RecipeContentActivity.class);
                    intent1.putExtra("recipe", String.valueOf(favoriteList.get(position).getRno()));
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(FavoritesActivity.this, VideoActivity.class);
                    intent2.putExtra("id",favoriteList.get(position).getcLink());
                    startActivity(intent2);
                }
            }
        });





        recyclerView.setAdapter(adapter);







    }







    //实现返回按钮功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(FavoritesActivity.this,HomeActivity.class);
                intent.putExtra("favorites","1");   //返回设置页面
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void myrefresh() {    //进行数据连接

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);
        String mac = user.getMac();

        String encoding = "UTF-8";
        try {
            byte[] data = ("{\"mac\":\""+mac+"\"}").getBytes(encoding);
            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.favorites02);    //实例化为url对象, 请求加载页面，返回所有数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();    //使用Http协议打开对应的url
            conn.setRequestMethod("POST");   //以post的方式发送请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setConnectTimeout(5 * 1000);   //设置连接超时时间
            OutputStream outStream = conn.getOutputStream();
            outStream.write(data);
            outStream.flush();
            outStream.close();
            System.out.println(conn.getResponseCode()); //响应代码 200表示成功

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response =  reader.readLine();
            if(response.equals("fail"))    //失败
            {
                Toast.makeText(FavoritesActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }
            else{    //返回列表
                System.out.println(response);     //服务器端应答返回对象列表的Json字符串

                Type type = new TypeToken<List<Favorite>>(){}.getType();

                List<Favorite> list = new ArrayList<Favorite>();

                list = new Gson().fromJson(response,type);

                System.out.println("list:" + list.getClass());


                favorites = list.toArray(new Favorite[list.size()]);

                favoriteList.clear();


                for (int i=0;i<50;i++) {
                    int index = i;

                    favoriteList.add(favorites[index]);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
