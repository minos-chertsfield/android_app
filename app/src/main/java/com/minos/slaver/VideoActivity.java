package com.minos.slaver;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoActivity extends BaseActivity {

    private WebView wvBookPlay;

    private FrameLayout commentContainer;

    private FrameLayout flVideoContainer;

    Button btn_back;

    private Button btn_favorite;    //收藏按钮

    private Button btn_share;    //分享按钮

    private String url;      //页面Url

    private String time;      //收藏时间

    private String mac;    //获取设备硬件地址

    private VideoView mVideoView;

    RelativeLayout container;

    private TextView video_title;    //视频的名称

    String str_video_title;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
//        setContentView(new CustomGifView(this));

//        wvBookPlay = (WebView)findViewById(R.id.webview);
        flVideoContainer = findViewById(R.id.container_video);

        btn_favorite = (Button) findViewById(R.id.video_favorite);

        btn_share = (Button) findViewById(R.id.share);

        btn_back = (Button) findViewById(R.id.back_to);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoActivity.this, HomeActivity.class);
                intent.putExtra("back_lesson","a");
                startActivity(intent);
            }
        });

        container = (RelativeLayout) findViewById(R.id.container_R);

        video_title = (TextView) findViewById(R.id.video_title);

        str_video_title = getIntent().getStringExtra("title");

        video_title.setText(str_video_title);

        mVideoView = (VideoView) findViewById(R.id.video);
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVideoView);







//        wvBookPlay.getSettings().setJavaScriptEnabled(true);
//        wvBookPlay.getSettings().setUseWideViewPort(true);
//        wvBookPlay.getSettings().setDisplayZoomControls(true);
//        wvBookPlay.getSettings().setLoadWithOverviewMode(true);
//        wvBookPlay.getSettings().setBuiltInZoomControls(true);
//        wvBookPlay.getSettings().setAllowFileAccess(true);
//        wvBookPlay.getSettings().setSupportZoom(true);
//        wvBookPlay.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = wvBookPlay.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(wvBookPlay.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        wvBookPlay.getSettings().setPluginState(WebSettings.PluginState.ON);
//        wvBookPlay.getSettings().setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK
//        wvBookPlay.setWebChromeClient(new MyWebChromeClient());// 重写一下，有的时候可能会出现问题

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            wvBookPlay.getSettings().setMixedContentMode(wvBookPlay.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }

        Intent intent = getIntent();
        url = intent.getStringExtra("id");

        setupVideo();



        CookieManager cookieManager = CookieManager.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("android");

        cookieManager.setCookie(url, stringBuffer.toString());
        cookieManager.setAcceptCookie(true);

//        wvBookPlay.loadUrl(url);

        btn_favorite.setOnClickListener(new View.OnClickListener() {    //收藏按钮监听事件
            @Override
            public void onClick(View v) {


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                time = simpleDateFormat.format(date);    //获取当前时间

                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);    //从本地取出当前用户对象
                mac = user.getMac();    //获取硬件地址

                myFavorites(url, mac, time);    //填写对应参数并调用方法连接服务器

            }
        });



        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // 比如发送文本形式的数据内容
                // 指定发送的内容
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                // 指定发送内容的类型
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "分享至"));
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                container.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                Toast.makeText(this,"横屏",Toast.LENGTH_SHORT).show();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                container.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (250 * this.getResources().getDisplayMetrics().density+0.5f)));
                Toast.makeText(this,"竖屏",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class MyWebChromeClient extends WebChromeClient{
        WebChromeClient.CustomViewCallback mCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Log.i("ToVmp","onShowCustomView");
            fullScreen();
            wvBookPlay.setVisibility(View.GONE);
            flVideoContainer.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.VISIBLE);
            flVideoContainer.addView(view);
            mCallback = callback;
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            Log.i("ToVmp","onHideCustomView");
            fullScreen();
            wvBookPlay.setVisibility(View.VISIBLE);
            flVideoContainer.setVisibility(View.GONE);
            flVideoContainer.removeAllViews();
            super.onHideCustomView();

        }
    }

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Log.i("ToVmp","横屏");
        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            System.out.println("现在是横屏");
        }
    }

//    @Override
//    protected void onDestroy() {
//        if (wvBookPlay != null) {
//            wvBookPlay.destroy();
//        }
//        super.onDestroy();
//    }

    /**
     *
     * @param cLink 当前页的url地址
     * @param mac 用户设备硬件地址
     * @param time 用户收藏时间
     */
    private void myFavorites(String cLink, String mac, String time) {    //收藏当前页面内容

        String encoding = "UTF-8";
        try {
            byte[] data = ("{\"mac\":\""+mac+"\",\"cLink\":\""+cLink+"\",\"time\":\""+time+"\"}").getBytes(encoding);

            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.favorites01);    //创建收藏上传页面的url对象
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
                Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表
                Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     */
    private void setupVideo() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaybackVideo();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {
            Uri uri = Uri.parse(url);
            mVideoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()) {
            mVideoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.canPause()) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
