package com.minos.slaver;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;




public class CharacteristicActivity extends BaseActivity {
  /*
  功能：个人设置页面，用户可用来查看自己的个人基本设置，同时用户可进行一些个性化的设置，形成自己独特的风格
   */
  private int THEMEID = -1;// 设置主题id

  Button skin;   //换肤

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_characteristic);


    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

//      SkinCompatManager.withoutActivity(getApplication())                         // 基础控件换肤初始化
//              .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
//              .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
//              .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//              .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
//              .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
//              .loadSkin();




    Button btn = (Button) findViewById(R.id.skin);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });


  }



  //实现返回按钮功能
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId())
    {
      case android.R.id.home:
        Intent intent = new Intent(CharacteristicActivity.this,SettingsActivity.class);
        startActivity(intent);
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }



}


