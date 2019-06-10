package com.minos.slaver;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
功能：添加新用户时获取用户的身份信息，比你高且将信息转存至服务器端数据库
 */
public class RegisterActivity extends BaseActivity {


    private String head_src;   //用户头像信息
    SharedPreferences preferences = null;    //声明SharedPreferences的对象

    EditText editText_username;
    EditText editText_password;
    EditText editText_realname;
    EditText editText_mobilphone;
    EditText editText_birthday;

    final int CHOOSE_PICTURE = 0;     //选择照片功能编码
    final int TAKE_PHOTO = 1;      //拍照编码
    private Uri imageUri;
    CircleImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        /*
        严格模式
         */
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /*
        严格模式
         */

        final CheckBox protocols = (CheckBox) findViewById(R.id.protocols);
//        protocols.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        TextView txt_read = (TextView) findViewById(R.id.txt_read);    //“我已阅读并同意”，普通样式
        TextView txt_the_protocols = (TextView) findViewById(R.id.the_protocols);     //“《用户服务条款》，蓝色下划线”
        txt_read.setText("我已阅读并同意");
        txt_the_protocols.setText(Html.fromHtml("<u><font color='#0000FF'>《用户服务条款》</font></u>"));    //html格式
        txt_the_protocols.setOnClickListener(new View.OnClickListener() {     //设置点击事件
            @Override
            public void onClick(View v) {
                showSeriviceDialog();     //显示协议具体内容
            }
        });



        final TextView txt_username = (TextView) findViewById(R.id.txt_username);     //用户名验证报错
        final TextView txt_password = (TextView) findViewById(R.id.txt_password);     //密码验证报错
        final TextView txt_realname = (TextView) findViewById(R.id.txt_realname);     //真实姓名报错
        final TextView txt_mobilephone = (TextView) findViewById(R.id.txt_mobilephone);    //手机号码报错

        final ImageView clear_username = (ImageView) findViewById(R.id.clear_username);
        final ImageView clear_password = (ImageView) findViewById(R.id.clear_password);
        final ImageView clear_realname = (ImageView) findViewById(R.id.clear_realname);
        final ImageView clear_mobilephone = (ImageView) findViewById(R.id.clear_mobilephone);



        //对注册界面中的控件进行获取
        picture = (CircleImageView)findViewById(R.id.user_head);   //用户头像文件

        editText_username = (EditText)findViewById(R.id.register_username);   //用户名输入框
        editText_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入文字前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入文字中的状态
            }

            @Override
            public void afterTextChanged(Editable s) {      //输入同步验证
                //输入文字中的状态
                String username = editText_username.getText().toString();   //字符串获取用户ID
                String Regx = "^[a-zA-Z0-9]{6,12}$";     //正则表达式
                Pattern pattern = Pattern.compile(Regx);
                Matcher matcher = pattern.matcher(username);
                if(!matcher.matches())
                {
                    editText_username.setBackgroundResource(R.drawable.edit_bg_error);
                    txt_username.setText("用户名格式不正确");
                    clear_username.setVisibility(View.VISIBLE);
                    clear_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText_username.setText("");
                        }
                    });
                }
                else{
                    editText_username.setBackgroundResource(R.drawable.edit_bg_ok);
                    txt_username.setText("");
                    clear_username.setVisibility(View.GONE);
                }
            }
        });
        editText_password = (EditText)findViewById(R.id.register_password);    //密码获取
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = editText_password.getText().toString();   //字符串获取用户ID
                String Regx = "^[a-zA-Z0-9]{6,12}$";     //正则表达式
                Pattern pattern = Pattern.compile(Regx);
                Matcher matcher = pattern.matcher(password);
                if(!matcher.matches())
                {
                    editText_password.setBackgroundResource(R.drawable.edit_bg_error);
                    txt_password.setText("密码格式不正确");
                    clear_password.setVisibility(View.VISIBLE);
                    clear_password.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText_password.setText("");
                        }
                    });

                }
                else{
                    editText_password.setBackgroundResource(R.drawable.edit_bg_ok);
                    txt_password.setText("");
                    clear_password.setVisibility(View.GONE);
                }
            }
        });
        editText_realname = (EditText)findViewById(R.id.register_realname);    //真实姓名获取
        editText_realname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String realname = editText_realname.getText().toString();   //字符串获取用户ID
                String Regx = "^[\u4e00-\u9fa5]{2,}$";     //正则表达式
                Pattern pattern = Pattern.compile(Regx);
                Matcher matcher = pattern.matcher(realname);
                if(!matcher.matches())
                {
                    editText_realname.setBackgroundResource(R.drawable.edit_bg_error);
                    txt_realname.setText("真实姓名格式不正确");
                    clear_realname.setVisibility(View.VISIBLE);
                    clear_realname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText_realname.setText("");
                        }
                    });
                }
                else{
                    editText_realname.setBackgroundResource(R.drawable.edit_bg_ok);
                    txt_realname.setText("");
                    clear_realname.setVisibility(View.GONE);
                }
            }
        });
        editText_mobilphone = (EditText)findViewById(R.id.register_mobilephone);  //手机号码获取
        editText_mobilphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobilephone = editText_mobilphone.getText().toString();   //字符串获取用户ID
                String Regx = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";     //正则表达式
                Pattern pattern = Pattern.compile(Regx);
                Matcher matcher = pattern.matcher(mobilephone);
                if(!matcher.matches())
                {
                    editText_mobilphone.setBackgroundResource(R.drawable.edit_bg_error);
                    txt_mobilephone.setText("手机号码格式不正确");
                    clear_mobilephone.setVisibility(View.VISIBLE);
                    clear_mobilephone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText_mobilphone.setText("");
                        }
                    });
                }
                else{
                    editText_mobilphone.setBackgroundResource(R.drawable.edit_bg_ok);
                    txt_mobilephone.setText("");
                    clear_mobilephone.setVisibility(View.GONE);
                }
            }
        });
        editText_birthday = (EditText)findViewById(R.id.register_birthday);     //出生日期获取

        Button btn_register_finished = (Button)findViewById(R.id.btn_register_finished);   //注册完成按钮获取
        Button btn_upload = (Button)findViewById(R.id.upload_head);    //上传头像按钮


        /**
         * 头像上传
         * 1.显示
         */
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();   //调用图片选择对话框,进行图片来源的选择
            }
        });

        Calendar cal;         //日历对象
        final int year,month,day;

        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);

        editText_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        editText_birthday.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(RegisterActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });

        /*
        文本规范化
         */
//        editText_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {   //焦点监听
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasWindowFocus() && editText_username.length() < 6){
//                    editText_username.setError("用户名不能小于6位");
//                }
//            }
//        });
//
//        editText_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasWindowFocus() && editText_password.length() < 8){
//                    editText_password.setError("密码不能小于8位");
//                }
//            }
//        });
//
//
//        editText_realname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasWindowFocus() && editText_realname.length() < 2){
//                    editText_realname.setError("真实姓名不得小于2位");
//                }
//            }
//        });
//
//
//        editText_mobilphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasWindowFocus() && editText_mobilphone.length() != 11){
//                    editText_mobilphone.setError("手机号码应为11位");
//                }
//            }
//        });



        /*
        注册监听
         */
        btn_register_finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (protocols.isChecked() && judge_username() && judge_password() && judge_realname() && judge_mobilephone() && judge_birthday()) {
                    Mac mac_obj = new Mac();    //建立手机硬件地址对象
                    /*
                     * 用字符串对于用户的输入文本进行获取
                     */
                    String username = editText_username.getText().toString();
                    String password = editText_password.getText().toString();
                    /**
                     * 可添加MD5加密
                     */
                    String realname = editText_realname.getText().toString();
                    String mobilephone = editText_mobilphone.getText().toString();
                    String birthday_str = editText_birthday.getText().toString();
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                    Date date_time = null;      //时间格式转换
                    try {
                        date_time = sdf.parse(birthday_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long birthday = date_time.getTime();
                    double money = 0.0;     //新用户的资金置为0

                    /**
                     * 需要获取图片路径
                     */
                    Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
                    head_src = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);//加密转换成String
                    int n = head_src.length();
                    System.out.println(head_src);   //图片压缩编码
                    /**
                     * 需要获取图片路径
                     */


                    String mac = mac_obj.GenerateRandom(preferences);
                /*
                用json进行对象封装
                 */
                    Gson gson = new Gson();    //创建Gson对象
                    User user = new User(username, password, realname, mobilephone, birthday, money, head_src, mac);
                    String json = gson.toJson(user);   //将User类对象转化为Json类型的字符串
                    /**
                     * 用作测试，打印Json字符串
                     */
                    System.out.println(json);

                    /**
                     *
                     */
                    String encoding = "UTF-8";
                    try {
                        byte[] data = json.getBytes(encoding);

                        ConfigurationUrl confurl = new ConfigurationUrl();     //创建连接配置类的对象
                        URL url = new URL(confurl.url_register);    //实例化为url对象
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
                        if(response.equals("success"))   //返回值为"success"
                        {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();   //返回值为1，显示注册成功
                        /*
                           注册成功则进行页面跳转
                        */
                            Intent intent = new Intent(RegisterActivity.this, CongratulationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{    //返回值为"fail"
                            Toast.makeText(RegisterActivity.this, "注册失败，该用户已存在！", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

               else {
                    Toast.makeText(RegisterActivity.this,"请检查是否规范填写！",Toast.LENGTH_LONG).show();
                }
                /**
                 *
                 */

            }
        });

    }


    //实现返回按钮功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 图像对话框
     */

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            imageUri = FileProvider.getUriForFile(RegisterActivity.this,
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
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
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

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            return;
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
                .setTitle("用户服务条款").setView(scrollView)    //为文本设置标题
                .create().show();

    }


    boolean judge_username() {       //判断用户名合法
        String username = editText_username.getText().toString();   //字符串获取用户ID
        String Regx = "^[a-zA-Z0-9]{6,12}$";     //正则表达式
        Pattern pattern = Pattern.compile(Regx);
        Matcher matcher = pattern.matcher(username);
        if (matcher.matches()){
            return true;
        } else {
            return false;
        }
    }

    boolean judge_password() {       //判断密码合法
        String password = editText_password.getText().toString();   //字符串获取用户ID
        String Regx = "^[a-zA-Z0-9]{6,12}$";     //正则表达式
        Pattern pattern = Pattern.compile(Regx);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()){
            return true;
        } else {
            return false;
        }
    }

    boolean judge_realname() {       //判断真名合法
        String realname = editText_realname.getText().toString();   //字符串获取用户ID
        String Regx = "^[\u4e00-\u9fa5]{2,}$";     //正则表达式
        Pattern pattern = Pattern.compile(Regx);
        Matcher matcher = pattern.matcher(realname);
        if (matcher.matches()){
            return true;
        } else {
            return false;
        }
    }

    boolean judge_mobilephone() {       //判断手机号码合法
        String mobilephone = editText_mobilphone.getText().toString();   //字符串获取用户ID
        String Regx = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";     //正则表达式
        Pattern pattern = Pattern.compile(Regx);
        Matcher matcher = pattern.matcher(mobilephone);
        if (matcher.matches()){
            return true;
        } else {
            return false;
        }
    }

    boolean judge_birthday() {       //判断生日是否为空

        if (!editText_birthday.getText().toString().equals("")){
            return true;
        } else {
            return false;
        }
    }

}
