package com.minos.slaver;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.sql.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


import static android.app.Activity.RESULT_OK;



public class Fragment_Personal extends Fragment {
/*
功能：继承自Fragment类，对个人信息对应组件进行初始化和管理
 */



    private String[] info;     //个人信息数组
    final int CHOOSE_PICTURE = 0;     //选择照片功能编码
    final int TAKE_PHOTO = 1;      //拍照编码
    final int CROP_SMALL_PICTURE = 2;    //裁剪编码
    private Uri imageUri;
//    Uri tempUri;
    CircleImageView picture;   //用户头像

    private String headImg;    //获取用户头像编码


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        return view;
    }

    @Override
    public void onStart() {
    /*
    添加组件功能逻辑
     */


        picture = (CircleImageView) getView().findViewById(R.id.id_personal_head);   //获取用户的头像

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);

        Gson gson = new Gson();
        User  user = gson.fromJson(sharedPreferences.getString("User",""), User.class);


/**
 * 第二个ListView
 */
//        more_info = new String[]{"个人收藏", "收货地址"};   //列表内容
//        final ArrayAdapter<String> adapter_more = new ArrayAdapter<String>(getActivity(), R.layout.listview_layout, more_info);
//        final ListView listView_more = (ListView) getView().findViewById(R.id.more_info_list);
//        listView_more.setAdapter(adapter_more);
//        listView_more.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch(position){
//                    case 0:
//                        Toast.makeText(getActivity(),"个人收藏",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        Toast.makeText(getActivity(),"收货地址",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });



        info = new String[]
                {"昵称：" + user.getUsername(),
                "真实姓名：" + user.getRealname(),
                "用户生日：" + new Date(user.getBirthday()).toString(),
                "手机："+ user.getMobilephone(),
                "钱包："+ user.getMoney(),
                "充值",
                "个人收藏",
                "收货地址",
                "我的购物车"};
        headImg = user.getHead_src();    //从本地信息中读出图像编码
//        byte[] bytes = Base64.decode(headImg, Base64.DEFAULT);
//        picture.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
        Glide.with(getContext()).load(headImg).into(picture);

        /*Toast.makeText(PersonalActivity.this,(new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("User", ""), User.class).getId()),Toast.LENGTH_SHORT).show();*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, info);
        ListView listView = (ListView) getView().findViewById(R.id.person_info_list);
        listView.setAdapter(adapter);
        /**
         * 为ListView进行点击事件设置
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 5:
//                        Toast.makeText(getActivity(), "充值", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getActivity(),RechargeActivity.class);
                        startActivity(intent1);
                        break;
                    case 6:
//                        Toast.makeText(getActivity(),"收藏", Toast.LENGTH_SHORT).show();   //点击收藏页面
                        Intent intent2 = new Intent(getActivity(),FavoritesActivity.class);
                        startActivity(intent2);     //页面跳转
                        break;
                    case 7:
                        Toast.makeText(getActivity(), "收货地址", Toast.LENGTH_SHORT).show();   //点击收货地址页面
                        break;
                    case 8:
                        Toast.makeText(getActivity(),"我的购物车", Toast.LENGTH_SHORT).show();   //点击购物车
                        Intent intent_cart = new Intent(getActivity(),ShoppingCartActivity.class);
                        startActivity(intent_cart);     //页面跳转
                        break;
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {   //为头像的按钮设置监听器
            @Override
            public void onClick(View v) {
                showChoosePicDialog();   //调用图片选择对话框

            }
        });

        super.onStart();
    }


    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("更换头像");
        String[] items = {"选择本地图片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        System.out.println("选择相片1");
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PHOTO: // 拍照
                        System.out.println("拍照启动1");
                        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if(outputImage.exists() ) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(getActivity(),
                                    "com.minos.slaver.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                        break;
//                        Intent openCameraIntent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        tempUri = Uri.fromFile(new File(Environment
//                                .getExternalStorageDirectory(), "image.jpg"));
//                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//                        break;
                }
            }
        });
        builder.create().show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if( resultCode == RESULT_OK ) {
                    System.out.println("拍照启动2");
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PICTURE:
                System.out.println("选择相片2");
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String imagePath = null;
                    if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id = docId.split(":")[1];
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI ,selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                            imagePath = getImagePath(contentUri, null);
                        }
                    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        imagePath = getImagePath(uri , null);
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        imagePath = uri.getPath();
                    }
                    System.out.println("imagePath="+imagePath);
                    displayImage(imagePath);
                }
                break;
            default:
                break;
        }
    }



    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        System.out.println("path="+path);
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            return;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
//    protected void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param
     */
//    protected void setImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            personal_icon.setImageBitmap(photo);
//            uploadPic(photo);
//        }
//    }


//    private void uploadPic(Bitmap bitmap) {
////         上传至服务器
////         ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
////         注意这里得到的图片已经是圆形图片了
////         bitmap是没有做个圆形处理的，但已经被裁剪了
//
//        String imagePath = Utils.savePhoto(bitmap, Environment
//                .getExternalStorageDirectory().getAbsolutePath(), String
//                .valueOf(System.currentTimeMillis()));
//        Log.e("imagePath", imagePath + "");
//        if (imagePath != null) {
//            // 拿着imagePath上传了
//            // ...
//        }
//    }


//    public static class Utils {
//
//
//        /**
//         * Save image to the SD card
//         *
//         * @param photoBitmap
//         * @param photoName
//         * @param path
//         */
//        public static String savePhoto(Bitmap photoBitmap, String path,
//                                       String photoName) {
//            String localPath = null;
//            if (android.os.Environment.getExternalStorageState().equals(
//                    android.os.Environment.MEDIA_MOUNTED)) {
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//            }
//            return photoName;
//        }


//        public static Bitmap toRoundBitmap(Bitmap bitmap, Uri tempUri) {
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            float roundPx;
//            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
//            if (width <= height) {
//                roundPx = width / 2;
//                left = 0;
//                top = 0;
//                right = width;
//                bottom = width;
//                height = width;
//                dst_left = 0;
//                dst_top = 0;
//                dst_right = width;
//                dst_bottom = width;
//            } else {
//                roundPx = height / 2;
//                float clip = (width - height) / 2;
//                left = clip;
//                right = width - clip;
//                top = 0;
//                bottom = height;
//                width = height;
//                dst_left = 0;
//                dst_top = 0;
//                dst_right = height;
//                dst_bottom = height;
//            }
//
//            return bitmap;
//
//
//        }


    }










