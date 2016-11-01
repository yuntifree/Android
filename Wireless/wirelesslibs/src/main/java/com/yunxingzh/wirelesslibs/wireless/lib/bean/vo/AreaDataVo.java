package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.io.Serializable;

/**
 * 省市区数据对象
 * Created by carey on 2016/8/11 0011.
 */
public class AreaDataVo implements Serializable {
    private int pId;    //省Id
    private int cId;    //市Id
    private int aId;    //区域Id
    private String pName;   //省名称
    private String cName;   //市名称
    private String aName;   //区域名称

    public AreaDataVo(int pId, int cId, int aId, String pName, String cName, String aName) {
        this.pId = pId;
        this.cId = cId;
        this.aId = aId;
        this.pName = pName;
        this.cName = cName;
        this.aName = aName;
    }

    public void setData(int pId, int cId, int aId, String pName, String cName, String aName){
        this.pId = pId;
        this.cId = cId;
        this.aId = aId;
        this.pName = pName;
        this.cName = cName;
        this.aName = aName;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }
}
