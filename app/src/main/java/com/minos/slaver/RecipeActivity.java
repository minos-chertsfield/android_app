package com.minos.slaver;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class RecipeActivity extends BaseActivity {



    private LinkedList<TextView> textViewLinkedList;       //文本集合
    private LinkedList<ImageView> imageViewLinkedList;    //图片集合
    private LinkedList<EditText> editTextLinkedList;    //输入框集合
    int num;

    final int CHOOSE_PICTURE = 0;     //选择照片功能编码
    final int TAKE_PHOTO = 1;      //拍照编码
    private Uri imageUri;     //图片地址
    ImageView temp_imageView;     //临时图片存储框

    ImageView temp_imageView2;

    Bitmap bitmap;
    Bitmap bitmap1;
    Bitmap bitmap_cover;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();//将Bitmap转成Byte[]
    String img_base64;
    String img_base64_1;
    String cover64;


    String recipe_content;    //编辑的菜谱内容
    ImageView cover;
    EditText recipe_name;
    EditText ingredients;
    WebView preview;
    Button recipe_finish;      //确定按钮
    Button preview_btn;
    Button add_Step;
    Button remove_Step;
    EditText step1;   //步骤1的内容
    ImageView step1_img;    //步骤1的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

/**
 *
 */
        textViewLinkedList = new LinkedList<TextView>();     //列表初始化
        imageViewLinkedList = new LinkedList<ImageView>();
        editTextLinkedList = new LinkedList<EditText>();
/**
 *
 */

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cover = (ImageView) findViewById(R.id.cover);      //菜谱封面




        recipe_name = (EditText) findViewById(R.id.recipe_name);    //菜谱名称

        ingredients = (EditText) findViewById(R.id.ingredients);    //菜谱的用料

        preview = (WebView) findViewById(R.id.preview);    //预览界面

        preview_btn = (Button) findViewById(R.id.preview_btn);    //预览按钮

        add_Step = (Button) findViewById(R.id.add_step);     //增加步骤按钮

        remove_Step = (Button) findViewById(R.id.remove_step);    //移除步骤


        recipe_finish = (Button) findViewById(R.id.recipe_finish);    //确定按钮


        step1 = (EditText) findViewById(R.id.step1);    //步骤1
        step1_img = (ImageView) findViewById(R.id.step1_img);   //步骤1的图片

        step1_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_imageView = step1_img;
                showChoosePicDialog();
                bitmap_cover = ((BitmapDrawable) cover.getDrawable()).getBitmap();   //获取位图文件
            }
        });


        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //封面
                temp_imageView = cover;
                showChoosePicDialog();
            }
        });
//        preview_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                String html_name = "<h1 style=\"text-align:center;\"><b>"+recipe_name.getText().toString()+"</b></h1> ";     //获取标题
//                String html_ingredients = "<p><b>用料：</b></p><p>"+ingredients.getText().toString()+"</p>";    //获取用料
//                String steps = "<p><b>步骤1: </b></p>" + "<p>" + step1.getText().toString() + "</p>";
//                for (int i=0;i<editTextLinkedList.size();i++) {
//                    bitmap = ((BitmapDrawable) imageViewLinkedList.get(i).getDrawable()).getBitmap();   //
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
//                    img_base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String
//                    System.out.println(img_base64);
//
//                    steps += "<p><b>步骤"+ (i+2) + "：</b></p>"+"<p><img src=\"data:image/png;base64,"+img_base64+"\"/></p>"+"<p>" + editTextLinkedList.get(i).getText().toString() + "</p>";
//
//
//                }
//
//                System.out.println(steps);
//
//                preview.getSettings().setDefaultTextEncodingName("utf-8");
//
//                preview.loadDataWithBaseURL(null,html_name + html_ingredients + steps,"text/html", "utf-8", null);
//
//
//            }
//        });


        recipe_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap1 = ((BitmapDrawable) step1_img.getDrawable()).getBitmap();   //获取位图文件
                bitmap1.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
                img_base64_1 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String


//                bitmap_cover = ((BitmapDrawable) cover.getDrawable()).getBitmap();   //获取位图文件
                bitmap_cover.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
                cover64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String

                String fit_screen = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>";
                String html_name = "<h1 style=\"text-align:center;\"><b>"+recipe_name.getText().toString()+"</b></h1> ";     //获取标题
                String html_ingredients = "<p><b>用料：</b></p><p>"+ingredients.getText().toString()+"</p>";    //获取用料
                String steps = "<p><b>步骤1: </b></p>" + "<p><img width=\"100%\" height=\"40%\" src=\"data:image/png;base64,"+ img_base64_1 +"\"/></p>" + "<p>" + step1.getText().toString() + "</p>";     //步骤1的开始


                for (int i=0;i<editTextLinkedList.size();i++) {
                    bitmap = ((BitmapDrawable) imageViewLinkedList.get(i).getDrawable()).getBitmap();   //
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos2);//压缩
                    img_base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String
                    System.out.println(img_base64);

                    steps += "<p><b>步骤"+ (i+2) + "：</b></p>"+"<p><img width=\"100%\" height=\"40%\" src=\"data:image/png;base64,"+img_base64+"\"/></p>"+"<p>" + editTextLinkedList.get(i).getText().toString() + "</p>";


                }


                System.out.println(steps);

                recipe_content = fit_screen + html_name + html_ingredients + steps;

//                preview.getSettings().setDefaultTextEncodingName("utf-8");
//
//                preview.loadDataWithBaseURL(null,recipe_content,"text/html", "utf-8", null);

                provide(recipe_content,recipe_name.getText().toString(),cover64);      //上传当前编辑页面
                System.out.println("cover64"+cover64);
            }
        });


        add_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStep();
            }
        });

        remove_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, RecipeContentActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(RecipeActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




//    /**
////     * 弹出对话框
////     */
////    private void showInputDialog() {
////        /*@setView 装入一个EditView
////         */
////        final EditText editText = new EditText(RecipeActivity.this);
////        editText.setLines(5);
////        final AlertDialog.Builder inputDialog =
////                new AlertDialog.Builder(RecipeActivity.this);
////        editText.setHint("编辑您的菜谱");
////        inputDialog.setTitle("编辑菜谱").setView(editText);
////        inputDialog.setPositiveButton("上传",
////                new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////
////                    }
////                }).show();
////
////
////    }

    private void addStep() {      //添加步骤
        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);    //定位父布局





        LinearLayout linearLayout_Step = new LinearLayout(this);
        linearLayout_Step.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout_Step.setLayoutParams(params);

        TextView textView = new TextView(this);    //步骤标题
        LinearLayout.LayoutParams text_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(text_param);
        textView.setText("步骤");
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextSize(18);
        num = textViewLinkedList.size() + 2;
        textView.append(String.valueOf(num) + "：");

        final ImageView imageView = new ImageView(this);   //步骤图设计
        LinearLayout.LayoutParams image_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp_Convert_px(300));
        imageView.setLayoutParams(image_param);
        imageView.setImageResource(R.drawable.add_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_imageView = imageView;    //替换变量
                showChoosePicDialog();
            }
        });

        EditText editText = new EditText(this);    //对应的步骤说明
        LinearLayout.LayoutParams edit_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(edit_param);    //布局尺寸填充
        editText.setMinLines(1);
        editText.setMaxLines(5);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        linearLayout_Step.addView(textView);
        textViewLinkedList.add(textView);      //将文本控件添加至列表

        linearLayout_Step.addView(imageView);
        imageViewLinkedList.add(imageView);    //将图片控件添加至对应列表

        linearLayout_Step.addView(editText);
        editTextLinkedList.add(editText);      //将输入框控件添加至列表

        linear.addView(linearLayout_Step);   //添加至整体布局上
    }


    private void removeStep() {     //删除步骤
        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);    //定位父布局

    }



    private int dp_Convert_px(int dp) {    //将组件的单位dp转化为对应的像素
        int result = (int) (dp * this.getResources().getDisplayMetrics().density+0.5f);
        return result;
    }



    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加图片");
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
                        openAlbumIntent.putExtra("1","imageView");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PHOTO: // 拍照
                        System.out.println("拍照启动1");
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if(outputImage.exists() ) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(RecipeActivity.this,
                                    "com.minos.slaver.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {   //活动结果回调
        switch (requestCode) {
            case TAKE_PHOTO:
                if( resultCode == RESULT_OK ) {
                    System.out.println("拍照启动2");
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Glide.with(this).load(bitmap).into(temp_imageView);
//                        cover.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PICTURE:
                System.out.println("选择相片2");
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String image = data.getStringExtra("1");
                    System.out.println(image);
                    String imagePath = null;
                    if (DocumentsContract.isDocumentUri(this, uri)) {
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
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        System.out.println("path="+path);
        return path;
    }


    private void displayImage(String imagePath) {    //显示图片
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Glide.with(this).load(bitmap).into(temp_imageView);   //加载图片并显示
        } else {
            return;
        }
    }


    private void provide(String recipe,String title, String cover) {


        Gson gson = new Gson();    //创建Gson对象

        String mac;

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        User  user = new Gson().fromJson(sharedPreferences.getString("User",""), User.class);
        mac = user.getMac();



        Recipe r = new Recipe();
        r.setMac(mac);
        r.setTitle(title);
        r.setCover(cover);
        r.setRecipe(recipe);
        String json = new Gson().toJson(r);

        String encoding = "UTF-8";
        try {
            byte[] data = json.getBytes(encoding);

            System.out.println(data);
            ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
            URL url = new URL(confurl.url_recipe01);    //实例化为url对象, 请求充值服务，返回所有数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();    //使用Http协议打开对应的url
            conn.setRequestMethod("POST");   //以post的方式发送请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setConnectTimeout(10 * 1000);   //设置连接超时时间
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
                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();

            }
            else{    //返回列表

                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
