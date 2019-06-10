package com.minos.slaver;

import android.app.Activity;
import android.content.SharedPreferences;
/*

 */
public class Mac extends Activity {
    public String GenerateRandom(SharedPreferences preferences) {



        String name = preferences.getString("slaver", "");
        if(name.equals("")) {
            int random_num = (int)((Math.random() * 9 + 1) * 100000);    //产生随机数
            //实例化SharedPreferences.Editor对象
            SharedPreferences.Editor editor = preferences.edit();

            //用putString的方法保存数据
            editor.putString("slaver", random_num + "");

            //提交数据
            editor.commit();
            return random_num + "";
        }
        return name;
    }






}


