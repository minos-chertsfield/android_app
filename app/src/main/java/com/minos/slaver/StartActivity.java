package com.minos.slaver;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/*
启动动画
 */





public class StartActivity extends AppCompatActivity {


    private TextView skip;    //跳过设置,存放文本

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_start, null);    //填充内容
        setContentView(view);

        skip = (TextView) findViewById(R.id.skip);

        countDownTimer.start();   //开始倒计时

        skip.setOnClickListener(new View.OnClickListener() {    //设置绑定事件
            @Override
            public void onClick(View v) {
            countDownTimer.cancel();     //取消倒计时
            redirectTo();     //调用重定向方法
            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);    //透明度从30%~100%渐变
        alphaAnimation.setDuration(5000);    //动画时间为5秒
        view.startAnimation(alphaAnimation);    //开始播放动画
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {    //与动画一同开始

            }

        });
    }



    private void redirectTo(){    //重定向方法
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //利用Android原生的倒计时控件
    private CountDownTimer countDownTimer = new CountDownTimer(5000 + 1000, 1000) {     //1s间隔
        @Override
        public void onTick(long millisUntilFinished) {       //重写计时的方法
            String value = String.valueOf((int) (millisUntilFinished  / 1000 ));        //强转int类型，获取当前倒数秒数
            skip.setText(value + "s | 跳过");      //显示在文本上
        }

        @Override
        public void onFinish() {      //重写计时结束方法
            return;
        }

    };





    @Override
    protected void onDestroy() {       //活动完成销毁
        super.onDestroy();
        if (countDownTimer!=null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }
}



