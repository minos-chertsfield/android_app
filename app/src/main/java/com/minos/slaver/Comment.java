package com.minos.slaver;

/**
 * 用户名
 */
public class Comment {
    private int cno;     //评论编号递增
    private String mac;     //用户硬件地址
    private String page;    //页面
    private String comment;    //用户评论内容
    private String username;   //用户名
    private String head_src;   //用户头像
    public Comment(int cno, String mac, String page, String comment, String username, String head_src) {
        super();
        this.cno = cno;
        this.mac = mac;
        this.page = page;
        this.comment = comment;
        this.username = username;
        this.head_src = head_src;
    }
    public int getCno() {
        return cno;
    }
    public void setCno(int cno) {
        this.cno = cno;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
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





}
