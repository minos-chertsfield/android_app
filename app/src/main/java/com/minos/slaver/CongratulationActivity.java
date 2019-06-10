package com.minos.slaver;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;

public class CongratulationActivity extends BaseActivity {
    /*
    功能：完成注册页面 3s重定向至登录
     */
    private TextView txt_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);

        countDownTimer.start();
        txt_skip = (TextView) findViewById(R.id.page_skip);     //获取控件

//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                //实现页面跳转
//                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0,3000);//表示延迟3秒发送任务
//    }
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3000 + 1000, 1000) {     //1s间隔
        @Override
        public void onTick(long millisUntilFinished) {       //重写计时的方法
            String value = String.valueOf((int) (millisUntilFinished  / 1000 ));        //强转int类型，获取当前倒数秒数
            txt_skip.setText("该页面将在" + value + "s后跳转");      //显示在文本上
        }

        @Override
        public void onFinish() {      //重写计时结束方法
            Intent intent = new Intent(CongratulationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }
}
