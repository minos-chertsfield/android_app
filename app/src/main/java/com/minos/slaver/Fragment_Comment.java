package com.minos.slaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * 评论模块
 */
public class Fragment_Comment extends Fragment {


    ListView listView;

    CommentAdapter adapter;

    private String page;    //保存当前页信息

    private Comment[] comments;      //声明评论数组
    private List<Comment> commentList = new ArrayList<>();    //初始化对象集合

    private FloatingActionButton trigger;   //浮动按钮

    private Handler handler = new Handler();   //定时刷新


    private Runnable runnable = new Runnable() {
        public void run () {
            listView.requestLayout();
            adapter.notifyDataSetChanged();
            loadComment(page);
            handler.postDelayed(this,5000);
        }
    };



    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            handler.removeCallbacks(runnable);
        } else {
            loadComment(page);
        }
    }









    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        return  view;
    }


    @Override
    public void onStart() {

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,5000);

        page = getActivity().getIntent().getStringExtra("page");      //获取传来的页面信息

        trigger = (FloatingActionButton) getView().findViewById(R.id.trigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        loadComment(page);    //加载列表
        listView = (ListView) getView().findViewById(R.id.comment_list);    //评论列表控件
        adapter = new CommentAdapter(getContext(), R.layout.comment_list_layout, commentList);
        listView.setAdapter(adapter);
        super.onStart();
    }


    /**
     * 弹出对话框
     */
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(getActivity());
        final AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getActivity());
        editText.setHint("说点什么吧……");
        inputDialog.setTitle("发表评论").setView(editText);
        inputDialog.setPositiveButton("发表",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = editText.getText().toString();
                        Gson gson = new Gson();    //创建Gson对象
                        User  user = gson.fromJson(getActivity().getSharedPreferences("user",getActivity().MODE_PRIVATE).getString("User",""), User.class);

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
                                Toast.makeText(getActivity(), "发表成功", Toast.LENGTH_SHORT).show();   //返回值为1，显示注册成功
                                //刷新




                            }
                            else{    //返回值为"fail"
                                Toast.makeText(getActivity(), "发表失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }).show();

    }


    /**
     *
     * @param page 页面信息
     */
    private void loadComment(String page) {    //加载购物车列表


        String encoding = "UTF-8";
        try {

            byte[] data = ("{\"page\":"+"\""+ page +"\"}").getBytes(encoding);

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
            if(response.equals("fail"))
            {
                Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();





            }
            else{    //返回列表
                System.out.println(response);     //服务器端应答返回对象列表的Json字符串

                Type type = new TypeToken<List<Comment>>(){}.getType();

                List<Comment> list = new ArrayList<Comment>();

                list = new Gson().fromJson(response,type);

                System.out.println("list:" + list.getClass());


                comments = list.toArray(new Comment[list.size()]);

                commentList.clear();


                for (int i=0;i<50;i++) {
                    commentList.add(comments[i]);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
