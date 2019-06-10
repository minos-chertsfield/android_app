package com.minos.slaver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TimeZone;


public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


//    final int CHOOSE_PICTURE = 0;     //选择照片功能编码
//    final int TAKE_PHOTO = 1;      //拍照编码
//    private Uri imageUri;
    //记录Fragment的位置
//    private int position = 0;
    private String[] info;     //个人信息数组

    BottomNavigationView navigation;

    ListView queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));


        info = new String[]
                {"1.三文鱼刺身" ,
                        "2.焦糖布丁" ,
                        "3.椰蓉芒果戚风" ,
                        "4.椰子百香果夏洛特",
                        "5.奶糖杏仁慕斯",
                        "6.葱油菌菇酱",
                        "7.蒜蓉粉丝娃娃菜",
                        "8.菠萝虾仁炒饭",
                        "9.桃胶红枣桂圆椰奶木瓜羹"};
        queue = (ListView) findViewById(R.id.queue);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, info);

        queue.setAdapter(adapter);

        Intent intent = getIntent();    //获取发送出的信息
        String page_send = intent.getStringExtra("favorites");
        String page_back = getIntent().getStringExtra("back_lesson");
        init();
        if (page_send!=null) {      //如果发送信息非空，则转向个人信息页面
            replaceFragment(new Fragment_Personal());
            navigation.setSelectedItemId(R.id.navigation_personal);   //回到个人信息页面
        }

        if (page_back!=null) {
            replaceFragment(new Fragment_Lesson());
            navigation.setSelectedItemId(R.id.navigation_dashboard);
        }


    }


//    protected void showChoosePicDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("上传作品");
//        String[] items = {"选择本地图片", "拍照"};
//        builder.setNegativeButton("取消", null);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case CHOOSE_PICTURE: // 选择本地照片
//                        Intent openAlbumIntent = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        openAlbumIntent.setType("image/*");
//                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//                        break;
//                    case TAKE_PHOTO: // 拍照
//                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
//                        try {
//                            if(outputImage.exists() ) {
//                                outputImage.delete();
//                            }
//                            outputImage.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if(Build.VERSION.SDK_INT >= 24) {
//                            imageUri = FileProvider.getUriForFile(HomeActivity.this,
//                                    "com.minos.slaver.fileprovider", outputImage);
//                        } else {
//                            imageUri = Uri.fromFile(outputImage);
//                        }
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        startActivityForResult(intent, TAKE_PHOTO);
//                        break;
//                }
//            }
//        });
//        builder.create().show();
//    }




    private void init(){           //初始化
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        actionBar.setCustomView(actionbarLayout);

        ImageView btn_head = (ImageView) findViewById(R.id.head);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Gson gson = new Gson();
        User  user = gson.fromJson(sharedPreferences.getString("User",""), User.class);
        Glide.with(this).load(user.getHead_src()).into(btn_head);

        btn_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_Personal());
            }
        });

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);   //获取设置的按钮
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent_setting);
                finish();
            }
        });


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        ImageView upload = (ImageView)findViewById(R.id.upload_btn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showChoosePicDialog();
//                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivity(albumIntent);
                Intent intent = new Intent(HomeActivity.this,RecipeActivity.class);
                startActivity(intent);


            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        //用来替换并且同时记录碎片的方法
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_home, fragment);      //替换原来的帧布局
        transaction.addToBackStack(null);    //存储碎片至返回栈中
        transaction.commit();     //提交事务
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                replaceFragment(new Fragment_Food());

                return true;
            case R.id.navigation_dashboard:
//
                replaceFragment(new Fragment_Lesson());
                return true;
//            case R.id.navigation_notifications:
//                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivity(albumIntent);
//                return true;
            case R.id.navigation_collection:

                replaceFragment(new Fragment_Collection());
                return true;
            case R.id.navigation_personal:

                replaceFragment(new Fragment_Personal());

                return true;
        }

        return false;
    }


    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
        return dstbmp;
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if( resultCode == RESULT_OK ) {
//                    try{
//                        Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
////                        picture.setImageBitmap(bitmap);
////                        Bundle bitmap_bundle = new Bundle();
////                        Bitmap b = imageScale(bitmap, 50, 50);
////                        bitmap_bundle.putParcelable("bitmap", b);
//                        Intent intent = new Intent(this, CommentActivity.class);
////                        intent.putExtra("bitmap",bitmap_bundle);
//                        startActivity(intent);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
//    }



//    @SuppressLint("MissingSuperCall")
////    @Override
////    protected void onSaveInstanceState(Bundle outState) {
////        /* 记录当前的position */
////        outState.putInt("position", mPosition);
////    }
////
////    @Override
////    protected void onRestoreInstanceState(Bundle savedInstanceState) {
////        super.onRestoreInstanceState(savedInstanceState);
////        mPosition = savedInstanceState.getInt("position");
////        selectedFragment(mPosition);
////    }




}
