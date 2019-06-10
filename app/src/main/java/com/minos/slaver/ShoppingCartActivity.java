package com.minos.slaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.Stack;

public class ShoppingCartActivity extends BaseActivity {
    /**
     * 购物车页面
     * @param savedInstanceState
     */
    User  user;

    Button clear_cart;

    String username;

    Button count;

    String time;

    double show = 0.0;

    TextView count_all;

    double price;

    Stack<Double> stack = new Stack<Double>();      //储存价钱

    private Cart[] carts;      //声明商品的数组
    private List<Cart> cartList = new ArrayList<>();    //初始化对象集合
    String mac;   //用以获取用户当前设备硬件地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);





        count_all = (TextView) findViewById(R.id.cart_sum);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout_cart, null);
        actionBar.setCustomView(actionbarLayout);


        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        Gson gson = new Gson();
        user = gson.fromJson(sharedPreferences.getString("User",""), User.class);

        mac = user.getMac();
        username = user.getUsername();

        myrefresh(mac);

        final ListView listView = (ListView) findViewById(R.id.cart_list);    //购物车的列表控件
        final CartAdapter adapter = new CartAdapter(ShoppingCartActivity.this, R.layout.cart_list_layout, cartList);
        listView.setAdapter(adapter);

        clear_cart = (Button) findViewById(R.id.clear_cart);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cart mycart = cartList.get(position);
                price = mycart.getPrice() * mycart.getNum();


//                if ((Drawable)view.getBackground()).getColor()==Color.parseColor("#FFFF00")){     //如果被选中，减
//                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                    show -= price;
//                } else {             //选中
//                    view.setBackgroundColor(Color.parseColor("#FFFF00"));
//
//
//                    stack.push(price);
//
//                }

                stack.push(price);

                while(!stack.empty()){
                    show += stack.pop();
                }
                count_all.setText("总价："+String.valueOf(show)+"元");
//                Toast.makeText(ShoppingCartActivity.this, ""+price +"元" , Toast.LENGTH_SHORT).show();
//                String cId = mycart.getcId();
//                Intent intent = new Intent(ShoppingCartActivity.this, DetailsActivity.class);
//                intent.putExtra("commodity", cId);
//                startActivity(intent);
            }
        });


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingCartActivity.this,HomeActivity.class);
                intent.putExtra("favorites","1");
                startActivity(intent);
                finish();
            }
        });




        count = (Button) findViewById(R.id.count_price);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                time = simpleDateFormat.format(date);    //获取当前时间
                if (user.getMoney() >= show) {
                    pay_now(mac,username,"","","",show,1,show,time);
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "余额不足，请充值！", Toast.LENGTH_SHORT).show();
                }

            }
        });



        clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = 0.0;
                count_all.setText("总价："+String.valueOf(show)+"元");
            }
        });

    }








    private void myrefresh(String mac) {    //加载购物车列表




        Gson gson = new Gson();    //创建Gson对象


        String encoding = "UTF-8";
        try {

            byte[] data = ("{\"mac\":"+"\""+ mac +"\"}").getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_cart02);    //实例化为url对象, 请求加载页面，返回所有数据
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
                System.out.println(response);     //服务器端应答返回对象列表的Json字符串

                Type type = new TypeToken<List<Cart>>(){}.getType();

                List<Cart> list = new ArrayList<Cart>();

                list = new Gson().fromJson(response,type);

                System.out.println("list:" + list.getClass());


                carts = list.toArray(new Cart[list.size()]);

                cartList.clear();


                for (int i=0;i<50;i++) {
                    cartList.add(carts[i]);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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



}
