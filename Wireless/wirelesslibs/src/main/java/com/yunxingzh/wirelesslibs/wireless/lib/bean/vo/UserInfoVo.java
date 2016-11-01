package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.io.Serializable;

/**
 * 用户基本信息
 *
 * @author Comsys-Carey
 * @ClassName: UserInfoVo
 * @Description:
 * @date 2016-5-24 下午1:25:52
 */
public class UserInfoVo implements Serializable {

    private String token;
    private String email;
    private String realname;
    private String phone;
    private String head_image;
    private String companyname;
    private String truetime;

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getTruetime() {
        return truetime;
    }

    public void setTruetime(String truetime) {
        this.truetime = truetime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
