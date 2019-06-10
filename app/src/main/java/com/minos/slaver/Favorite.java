package com.minos.slaver;

public class Favorite {
    private int favoritesId;     //收藏编号
    private String cLink;    //课程链接
    private int rno;     //菜谱编号
    private String username;     //用户名
    private String mac;    //硬件地址
    private String time;     //收藏时间

    public Favorite(int favoritesId, String cLink, int rno, String username, String mac, String time) {
        super();
        this.favoritesId = favoritesId;
        this.cLink = cLink;
        this.rno = rno;
        this.username = username;
        this.mac = mac;
        this.time = time;
    }

    public Favorite() {
        super();
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public String getcLink() {
        return cLink;
    }

    public void setcLink(String cLink) {
        this.cLink = cLink;
    }

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}