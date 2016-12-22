package com.yunxingzh.wireless.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by carey on 2016/5/31 0031.
 */
public class StringUtils {
    /**
     * 判断字符串是否为空 包括 null字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0 || str.equals("null"));
    }

    /***
     * 获取短信验证码的（yzm）参数
     *
     * @return
     */
    public static String getValidateCodeYzm() {
        String strMd5 = StringUtils.getMD5("wl");
        String subStr = strMd5.substring(0, 16);
        long milli = System.currentTimeMillis() / 1000L + 1;
        String milliMd5 = StringUtils.getMD5(String.valueOf(milli));
        String milliStr = milliMd5.substring(milliMd5.length() - 16, milliMd5.length());
        return subStr + milliStr;
    }

    /***
     * 获取当前的时间戳
     */
    public static long getCurrentTime() {
        return (System.currentTimeMillis() / 1000L + 1);
    }

    /**
     * 是否到达某个日期（与当前时间比）
     *
     * @param date ""表示过期
     */
    public static boolean isExpired(String date) {
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        boolean bRet = true;
        Date mDate;
        try {
            mDate = sdf.parse(date);
            bRet = mDate.before(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bRet;
    }

    /**
     * 格式化价格 返回(￥00.00)
     *
     * @param price
     * @return
     */
    public static String formatPrice(double price) {
        // 想要转换成指定国家的货币格式
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        return format.format(price);
    }

    /**
     * 定时弹出软键盘
     */
    public static void popUpKeyboard(final View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(view, 0);
                           }
                       },
                998);
    }


    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取当前时间(时：小写的“h”是12小时制，大写的“H”是24小时制)
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH");
        return sDateFormat.format(new Date());
    }

    /**
     * 格式化价格字符串 返回(￥00.00)
     *
     * @param priceStr
     * @return
     */
    public static String formatPrice(String priceStr) {
        double price = 0.0D;
        if (!isEmpty(priceStr)) {
            priceStr = Pattern.compile("[^0-9.]").matcher(priceStr).replaceAll(""); //替换所有数字小数点以外的字符
            try {
                price = Double.valueOf(priceStr);
            } catch (NumberFormatException ex) {
                LogUtils.w("formatPrice", ex.getMessage());
            }
        }
        // 想要转换成指定国家的货币格式
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        return format.format(price);
    }


    /**
     * 获取图片地址字符串中的第一张图片
     *
     * @param imagesStr
     * @return
     */
    public static String getFirstImage(String imagesStr) {
        String[] imageArray = getImages(imagesStr);
        return imageArray == null ? null : imageArray[0];
    }

    /**
     * 获取图片数组
     *
     * @param imagesStr 图片地址字符串 格式：http://xcpnet.net/xxx.png|http://xcpnet.net/yyy.png
     * @return
     */
    public static String[] getImages(String imagesStr) {
        if (isEmpty(imagesStr)) {
            return null;
        } else {
            String[] images = imagesStr.split(",");
            for (int i = 0; i < images.length; i++) {
                String img = images[i];
                if (!img.startsWith("http:")) {
                    images[i] = "http:" + img;
                }
            }
            return images;
        }
    }

    /**
     * 将时间戳转换为时间
     */
    public static String parseDate(long times) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(times * 1000));
    }

    /**
     * 验证手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("1[0-9]{10}");
        return p.matcher(phoneNumber).matches();
    }

    /**
     * MD5加密
     *
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 格式化日期显示
     */
    public static String formatDate(String mDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = null;
        try {
            d = formatter.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter.format(d);
    }

}
