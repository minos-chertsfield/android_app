package com.minos.slaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailsActivity extends BaseActivity {
    /**
     * 商品详细信息
     * @param savedInstanceState
     */



    /**
     * 按钮声明
     */
    Button btn_favorite;     //收藏按钮
    Button btn_pay_now;      //立即购买按钮
    Button btn_put_into_cart;    //加入购物车按钮
    private String[] info;     //商品信息数组
    int temp_num;

    String price;

    String image_uri;

    int num_to_pass;

    String my_cId;
    String mac;
    int num;

    Commodity commodity_to_pass = new Commodity();      //商品类






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);
        mac = user.getMac();
        System.out.println("mac:"+mac);


//        btn_favorite = (Button) findViewById(R.id.btn_favorites);     //收藏按钮
        btn_pay_now = (Button) findViewById(R.id.btn_paynow);     //立即购买按钮
        btn_put_into_cart = (Button) findViewById(R.id.btn_put_into_cart);     //加入购物车按钮





        Intent intent = getIntent();
        String cId = intent.getStringExtra("commodity");
        my_cId = cId;
        Toast.makeText(this, cId, Toast.LENGTH_SHORT).show();

        myrefresh(cId);










        btn_put_into_cart.setOnClickListener(new View.OnClickListener() {    //加入购物车按监听
            @Override
            public void onClick(View v) {

                showInputDialog();


            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {      //立即购买按钮监听
            @Override
            public void onClick(View v) {
            showInputDialog02();
            }
        });

//        btn_favorite.setOnClickListener(new View.OnClickListener() {   //收藏按钮监听
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(DetailsActivity.this,ShopActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void myrefresh(String cId) {    //进行数据连接


        Gson gson = new Gson();    //创建Gson对象



        String encoding = "UTF-8";
        try {
            byte[] data = cId.getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_details);    //实例化为url对象, 请求加载页面，返回所有数据
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
            if(response.equals("fail"))   //返回值为"success"
            {
                Toast.makeText(DetailsActivity.this, "失败", Toast.LENGTH_SHORT).show();





            }
            else{    //返回列表
                System.out.println(response);     //服务器端应答返回对象列表的Json字符串
                Commodity commodity = new Gson().fromJson(response, Commodity.class);
                ImageView imageView = (ImageView) findViewById(R.id.commodity_image_show);
                Glide.with(this).load(commodity.getcImg()).into(imageView);    //图片加载
                TextView text_details = (TextView) findViewById(R.id.txt_details);
                text_details.setText(commodity.getcName());


                /**
                 * 进行对象序列包设置
                 */

                commodity_to_pass.setcId(cId);
                commodity_to_pass.setcImg(commodity.getcImg());
                commodity_to_pass.setcName(commodity.getcName());
                commodity_to_pass.setDescription(commodity.getDescription());
                commodity_to_pass.setPrice(commodity.getPrice());


                System.out.println(cId + commodity.getPrice());
                /**
                 *
                 */

                System.out.println("123456789");

                price = String.valueOf(commodity.getPrice()) + "元/份";   //获取价格
                image_uri = commodity.getcImg();    //获取图片路径

                info = new String[]
                                {"描述：" + commodity.getDescription(),
                                "价格：" + String.valueOf(commodity.getPrice()) + "元/份"};
                System.out.println(commodity.getDescription() + "\n" + commodity.getPrice());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_layout_2, info);
                ListView listView = (ListView) findViewById(R.id.list_details);
                listView.setAdapter(adapter);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void put_into_cart(String cId, String mac, int num) {    //进行数据连接


        Gson gson = new Gson();    //创建Gson对象


        String encoding = "UTF-8";
        try {
            byte[] data = ("{\"cId\":"+"\""+cId+"\""+",\"mac\":"+"\""+ mac +"\""+",\"num\":"+"\""+num+"\"}").getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_cart01);    //实例化为url对象, 请求加载页面，返回所有数据
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
                Toast.makeText(DetailsActivity.this, "失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表
                Toast.makeText(this, "商品已添加至购物车，请查看", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 弹出对话框，需要输入商品数量
     */
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        LayoutInflater inflater = LayoutInflater.from(this);     //转化布局文件
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView imageView = dialog.findViewById(R.id.commodity_to_cart);   //图片
        Glide.with(this).load(image_uri).into(imageView);   //加载图片
        TextView txt_price = (TextView) dialog.findViewById(R.id.price_to_cart);    //价格
        txt_price.setText(price);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_cart_ok);   //获取确定按钮
        final TextView txt_num = (TextView) layout.findViewById(R.id.btn_num);
        temp_num = Integer.parseInt(txt_num.getText().toString());
        final Button add = (Button) layout.findViewById(R.id.num_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp_num!=10){
                    temp_num ++;
                    txt_num.setText(String.valueOf(temp_num));
                } else {
                    txt_num.setText(String.valueOf(10));
                }
            }
        });
        final Button subtract = (Button) layout.findViewById(R.id.num_subtract);
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp_num!=1){
                    temp_num--;
                    txt_num.setText(String.valueOf(temp_num));
                } else {
                    txt_num.setText(String.valueOf(1));
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(txt_num.getText().toString());    //获取商品数量

                num_to_pass = num;

                put_into_cart(my_cId, mac, num);

                System.out.println("放入：" + my_cId + mac + num);

                dialog.dismiss();
            }
        });




    }



    /**
     * 弹出对话框，选择后跳转订单
     */
    private void showInputDialog02() {
        /*@setView 装入一个EditView
         */
        LayoutInflater inflater = LayoutInflater.from(this);     //转化布局文件
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView imageView = dialog.findViewById(R.id.commodity_to_cart);   //图片
        Glide.with(this).load(image_uri).into(imageView);   //加载图片
        TextView txt_price = (TextView) dialog.findViewById(R.id.price_to_cart);    //价格
        txt_price.setText(price);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_cart_ok);   //获取确定按钮
        final TextView txt_num = (TextView) layout.findViewById(R.id.btn_num);
        temp_num = Integer.parseInt(txt_num.getText().toString());
        final Button add = (Button) layout.findViewById(R.id.num_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp_num!=10){
                    temp_num ++;
                    txt_num.setText(String.valueOf(temp_num));
                } else {
                    txt_num.setText(String.valueOf(10));
                }
            }
        });
        final Button subtract = (Button) layout.findViewById(R.id.num_subtract);
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp_num!=1){
                    temp_num--;
                    txt_num.setText(String.valueOf(temp_num));
                } else {
                    txt_num.setText(String.valueOf(1));
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(txt_num.getText().toString());    //获取商品数量


                Intent intent = new Intent(DetailsActivity.this, OrderActivity.class);
                intent.putExtra("order", commodity_to_pass);       //传递对象
                intent.putExtra("num", txt_num.getText().toString());
                System.out.println("num" +txt_num.getText().toString());
                startActivity(intent);

                dialog.dismiss();
            }
        });




    }

}
