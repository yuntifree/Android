package com.yunxingzh.wireless.config;

import android.app.Application;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.AreaDataVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.DeviceUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by Carey on 2016/5/25.
 */
public class MyApplication extends Application {

    public static MyApplication sApplication;
    private boolean isExit = false;
    private String mMark;
    private String mToken;
    private UserInfoVo mUser;

    private AreaDataVo mAreaDataVo;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        OkHttpUtil.init(sApplication);
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public boolean needLogin() {
        return StringUtils.isEmpty(getToken()) || MyApplication.sApplication.getUser() == null;
    }

    public String getMark() {
        if (StringUtils.isEmpty(mMark)) {
            mMark = SPUtils.get(sApplication, Constants.SP_KEY_MARK, "");
            if (StringUtils.isEmpty(mMark.trim())) {
                mMark = DeviceUtils.createUUID(sApplication);
            }
        }
        return mMark;
    }

    public String getToken() {
        if (StringUtils.isEmpty(mToken)) {
            mToken = SPUtils.get(sApplication, Constants.SP_KEY_TOKEN, "");
//            if (StringUtils.isEmpty(mToken)) {
//                startActivity(new Intent(sApplication, LoginActivity.class));
//            }
        }
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        SPUtils.put(sApplication, Constants.SP_KEY_TOKEN, token);
    }

    public UserInfoVo getUser() {
        if (mUser == null) {
            mUser = (UserInfoVo) SPUtils.getObject(sApplication, Constants.SP_KEY_USER);
        }
        return mUser;
    }

    public void setUser(UserInfoVo mUser) {
        this.mUser = mUser;
        SPUtils.putObject(sApplication, Constants.SP_KEY_USER, mUser);
    }

    public AreaDataVo getAreaDataVo() {
        if (mAreaDataVo == null) {
            mAreaDataVo = (AreaDataVo) SPUtils.getObject(sApplication, Constants.SP_KEY_AREA);
        }
        return mAreaDataVo;
    }

    public void setAreaDataVo(int pId, int cId, int aId) {
        setAreaDataVo(pId, cId, aId, AreaUtils.getInstance(this).getProvinceNameById(pId)
                , AreaUtils.getInstance(this).getCityNameById(pId, cId), AreaUtils.getInstance(this).getAreaNameById(pId, cId, aId));
    }

    public void setAreaDataVo(int pId, int cId, int aId, String pName, String cName, String aName) {
        if (getAreaDataVo() == null) {
            this.mAreaDataVo = new AreaDataVo(pId, cId, aId, pName, cName, aName);
            SPUtils.putObject(sApplication, Constants.SP_KEY_AREA, mAreaDataVo);
        } else {
            mAreaDataVo.setData(pId, cId, aId, pName, cName, aName);
        }
    }

    public void loginOut() {
        MyApplication.sApplication.setToken("");
        MyApplication.sApplication.setUser(null);
    }
}
