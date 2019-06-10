package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class RecipeContentActivity extends BaseActivity {
/*
菜谱的具体内容
 */
    WebView wvBookPlay;
    int rno;
    ImageView cover;     //封面
    TextView username;    //用户名
    ImageView head;   //用户头像

    private ImageButton btn_favorite;    //收藏按钮
    private ImageButton btn_share;   //分享功能
    private String time;      //收藏时间
    private String mac;     //获取用户设备硬件地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        rno = Integer.parseInt(getIntent().getStringExtra("recipe"));    //获取传来的菜谱编号


        System.out.println(getIntent().getStringExtra("recipe"));

        wvBookPlay = (WebView) findViewById(R.id.content);
        cover = (ImageView) findViewById(R.id.content_cover);
        head = (ImageView) findViewById(R.id.content_head);
        username = (TextView) findViewById(R.id.content_username);

        btn_favorite = (ImageButton) findViewById(R.id.recipe_favorite);
        btn_share = (ImageButton) findViewById(R.id.recipe_share);

        wvBookPlay.getSettings().setJavaScriptEnabled(true);
        wvBookPlay.getSettings().setUseWideViewPort(true);
        wvBookPlay.getSettings().setLoadWithOverviewMode(true);
        wvBookPlay.getSettings().setBuiltInZoomControls(true);
        wvBookPlay.getSettings().setAllowFileAccess(true);
        wvBookPlay.getSettings().setSupportZoom(true);
        wvBookPlay.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvBookPlay.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvBookPlay.getSettings().setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
        wvBookPlay.loadUrl("http://116.62.23.56:80/slaver_demo2/recipe_html/"+rno+".html");

//        myrefresh(rno);



        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                time = simpleDateFormat.format(date);    //获取当前时间

                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);    //从本地取出当前用户对象
                mac = user.getMac();    //获取硬件地址

                myFavorites(rno, mac, time);    //填写对应参数并调用方法连接服务器


            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // 比如发送文本形式的数据内容
                // 指定发送的内容
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://116.62.23.56:80/slaver_demo2/recipe_html/"+rno+".html");
                // 指定发送内容的类型
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "分享至"));
            }
        });


    }



//    private void myrefresh(int rno) {    //进行数据连接
//
//
//
//        Gson gson = new Gson();
//        //请求
//        String encoding = "UTF-8";
//        try {
//            byte[] data = ("{\"rno\":"+rno+"}").getBytes(encoding);
//
//            ConfigurationUrl cf = new ConfigurationUrl();
//            URL url = null;
//            url = new URL(cf.url_recipe03);     //普通用户
//            System.out.println(url);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();   //打开连接
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
//            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//            conn.setConnectTimeout(5 * 10000);
//            OutputStream outStream = conn.getOutputStream();
//            outStream.write(data);
//            outStream.flush();
//            System.out.println(conn.getResponseCode()); //响应代码 200表示成功
//
//            InputStream inputStream = conn.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String content =  reader.readLine();
//            System.out.println(content);
//            if(content.equals("fail"))
//            {
//                Toast.makeText(RecipeContentActivity.this, "数据加载失败！", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Recipe recipe = new Gson().fromJson(content, Recipe.class);
//
//                String content_html = new String(recipe.getRecipe());
//                System.out.println("html:" + content_html);
//                byte[] bytes = Base64.decode(recipe.getCover(), Base64.DEFAULT);
////                Glide.with(this).load(BitmapFactory.decodeByteArray(bytes,0,bytes.length)).into(cover);
//                cover.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
//                byte[] bytes_head = Base64.decode(recipe.getHead_src(), Base64.DEFAULT);
////                Glide.with(this).load(BitmapFactory.decodeByteArray(bytes_head,0,bytes.length)).into(head);
////                head.setImageBitmap(BitmapFactory.decodeByteArray(bytes_head,0,bytes.length));
//                username.setText(recipe.getUsername());
//                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.getSettings().setDomStorageEnabled(true);
//                webView.getSettings().setDefaultTextEncodingName("utf-8");
////                webView.loadDataWithBaseURL(null, content_html, "text/html", "utf-8", null);
//                webView.loadUrl("http://116.62.23.56/slaver_demo2/recipe_html/17.html");
//
//            }
//
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(RecipeContentActivity.this,RecipeShowActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     *
     * @param rno 菜谱编号
     * @param mac 当前用户的设备硬件地址
     * @param time 收藏时间
     */
    private void myFavorites(int rno, String mac, String time) {    //收藏当前页面内容

        String encoding = "UTF-8";
        try {
            byte[] data = ("{\"mac\":\""+mac+"\",\"rno\":\""+rno+"\",\"time\":\""+time+"\"}").getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.favorites01);    //创建收藏上传页面的url对象
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
            if(response.equals("fail"))
            {
                Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
