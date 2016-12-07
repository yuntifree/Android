package com.yunxingzh.wireless.config;


import com.yunxingzh.wireless.BuildConfig;

/**
 * 作者: andy
 * 时间: 16-6-8
 * 描述:
 */
public class AppConfig {
    /**
     * 开发者模式
     */
    public static final boolean DEV_MODEL = BuildConfig.DEBUG;
    /**
     * 测试环境
     */
    public static final String DEV_URL = "http://120.76.236.185/";

    /**
     * 正式环境
     * https://api.youcaitv.cn
     */
    public static final String API_URL = "http://api.yunxingzh.com/";

    /**
     * App 内部版本号
     */
    public static final int VERSION = 1;

}
