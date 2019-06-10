package com.minos.slaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/*
功能：设置页面，提供用户进行自定义设置，以及一些应用消息的反馈
 */
public class SettingsActivity extends BaseActivity {

    //定义显示对话框的方法，用来在用户即将退出的时候再三确认
    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingsActivity.this);
        normalDialog.setTitle("警告");
        normalDialog.setMessage("您真的要退出吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityController.finishAll();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[] setting_items = new String[] { "个人设置", "清除缓存",
                "检查更新", "注销账户", "意见反馈", "关于我们" };   //用户的设置列表
        ListView listView_settings = (ListView) findViewById(R.id.listView_settings);    //创建ListView的对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingsActivity.this, R.layout.listview_layout, setting_items);
        //采用数组适配器在当前活动中使用用户设置列表功能项
        listView_settings.setAdapter(adapter);     //ListView组件将适配器的内容加载到组件之中
        listView_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){     //根据返回的ListView中的Item的位置进行跳转
                    case 0:
                        Intent intent_characteristic = new Intent(SettingsActivity.this, CharacteristicActivity.class);
                        startActivity(intent_characteristic);
                        finish();
                        break;
                    case 1:
                        Toast.makeText(SettingsActivity.this,"清除缓存",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(SettingsActivity.this, "当前版本是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(SettingsActivity.this, "注销账户", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(SettingsActivity.this, "意见反馈", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        showSeriviceDialog();
                        break;
                }
            }
        });


        Button btn_quit_app = (Button) findViewById(R.id.quit_app);     //退出App的按钮
        btn_quit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });

    }

    //实现返回按钮功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void showSeriviceDialog() {      //协议弹窗
        ScrollView scrollView = new ScrollView(this);
        // 背景色
        scrollView.setBackgroundResource(R.color.colorWhite);     //背景板为白色
        TextView textView = new TextView(this);
        textView.setTextSize(14);     //正文字体大小
        // 标题
        textView.setText(Html.fromHtml("" +
                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本软件尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本软件会按照本隐私权政策的规定使用和披露您的个人信息。但本软件将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，本软件不会将这些信息对外披露或向第三方提供。本软件会不时更新本隐私权政策。您在同意本软件服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本软件服务使用协议不可分割的一部分。</p>\n" +
                "<p>1.适用范围</p>\n" +
                "<p>a)在您使用本软件网络服务，本软件自动接收并记录的您的手机上的信息，包括但不限于您的健康数据、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p>\n" +
                "<p>2.信息的使用</p>\n" +
                "<p>a)在获得您的数据之后，本软件会将其上传至服务器，以生成您的排行榜数据，以便您能够更好地使用服务。</p>\n" +
                "<p>3.信息披露</p>\n" +
                "<p>a)本软件不会将您的信息披露给不受信任的第三方。</p>\n" +
                "<p>b)根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p>\n" +
                "<p>c)如您出现违反中国有关法律、法规或者相关规则的情况，需要向第三方披露；</p>\n" +
                "<p>4.信息存储和交换</p>\n" +
                "<p>本软件收集的有关您的信息和资料将保存在本软件及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或本软件收集信息和资料所在地的境外并在境外被访问、存储和展示。</p>\n" +
                "<p>5.信息安全</p>\n" +
                "<p>a)在使用本软件网络服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，请您立即联络本软件客服，以便本软件采取相应措施。</p>\n" +
                "\n"));     //设置条款正文
        // tv.setTextColor(getResources().getDrawable(R.color.blue));
        scrollView.addView(textView);    //添加文本视图至滚动面板
        // 内容
        new AlertDialog.Builder(this)
                .setTitle("关于垂涎").setView(scrollView)    //为文本设置标题
                .create().show();

    }



}
