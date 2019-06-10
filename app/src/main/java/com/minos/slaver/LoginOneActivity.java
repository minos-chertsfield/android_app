package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginOneActivity extends BaseActivity {
/*
功能：简洁起见，登陆的第一个附属页面
 */

    SharedPreferences preferences = null;   //本地存储先置空


    String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_one);

/**
 * 采用严格模式
 */
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //获取用户名与密码输入框中的内容
        final EditText editText_username = (EditText) findViewById(R.id.account);
        final EditText editText_password = (EditText) findViewById(R.id.password);


        String username = preferences.getString("username","");   //
        String password = preferences.getString("password","");   //
        editText_username.setText(username);    //填写用户名
        editText_password.setText(password);    //填写密码


        Button btn_next = (Button) findViewById(R.id.btn_account_next);
        btn_next.setOnClickListener(new View.OnClickListener() {   //登录按钮监听
            @Override
            public void onClick(View v) {

                Mac mac_obj = new Mac();    //建立手机硬件地址对象
                /*
                 * 用字符串对于用户的输入文本进行获取
                 */
                String username = editText_username.getText().toString();
                String password = editText_password.getText().toString();


//==========================================
                SharedPreferences.Editor editor_remember = preferences.edit();


                    editor_remember.putString("username", username);
                    editor_remember.putString("password", password);
                    editor_remember.apply();
///==============================================================================
                /*
                将用户输入的用户名以及密码写成Json格式
                 */
                /**
                 * 进行服务器请求
                 */
                Gson gson = new Gson();
                Mac mac = new Mac();
                String json_login = null;    // 登录请求
                json_login = ("{\"username\":"+"\""+username+"\""+",\"password\":"+"\""+password+"\""+",\"mac\":"+"\""+mac.GenerateRandom(preferences)+"\"}"+"\n");
                System.out.println(json_login);
                //请求
                String encoding = "UTF-8";
                try {
                    byte[] data = json_login.getBytes(encoding);

                    ConfigurationUrl cf = new ConfigurationUrl();
                    URL url = null;
                    url = new URL(cf.url_login);     //普通用户
                    System.out.println(url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();   //打开连接
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
                    conn.setRequestProperty("Content-Length", String.valueOf(data.length));
                    conn.setConnectTimeout(5 * 10000);
                    OutputStream outStream = conn.getOutputStream();
                    outStream.write(data);
                    outStream.flush();
                    System.out.println(conn.getResponseCode()); //响应代码 200表示成功

                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String string =  reader.readLine();
                    System.out.println(string);
                    if(string.equals("0"))
                    {
                        Toast.makeText(LoginOneActivity.this, "失败，用户不存在！", Toast.LENGTH_SHORT).show();
                    }
                    else if(string.equals("success")){

//                        Toast.makeText(LoginOneActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        String user_json = reader.readLine();
                        System.out.println(user_json);


                        /**
                         *
                         */


                        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("User", user_json);
                        editor.commit();
                        test = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User", ""), User.class).getMobilephone();

                        System.out.println(test);
                        Toast.makeText(LoginOneActivity.this, "欢迎登陆,"+"用户"+new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User", ""), User.class).getUsername(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginOneActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else if(string.equals("fail")) {
                        Toast.makeText(LoginOneActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginOneActivity.this, "验证码已发送，请查阅短信！", Toast.LENGTH_SHORT).show();
                    }
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }




//                Intent intent = new Intent(LoginOneActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    //实现返回按钮功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(LoginOneActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
