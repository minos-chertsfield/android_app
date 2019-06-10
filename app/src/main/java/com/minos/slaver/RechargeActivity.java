package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 充值页面
 */
public class RechargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final TextView money = (TextView) findViewById(R.id.money);

        final ToggleButton choose25 = (ToggleButton) findViewById(R.id.choose25);
        final ToggleButton choose50 = (ToggleButton) findViewById(R.id.choose50);
        final ToggleButton choose100 = (ToggleButton) findViewById(R.id.choose100);
        final ToggleButton choose200 = (ToggleButton) findViewById(R.id.choose200);
        final ToggleButton choose500 = (ToggleButton) findViewById(R.id.choose500);
        final ToggleButton choose1000 = (ToggleButton) findViewById(R.id.chose1000);



        choose25.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充25", Toast.LENGTH_SHORT).show();
                    choose50.setChecked(false);
                    choose100.setChecked(false);
                    choose200.setChecked(false);
                    choose500.setChecked(false);
                    choose1000.setChecked(false);
                    money.setText("25");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        choose50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充50", Toast.LENGTH_SHORT).show();
                    choose25.setChecked(false);
                    choose100.setChecked(false);
                    choose200.setChecked(false);
                    choose500.setChecked(false);
                    choose1000.setChecked(false);
                    money.setText("50");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        choose100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充100", Toast.LENGTH_SHORT).show();
                    choose25.setChecked(false);
                    choose50.setChecked(false);
                    choose200.setChecked(false);
                    choose500.setChecked(false);
                    choose1000.setChecked(false);
                    money.setText("100");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        choose200.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充200", Toast.LENGTH_SHORT).show();
                    choose25.setChecked(false);
                    choose50.setChecked(false);
                    choose100.setChecked(false);
                    choose500.setChecked(false);
                    choose1000.setChecked(false);
                    money.setText("200");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        choose500.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充500", Toast.LENGTH_SHORT).show();
                    choose25.setChecked(false);
                    choose50.setChecked(false);
                    choose100.setChecked(false);
                    choose200.setChecked(false);
                    choose1000.setChecked(false);
                    money.setText("500");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        choose1000.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RechargeActivity.this, "充25", Toast.LENGTH_SHORT).show();
                    choose25.setChecked(false);
                    choose50.setChecked(false);
                    choose100.setChecked(false);
                    choose200.setChecked(false);
                    choose500.setChecked(false);
                    money.setText("1000");
                } else {
                    Toast.makeText(RechargeActivity.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button recharge = (Button) findViewById(R.id.recharge);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RechargeActivity.this, money.getText().toString(), Toast.LENGTH_SHORT).show();


                recharge(Double.parseDouble(money.getText().toString()));
                /**
                 * 将输入的充值金额更新至服务器数据，再更新回本地
                 */


            }
        });

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(RechargeActivity.this,HomeActivity.class);
                intent.putExtra("favorites","1");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






    private void recharge(double increment) {


        Gson gson = new Gson();    //创建Gson对象

        String mac;

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);
        mac = user.getMac();


        String encoding = "UTF-8";
        try {
            byte[] data = ("{\"money\":"+"\""+increment+"\""+",\"mac\":"+"\""+ mac +"\"}").getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_recharge);    //实例化为url对象, 请求充值服务，返回所有数据
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
                Toast.makeText(this, "充值失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表

                Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("User", response);
                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
