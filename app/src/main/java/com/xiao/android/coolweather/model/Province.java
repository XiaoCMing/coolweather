package com.xiao.android.coolweather.model;

/**
 * Created by xiongdi on 2016/10/18.
 */
public class Province {

    private int mId;
    private String mProvinceName;
    private String mProvinceCode;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        mProvinceCode = provinceCode;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    public void setProvinceName(String provinceName) {
        mProvinceName = provinceName;
    }
}
