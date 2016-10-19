package com.xiao.android.coolweather.util;

/**
 * Created by xiongdi on 2016/10/18.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
}
