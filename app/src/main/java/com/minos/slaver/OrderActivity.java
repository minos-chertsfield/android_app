package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 订单页面
 */
public class OrderActivity extends BaseActivity {




    Commodity commodity;
    double money;    //用户的余额

    private String mac;
    private String username;
    private String address;
    private String cId;
    private String cName;
    private double price;
    private int num;
    private double sum;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        commodity = (Commodity) getIntent().getSerializableExtra("order");

        //商品属性获取
        cId = commodity.getcId();
        cName = commodity.getcName();
        price = commodity.getPrice();
        System.out.println("价格"+price);

        num = Integer.parseInt(getIntent().getStringExtra("num"));
        System.out.println("数量" + num);

        sum = price * num;    //计算总价

        mac = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User",""), User.class).getMac();
        username = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User",""), User.class).getUsername();
        //属性获取

        TextView order_username = (TextView) findViewById(R.id.order_username);
        order_username.setText(username);
        TextView order_address = (TextView) findViewById(R.id.order_address);
        TextView order_price = (TextView) findViewById(R.id.order_price);
        order_price.setText(String.valueOf(price + "元/份"));
        TextView order_num = (TextView) findViewById(R.id.order_num);
        order_num.setText("数量："+ String.valueOf(num));
        TextView order_sum = (TextView) findViewById(R.id.order_sum);
        order_sum.setText(String.valueOf("总价：" + sum));
        ImageView imageView = (ImageView) findViewById(R.id.order_img);
        Glide.with(this).load(commodity.getcImg()).into(imageView);

        Button commit_order = (Button) findViewById(R.id.commit_order);     //提交订单按钮
        commit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                money = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User",""), User.class).getMoney();
                if (money < sum){
                    Toast.makeText(OrderActivity.this, "余额不足，请充值", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    time = simpleDateFormat.format(date);    //获取当前时间

                    pay_now(mac, username, address, cId, cName, price, num, sum, time);

                }





            }
        });




    }



    private void pay_now(String mac,
                         String username,
                         String address,
                         String cId,
                         String cName,
                         double price,
                         int num,
                         double sum,
                         String time) {    //将数据上传至订单表


        Gson gson = new Gson();    //创建Gson对象

        Order order = new Order();
        order.setMac(mac);
        order.setUsername(username);
        order.setAddress(address);
        order.setcId(cId);
        order.setcName(cName);
        order.setPrice(price);
        order.setNum(num);
        order.setSum(sum);
        order.setTime(time);
        order.setoId(0);

        String json = gson.toJson(order);

        String encoding = "UTF-8";
        try {
            byte[] data = json.getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_pay);    //实例化为url对象, 请求加载页面，返回所有数据
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
                Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
                System.out.println(response);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("User", response);
                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this,DetailsActivity.class);
                intent.putExtra("commodity",cId);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
