package com.yunxingzh.wirelesslibs.wireless.lib.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by carey on 2016/6/26 0026.
 */
public class AreaUtils {

    private static AreaUtils mInstance;

    private List<Data> mProvinceList = new ArrayList<>();
    private List<Data> mCityList = new ArrayList<>();
    private List<Data> mAreaList = new ArrayList<>();

    private Map<String, List<Data>> mCityMap = new HashMap<>();
    private Map<String, List<Data>> mAreaMap = new HashMap<>();

    public static AreaUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AreaUtils.class) {
                if (mInstance == null) {
                    mInstance = new AreaUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private AreaUtils(Context context) {
        try {
            initData(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据省Id获取省名称
     *
     * @param provinceId
     * @return
     */
    public String getProvinceNameById(int provinceId) {
        for (Data data : mProvinceList) {
            if (provinceId == data.getId()) {
                return data.getSname();
            }
        }
        return "";
    }

    /**
     * 根据市Id获取市名称
     *
     * @param provinceId
     * @param cityId
     * @return
     */
    public String getCityNameById(int provinceId, int cityId) {
        getCityListById(provinceId);
        if(mCityList != null) {
            for (Data data : mCityList) {
                if (cityId == data.getId()) {
                    return data.getSname();
                }
            }
        }
        return "";
    }

    /**
     * 根据区域Id获取区域名称
     * @param provinceId
     * @param cityId
     * @param areaId
     * @return
     */
    public String getAreaNameById(int provinceId, int cityId, int areaId) {
        getCityListById(provinceId);
        getAreaListById(cityId);
        if(mAreaList != null) {
            for (Data data : mAreaList) {
                if (areaId == data.getId()) {
                    return data.getSname();
                }
            }
        }
        return "";
    }

    /**
     * 根据编号获取省市区名字
     *
     * @param provinceId
     * @param cityId
     * @param areaId
     * @return
     */
    public String getProvincesNameById(int provinceId, int cityId, int areaId) {
        StringBuffer provincesName = new StringBuffer();
        for (Data data : mProvinceList) {
            if (provinceId == data.getId()) {
                provincesName.append(data.getSname()).append(" ");
                break;
            }
        }

        getCityListById(provinceId);
        if(mCityList != null) {
            for (Data data : mCityList) {
                if (cityId == data.getId()) {
                    provincesName.append(data.getSname()).append(" ");
                    break;
                }
            }
        }

        getAreaListById(cityId);
        if(mAreaList != null) {
            for (Data data : mAreaList) {
                if (areaId == data.getId()) {
                    provincesName.append(data.getSname()).append(" ");
                    break;
                }
            }
        }
        return provincesName.toString();
    }

    public List<Data> getProvinceList() {
        return mProvinceList;
    }

    public List<Data> getCityListById(int provinceId) {
        mCityList = mCityMap.get(provinceId + "");
        return mCityList;
    }

    public List<Data> getAreaListById(int cityId) {
        mAreaList = mAreaMap.get(cityId + "");
        return mAreaList;
    }

    /**
     * 返回省会索引
     *
     * @param provinceId
     * @return
     */
    public int getProvinceItem(int provinceId) {
        Data data;
        for (int i = 0; i < mProvinceList.size(); i++) {
            data = mProvinceList.get(i);
            if (provinceId == data.getId()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 得到城市索引
     *
     * @param cityId
     * @return
     */
    public int getCityItem(int cityId) {
        Data data;
        for (int i = 0; i < mCityList.size(); i++) {
            data = mCityList.get(i);
            if (cityId == data.getId()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 得到地区索引
     *
     * @param areaId
     * @return
     */
    public int getAreaItem(int areaId) {
        Data data;
        for (int i = 0; i < mAreaList.size(); ++i) {
            data = mAreaList.get(i);
            if (areaId == data.getId()) {
                return i;
            }
        }
        return 0;
    }

    public Data getCurrentProvince(int index) {
        return mProvinceList.get(index);
    }

    public Data getCurrentCity(int index) {
        return mCityList.get(index);
    }

    public Data getCurrentArea(int index) {
        return mAreaList.get(index);
    }

    private JSONObject readJson(Context context) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = context.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initData(Context context) throws JSONException {
        JSONObject jsonItem;
        Data data;
        String key;
        String level;
        double longitude;
        double latitude;
        JSONArray jsonArray = readJson(context).getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonItem = jsonArray.getJSONObject(i);
            level = jsonItem.getString("level");
            data = new Data(jsonItem.getInt("id"), jsonItem.getInt("parent"), jsonItem.getString("name"), jsonItem.getString("sname"));
            if ("County".equals(level)) {
                key = data.parent + "";
                List<Data> list = this.mAreaMap.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                    mAreaMap.put(key, list);
                }
                list.add(data);
            } else if ("City".equals(level)) {
                key = data.parent + "";
                List<Data> list = this.mCityMap.get(key);
                if (list == null) {
                    list = new ArrayList<Data>();
                    this.mCityMap.put(key, list);
                }
                longitude = jsonItem.getDouble("longitude");
                latitude = jsonItem.getDouble("latitude");
                data.setLongitude(longitude);
                data.setLatitude(latitude);
                list.add(data);
            } else if ("Province".equals(level)) {
                this.mProvinceList.add(data);
            }
        }
    }

    public class Data {
        private int id;
        private int parent;
        private String name;
        private String sname;
        private double longitude;
        private double latitude;

        public Data(int id, int parent, String name, String sname) {
            this.id = id;
            this.parent = parent;
            this.name = name;
            this.sname = sname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSname() {
            if (StringUtils.isEmpty(sname)) {
                sname = name;
            }
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
