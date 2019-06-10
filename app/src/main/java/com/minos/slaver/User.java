package com.minos.slaver;


/**
 * Created by PC on 2019/3/31.
 */

/*
用户类：对应数据库中的user表
 */

public class User {
    /*
    用户数据的声明
     */
    private String username;
    private String password;
    private String realname;
    private String mobilephone;
    private long birthday;
    private double money;
    private String head_src;
    private String mac;


    public User() {
        super();
    }

    public User(String username, String password, String realname, String mobilephone, long birthday, double money,
                String head_src, String mac) {
        super();
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.mobilephone = mobilephone;
        this.birthday = birthday;
        this.money = money;
        this.head_src = head_src;
        this.mac = mac;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRealname() {
        return realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getMobilephone() {
        return mobilephone;
    }
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }
    public long getBirthday() {
        return birthday;
    }
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getHead_src() {
        return head_src;
    }
    public void setHead_src(String head_src) {
        this.head_src = head_src;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }




}