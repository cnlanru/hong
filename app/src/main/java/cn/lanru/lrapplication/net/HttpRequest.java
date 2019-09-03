package cn.lanru.lrapplication.net;

import android.graphics.Bitmap;

import cn.lanru.lrapplication.Constant;
import cn.lanru.lrapplication.bean.NewCategory;
import cn.lanru.lrapplication.bean.UserInfo;

/**
 * 描述：所有的请求接口
 */
public class HttpRequest {

  //获取商品详情
  public static void getGoodDetial(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "good/detail", params, callback, null);
  }

  //商品分类
  public static void getGoods(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "good/index", params, callback, null);
  }

  //商品分类
  public static void getGoodCategory(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "good/category", params, callback, null);
  }

  //会员签到
  public static void postSignin(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "signin/index", params, callback, null);
  }

  //判断是否可签到
  public static void getSigninCheck(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "signin/check", params, callback, null);
  }

  //获取我的优惠券
  public static void getUserExchange(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "coupon/index", params, callback, null);
  }

  //兑换优惠券
  public static void postExchange(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "coupon/exchange", params, callback, null);
  }

  //更新用户代理
  public static void postUserAgent(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "agent/update", params, callback, null);
  }

  //获取用户代理
  public static void getUserAgent(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "agent/index", params, callback, null);
  }

  //更新用户卡
  public static void postUserCard(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "card/update", params, callback, null);
  }

  //获取用户卡
  public static void getUserCard(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "card/index", params, callback, null);
  }

  //更新用户资料
  public static void postUserInfo(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "user/update", params, callback, null);
  }

  //选择年级
  public static void getChoosingGrade(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "index/ChoosingGrade", params, callback, null);
  }

  //课件分类信息
  public static void getClassCategoryInfo(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "school/categoryInfo", params, callback, null);
  }

  //我的体验课
  public static void getExperience(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "experience/my", params, callback, null);
  }

  //课件分类
  public static void getClassCategory(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "school/category", params, callback, null);
  }

  //获取课件数据
  public static void getClasses(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "school/index", params, callback, null);
  }

  //获取新闻顶
  public static void getNewDig(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "news/dig", params, callback, null);
  }

  //获取新闻内容
  public static void getNewShow(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "news/show", params, callback, null);
  }

  //获取新闻列表
  public static void getNews(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "news/page", params, callback, null);
  }

  //获取新闻分类
  public static void getNewCategory(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "news/index", params, callback, null);
  }

  //获取验证码
  public static void getMobileCode(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "sms/send", params, callback, null);
  }

  //更新Token
  public static void getToken(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "token/check", params, callback, null);
  }

  //手机登录
  public static void postMobileLogin(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "user/mobilelogin", params, callback, UserInfo.class);
  }

  //手机登录
  public static void postNameLogin(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "user/login", params, callback, UserInfo.class);
  }

  //注册
  public static void postRegister(RequestParams params, ResponseCallback callback) {
    RequestMode.postRequest(Constant.URL + "user/register", params, callback, UserInfo.class);
  }

  //获取分类
  public static void getGrade(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "index/grade", params, callback, null);
  }

  //更新会员级别
  public static void postLevel(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "user/level", params, callback, null);
  }

  //获取欢迎图片
  public static void getAd(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "index/ad", params, callback, null);
  }

  //加载网络图片
  public static Bitmap getImgApi(RequestParams params, String imgPath) {
    return RequestMode.getLoadImage(imgPath,params);
  }

  //退出
  public static void getLogOut(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "user/logout", params, callback, null);
  }

  //获取用户信息
  public static void getUserInfo(RequestParams params, ResponseCallback callback) {
    RequestMode.getRequest(Constant.URL + "user/value", params, callback, null);
  }

}
