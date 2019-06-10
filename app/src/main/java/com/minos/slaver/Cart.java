package com.minos.slaver;

public class Cart {
    private int tId;       //购物车记录编号
    private String cId;       //商品编号
    private String cName;     //商品名称
    private String cImg;     //商品图片
    private double price;     //商品单价
    private int num;      //该种商品的购买量
    private String username;    //当前用户用户名
    private String mac;     //当前用户硬件地址




    public Cart() {
        super();
    }




    public Cart(int tId, String cId, String cName, String cImg, double price, int num, String username, String mac) {
        super();
        this.tId = tId;
        this.cId = cId;
        this.cName = cName;
        this.cImg = cImg;
        this.price = price;
        this.num = num;
        this.username = username;
        this.mac = mac;
    }




    public int gettId() {
        return tId;
    }




    public void settId(int tId) {
        this.tId = tId;
    }




    public String getcId() {
        return cId;
    }




    public void setcId(String cId) {
        this.cId = cId;
    }




    public String getcName() {
        return cName;
    }




    public void setcName(String cName) {
        this.cName = cName;
    }




    public String getcImg() {
        return cImg;
    }




    public void setcImg(String cImg) {
        this.cImg = cImg;
    }




    public double getPrice() {
        return price;
    }




    public void setPrice(double price) {
        this.price = price;
    }




    public int getNum() {
        return num;
    }




    public void setNum(int num) {
        this.num = num;
    }




    public String getUsername() {
        return username;
    }




    public void setUsername(String username) {
        this.username = username;
    }




    public String getMac() {
        return mac;
    }




    public void setMac(String mac) {
        this.mac = mac;
    }
}
