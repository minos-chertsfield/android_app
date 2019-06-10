package com.minos.slaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
功能：登录界面   可供用户选择登录该App，或者前往注册用户信息
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btn_register = (Button)findViewById(R.id.btn_register);
        //建立起的登录与注册按钮对应的对象
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录按钮动作*************暂定
                Intent intent_login = new Intent(LoginActivity.this, LoginOneActivity.class);
                startActivity(intent_login);
                finish();
            }
        });
        //登录按钮监听
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
                finish();  //切换至注册活动
            }
        });
        //注册按钮监听
    }
}
