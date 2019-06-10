package com.minos.slaver;

/*
功能：连接配置类
 */
public class ConfigurationUrl {

    String url = " http://116.62.23.56:80/slaver_demo2/";

    /**
     *  http://116.62.23.56:80/slaver_demo2/
     */
    /**
     * http://192.168.0.103:8080/slaver_demo2/
     */


    public String url_register;     //注册功能连接
    public String url_login;      //登录功能连接
    public String url_comment;   //社区评论功能
    public String url_loadall;    //加载所有评论
    public String url_shop_load;    //商品加载
    public String url_details;       //商品详情
    public String url_cart01;     //添加至购物车
    public String url_cart02;      //购物车列表加载
    public String url_pay;      //支付
    public String url_recharge;   // 充值
    public String url_recipe01;   //菜谱上传
    public String url_recipe02;   //菜谱加载
    public String url_recipe03;    //菜谱内容
    public String favorites01;      //收藏信息上传
    public String favorites02;      //将收藏信息加载


    public ConfigurationUrl() {
        this.url_register = url + "Androidregister";


        this.url_login = url + "Androidlogin";

        this.url_loadall = url + "AndroidLoading";


        this.url_comment = url + "AndroidCommunity";


        this.url_shop_load = url + "Shop_Service";


        this.url_details = url + "Details";

        this.url_cart01 = url + "Cart01";

        this.url_cart02 = url + "Cart02";

        this.url_pay = url + "Pay";

        this.url_recharge = url + "RechargeService";

        this.url_recipe01 = url + "Recipe_Provide";

        this.url_recipe02 = url + "Recipe_Load";

        this.url_recipe03 = url + "Recipe_Content";

        this.favorites01 = url+ "Favorite_Upload";

        this.favorites02 = url + "Favorite_Load";
    }
}
