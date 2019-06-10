package com.minos.slaver;
/*
菜谱类
 */
public class Recipe {

    private int rno;   //菜谱编号
    private String mac;    //用户的硬件地址
    private String recipe;    //用户的菜谱内容
    private String username;   //用户名
    private String head_src;   //
    private String title;    //用户的菜谱名称
    private String cover;    //用户的菜谱封面
    public Recipe() {
        super();
    }
    public Recipe(int rno, String mac, String recipe, String username, String head_src, String title, String cover) {
        super();
        this.rno = rno;
        this.mac = mac;
        this.recipe = recipe;
        this.username = username;
        this.head_src = head_src;
        this.title = title;
        this.cover = cover;
    }
    public int getRno() {
        return rno;
    }
    public void setRno(int rno) {
        this.rno = rno;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getRecipe() {
        return recipe;
    }
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getHead_src() {
        return head_src;
    }
    public void setHead_src(String head_src) {
        this.head_src = head_src;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }


}
