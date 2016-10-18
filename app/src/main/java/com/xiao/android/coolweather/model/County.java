package com.xiao.android.coolweather.model;

/**
 * Created by xiongdi on 2016/10/18.
 */
public class County {

    private int mId;
    private String mCountyName;
    private String mCountyCode;
    private int mCityId;

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }

    public String getCountyCode() {
        return mCountyCode;
    }

    public void setCountyCode(String countyCode) {
        mCountyCode = countyCode;
    }

    public String getCountyName() {
        return mCountyName;
    }

    public void setCountyName(String countyName) {
        mCountyName = countyName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
