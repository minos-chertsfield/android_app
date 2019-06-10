package com.minos.slaver;

/*
订单类
 */

public class Order {

    private int oId;
    private String mac;
    private String username;
    private String address;
    private String cId;
    private String cName;
    private double price;
    private int num;
    private double sum;
    private String time;


    public Order() {
        super();
    }


    public Order(int oId, String mac, String username, String address, String cId, String cName, double price, int num,
                 double sum, String time) {
        super();
        this.oId = oId;
        this.mac = mac;
        this.username = username;
        this.address = address;
        this.cId = cId;
        this.cName = cName;
        this.price = price;
        this.num = num;
        this.sum = sum;
        this.time = time;
    }


    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
