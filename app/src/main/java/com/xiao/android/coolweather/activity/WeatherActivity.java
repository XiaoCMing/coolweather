package com.xiao.android.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiao.android.coolweather.R;
import com.xiao.android.coolweather.service.AutoUpdateService;
import com.xiao.android.coolweather.util.HttpCallbackListener;
import com.xiao.android.coolweather.util.HttpUtil;
import com.xiao.android.coolweather.util.Utility;

import org.w3c.dom.Text;

/**
 * Created by xiongdi on 2016/10/19.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout mWeatherInfoLayout;
    /**
     * 用于显示城市名
     */
    private TextView mCityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView mPublishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView mWeatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView mTemp1Text;
    /**
     * 用于显示气温2
     */
    private TextView mTemp2Text;
    /**
     * 用于显示当前日期
     */
    private TextView mCurrentDateText;
    /**
     * 切换城市按钮
     */
    private Button mSwitchCity;
    /**
     * 更新天气按钮
     */
    private Button mRefreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        // 初始化各控件
        mWeatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        mCityNameText = (TextView) findViewById(R.id.city_name);
        mPublishText = (TextView) findViewById(R.id.publish_text);
        mWeatherDespText = (TextView) findViewById(R.id.weather_desp);
        mTemp1Text = (TextView) findViewById(R.id.temp1);
        mTemp2Text = (TextView) findViewById(R.id.temp2);
        mCurrentDateText = (TextView) findViewById(R.id.current_data);

        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            // 有县级代号就去查询天气
            mPublishText.setText("同步中...");
            mWeatherInfoLayout.setVisibility(View.INVISIBLE);
            mCityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            // 没有县级代号直接显示本地天气
            showWeather();
        }

        mSwitchCity = (Button) findViewById(R.id.switch_city);
        mSwitchCity.setOnClickListener(this);
        mRefreshWeather = (Button) findViewById(R.id.refresh_weather);
        mRefreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                mPublishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询县级代号所对应的天气代号
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromService(address, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromService(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromService(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    // 处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPublishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从 SharedPreferences 文件中读取存储的天气信息，并显示到界面上。
     */
    public void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mCityNameText.setText(prefs.getString("city_name", ""));
        mTemp1Text.setText(prefs.getString("temp1", ""));
        mTemp2Text.setText(prefs.getString("temp2", ""));
        mWeatherDespText.setText(prefs.getString("weather_desp", ""));
        mPublishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
        mCurrentDateText.setText(prefs.getString("current_date", ""));
        mWeatherInfoLayout.setVisibility(View.VISIBLE);
        mCityNameText.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
