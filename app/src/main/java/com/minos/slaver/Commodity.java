package com.minos.slaver;

import java.io.Serializable;

/**
 * 商品类
 */
public class Commodity implements Serializable {     //序列化接口

    private String cId;   //商品编号
    private String cName;   //商品名称
    private double price;     //商品价格
    private String description;   //商品描述
    private String cImg;     //商品图片


    public Commodity() {
        super();
    }

    public Commodity(String cId, String cName, double price, String description, String cImg) {    // 构造方法
        super();
        this.cId = cId;
        this.cName = cName;
        this.price = price;
        this.description = description;
        this.cImg = cImg;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getcImg() {
        return cImg;
    }

    public void setcImg(String cImg) {
        this.cImg = cImg;
    }
}
