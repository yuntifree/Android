package wireless.libs.network.request;

import com.alibaba.fastjson.JSONArray;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.JsonUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.resp.BaseResult;
import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.FindList;
import wireless.libs.bean.resp.JokeList;
import wireless.libs.bean.resp.LiveList;
import wireless.libs.bean.resp.MenuList;
import wireless.libs.bean.resp.NickNameList;
import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.AutoLoginVo;
import wireless.libs.bean.vo.ImageTokenVo;
import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UpdateVo;
import wireless.libs.bean.vo.User;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.resp.WifiList;
import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.network.ErrorType;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.HttpParams;
import wireless.libs.network.HttpUtils;

/**
 * 网络请求统一这里来管理  在方法的末尾一定要指定HttpHandler默认的泛型类型 HttpHandler<T> T缺省 默认为object  也适用于不需要data返回值的接口
 *
 * @author jerry
 * @date 2016/03/21
 * <p>
 * 修订: 2016/06/30 carlos
 * 1. 添加search()方法的注释
 */
public class NetWorkWarpper {

    /***
     * 获取应用首页广告
     */
    public static void getAdvert(HttpHandler<AdvertVo> handler) {
        String path = "get_flash_ad";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 注册接口
     * @param code        md5 加密
     */
    public static void register(String phone, String code, HttpHandler<User> httpHandler) {
        String path = "register";
        HttpParams httpParams = new HttpParams();
        httpParams.add("username", phone);
        httpParams.add("password", StringUtils.getMD5(code));
        httpParams.add("model", AppUtils.getPhoneModel());
        httpParams.add("channel", AppUtils.getChannelName(MainApplication.get()));
        httpParams.add("udid", MainApplication.get().getMark());
        httpParams.add("code", code);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /**
     * 获取首页新闻天气
     * @param handler
     */
    public static void weatherNews(HttpHandler<WeatherNewsList> handler) {
        String path = "get_weather_news";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 获取头条顶部分类菜单
     * @param handler
     */
    public static void getMenu(HttpHandler<MenuList> handler) {
        String path = "get_menu";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 获取头条信息
     * @param httpHandler
     */
    public static void hot(int type, int seq, HttpHandler<HotInfoList> httpHandler) {
        String path = "hot";
        HttpParams httpParams = new HttpParams();
        httpParams.add("type", type);
        httpParams.add("seq", seq);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 记录点击次数
     */
    public static void clickCount(int id, int type, String name, HttpHandler<Object> handler) {
        String path = "report_click";
        HttpParams httpParams = new HttpParams();
        httpParams.add("id", id);
        httpParams.add("type", type);
        httpParams.add("name", name);
        HttpUtils.post(path, httpParams, handler);
    }

    /***
     * 获取wifi列表
     */
    public static void getWifi(double longitude, double latitude, String[] ssids, HttpHandler<WifiList> handler) {
        String path = "get_wifi_pass";
        HttpParams httpParams = new HttpParams();
        httpParams.add("longitude", longitude);
        httpParams.add("latitude", latitude);

        JSONArray array = new JSONArray();
        for (int i = 0; i < ssids.length; i++) {
            array.add(ssids[i]);
        }
        httpParams.add("ssids", array);
        HttpUtils.post(path, httpParams, handler);
    }

    /***
     * 获取地图所有热点列表
     * @param
     */
    public static void getWifiMap(HttpHandler<WifiMapList> handler) {
        String path = "get_all_aps";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * wifi公益
     * @param handler
     */
    public static void wifiSpirited(String ssid, String password, double longitude, double latitude, HttpHandler<Object> handler) {
        String path = "report_wifi";
        HttpParams httpParams = new HttpParams();
        httpParams.add("ssid",ssid);
        httpParams.add("password",password);
        httpParams.add("longitude",longitude);
        httpParams.add("latitude",latitude);
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 用户反馈
     * @param handler
     */
    public static void feedBack(String content, String contact, HttpHandler<Object> handler) {
        String path = "feedback";
        HttpParams httpParams = new HttpParams();
        httpParams.add("content",content);
        httpParams.add("contact",contact);
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 用户反馈
     */
    public static AutoLoginVo autoLogin(String privdata) {
        String path = "auto_login";
        HttpParams httpParams = new HttpParams();
        httpParams.add("privdata", privdata);
        String respBodyStr = HttpUtils.postSync(path, httpParams);
        if (respBodyStr != null) {
            try {
                BaseResult resp = JsonUtils.parseObject(respBodyStr, BaseResult.class);
                if (resp != null) {
                    if (resp.errno() == ErrorType.E_OK) {
                        //请求成功
                        //后台没有返回data类型
                        if (resp.data == null) {
                            //
                        } else {
                            AutoLoginVo data = JsonUtils.parseObject(resp.data, AutoLoginVo.class);
                            if (data != null) {
                                return data;
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    /**
     * 检查更新
     * @param handler
     */
    public static void checkUpdate(String channel, HttpHandler<UpdateVo> handler) {
        String path = "check_update";
        HttpParams httpParams = new HttpParams();
        httpParams.add("channel",channel);
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 东莞wifi--checkEnv:检查网络状况
     */
    public static int checkEnv() {
        String path = "http://apps.bdimg.com/favicon.ico";
        //String path = "http://xw.qq.com/index.html";
        //String path = "http://captive.apple.com/hotspot-detect.html";
        //String path = "http://120.76.236.185/portal?wlanacname=100&wlanuserip=lisi&ssid=100&wlanacip=100";
        return HttpUtils.getReqForDGWifi(path);
    }

    /***
     * 东莞wifi--获取验证码
     */
    public static void validateCode(String phone, HttpHandler<Object> httpHandler) {
        String path = "get_check_code";
        HttpParams httpParams = new HttpParams();
        httpParams.add("phone", phone);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 东莞wifi--连接认证
     */
    public static void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac, HttpHandler<Object> httpHandler) {
        String path = "connect_wifi";
        HttpParams httpParams = new HttpParams();
        httpParams.add("wlanacname", wlanacname);
        httpParams.add("wlanuserip", wlanuserip);
        httpParams.add("wlanacip", wlanacip);
        httpParams.add("wlanusermac", wlanusermac);
        httpParams.add("apmac", apmac);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取直播列表
     */
    public static void getLiveList(int seq, HttpHandler<LiveList> httpHandler) {
        String path = "get_live_info";
        HttpParams httpParams = new HttpParams();
        httpParams.add("seq", seq);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取头像（图片）上传的token
     */
    public static ImageTokenVo getImageToken() {
        String path = "get_image_token";
        HttpParams httpParams = new HttpParams();
        String respBodyStr = HttpUtils.postSync(path, httpParams);
        if (respBodyStr != null) {
            try {
                BaseResult resp = JsonUtils.parseObject(respBodyStr, BaseResult.class);
                if (resp != null) {
                    if (resp.errno() == ErrorType.E_OK) {
                        //请求成功
                        //后台没有返回data类型
                        if (resp.data == null) {
                            //
                        } else {
                            ImageTokenVo data = JsonUtils.parseObject(resp.data, ImageTokenVo.class);
                            if (data != null) {
                                return data;
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    /***
     * 申请上传头像（图片）
     *  "size":xxxx,  //图片大小
        "format":"xxx" //图片格式(png,jpg,gif之类)
     * */
    public static void applyImageUpload(int size, String format, HttpHandler<ImageUploadVo> httpHandler) {
        String path = "apply_image_upload";
        HttpParams httpParams = new HttpParams();
        httpParams.add("size", size);
        httpParams.add("format", format);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取用户默认头像
     * */
    public static void getDefHead(HttpHandler<DefHeadList> httpHandler) {
        String path = "get_def_head";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取我（个人中心）用户信息
     * */
    public static void getUserInfo(int tuid, HttpHandler<UserInfoVo> httpHandler) {
        String path = "get_user_info";
        HttpParams httpParams = new HttpParams();
        httpParams.add("tuid", tuid);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取段子
     * */
    public static void getJokes(int seq, HttpHandler<JokeList> httpHandler) {
        String path = "get_jokes";
        HttpParams httpParams = new HttpParams();
        httpParams.add("seq", seq);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 发现
     * */
    public static void getFind(HttpHandler<FindList> httpHandler) {
        String path = "get_discovery";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 修改用户信息
     * */
    public static void updateUserInfo(String headurl, String nickname, HttpHandler<Object> httpHandler) {
        String path = "mod_user_info";
        HttpParams httpParams = new HttpParams();
        httpParams.add("headurl", headurl);
        httpParams.add("nickname", nickname);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 获取随机昵称
     * */
    public static void getRandNick(HttpHandler<NickNameList> httpHandler) {
        String path = "get_rand_nick";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, httpHandler);
    }

}