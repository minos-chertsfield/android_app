package com.minos.slaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity {


    private Handler handler = new Handler();   //定时刷新


    private Runnable runnable = new Runnable() {
        public void run () {
            content.setText("");
            myrefresh();
        handler.postDelayed(this,1000);
        }
    };




    SharedPreferences preferences = null;    //声明SharedPreferences的对象

    private String[] info;       //个人信息数组

    private FloatingActionButton trigger;   //浮动按钮

    private TextView content;

    private EditText editText = null;
    /**
     *
     */
    String page = "d"; //标记当前页面



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {   //页面加载时进行数据加载
        myrefresh();   //刷新方法
        super.onWindowFocusChanged(hasFocus);
    }

    @Override    //不可见
    protected void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,5000);



        final SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        content = (TextView) findViewById(R.id.show_content);

//        editText = (EditText)findViewById(R.id.say);     //编辑框
//        Button btn_comment = (Button) findViewById(R.id.btn_comment);

        trigger = (FloatingActionButton) findViewById(R.id.trigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });





//        btn_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str_comment = editText.getText().toString();    //获取评论的内容
//
//                Gson gson = new Gson();    //创建Gson对象
//                User  user = gson.fromJson(sharedPreferences.getString("User",""), User.class);
//
//                Comment comment = new Comment(0,user.getMac(),page,str_comment,user.getUsername(),user.getHead_src());
//                String json = gson.toJson(comment);   //将User类对象转化为Json类型的字符串
//                /**
//                 * 用作测试，打印Json字符串
//                 */
//                System.out.println(json);
//
//                /**
//                 *
//                 */
//                String encoding = "UTF-8";
//                try {
//                    byte[] data = json.getBytes(encoding);
//
//                    ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
//                    URL url = new URL(confurl.url_comment);    //实例化为url对象
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();    //使用Http协议打开对应的url
//                    conn.setRequestMethod("POST");   //以post的方式发送请求
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//                    conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
//                    conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//                    conn.setConnectTimeout(5 * 1000);   //设置连接超时时间
//                    OutputStream outStream = conn.getOutputStream();
//                    outStream.write(data);
//                    outStream.flush();
//                    outStream.close();
//                    System.out.println(conn.getResponseCode()); //响应代码 200表示成功
//
//
//
//
//                    InputStream inputStream = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String response =  reader.readLine();
//                    if(response.equals("success"))   //返回值为"success"
//                    {
//                        Toast.makeText(CommentActivity.this, "发表成功", Toast.LENGTH_SHORT).show();   //返回值为1，显示注册成功
//                        //刷新
//
//
//                        content.append("\n\n"+user.getUsername()+":"+str_comment);
//
//                    }
//                    else{    //返回值为"fail"
//                        Toast.makeText(CommentActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                editText.setText("");
//
//                myrefresh();
//                /**
//                 *
//                 */
//
//                /**
//                 *
//                 */


//            }
//        });



    }

    private void myrefresh() {
        Gson gson = new Gson();    //创建Gson对象
        User  user = gson.fromJson(getSharedPreferences("user",MODE_PRIVATE).getString("User",""), User.class);

        Comment comment = new Comment(0,user.getMac(),page,"123",user.getUsername(),user.getHead_src());     //上传当前页面信息
        String json = gson.toJson(comment);   //将User类对象转化为Json类型的字符串

        String encoding = "UTF-8";
        try {
            byte[] data = json.getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_loadall);    //实例化为url对象, 请求加载页面，返回所有数据
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
            if(response.equals("success"))   //返回值为"success"
            {
//                Toast.makeText(CommentActivity.this, "加载成功", Toast.LENGTH_SHORT).show();   //返回值为1，显示注册成功
                //刷新
                String mylist = reader.readLine();
                System.out.println(mylist);

                info=mylist.split("\\$");
                for (int i = 0; i < data.length; i++) {
                    content.append("\n\n"+info[i]);
                    System.out.println(info[i]);
                }


            }
            else{    //返回值为"fail"
                Toast.makeText(CommentActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 弹出对话框
     */
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(CommentActivity.this);
        final AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(CommentActivity.this);
        editText.setHint("说点什么吧……");
        inputDialog.setTitle("发表评论").setView(editText);
        inputDialog.setPositiveButton("发表",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String comment = editText.getText().toString();
                        Gson gson = new Gson();    //创建Gson对象
                        User  user = gson.fromJson(getSharedPreferences("user",MODE_PRIVATE).getString("User",""), User.class);

                        Comment comment_obj = new Comment(0,user.getMac(),page,comment,user.getUsername(),user.getHead_src());
                        String json = gson.toJson(comment_obj);   //将User类对象转化为Json类型的字符串
                        /**
                         * 用作测试，打印Json字符串
                         */
                        System.out.println(json);

                        /**
                         *
                         */
                        String encoding = "UTF-8";
                        try {
                            byte[] data = json.getBytes(encoding);

                            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
                            URL url = new URL(confurl.url_comment);    //实例化为url对象
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
                            if(response.equals("success"))   //返回值为"success"
                            {
                                Toast.makeText(CommentActivity.this, "发表成功", Toast.LENGTH_SHORT).show();   //返回值为1，显示注册成功
                                //刷新


                                content.append("\n\n"+user.getUsername()+":"+comment);

                            }
                            else{    //返回值为"fail"
                                Toast.makeText(CommentActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }).show();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(CommentActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
