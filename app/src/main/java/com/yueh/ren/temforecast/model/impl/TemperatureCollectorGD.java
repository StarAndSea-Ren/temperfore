package com.yueh.ren.temforecast.model.impl;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;
import com.yueh.ren.temforecast.app.TFApplication;
import com.yueh.ren.temforecast.model.Collector;
import com.yueh.ren.temforecast.model.entity.TemperBean;
import com.yueh.ren.temforecast.util.DateUtil;

import java.util.Date;

/**
 * Created by wwhhff11 on 2016/4/17 0017.
 * 通过高德API采集温度
 */
public class TemperatureCollectorGD extends Collector implements AMapLocalWeatherListener {

    private TemperBean mTemperBean = null;

    private LocationManagerProxy mLocationManagerProxy;
    private Context mContext = TFApplication.getInstance();

    public TemperatureCollectorGD() {
        super();
        mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
        mLocationManagerProxy.requestWeatherUpdates(
                LocationManagerProxy.WEATHER_TYPE_LIVE, this);
    }

    @Override
    public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
        double temper = Double.valueOf(aMapLocalWeatherLive.getTemperature());
        Log.e("debug", "高德数据"+temper);
        String dateStr = DateUtil.date2String(new Date());
        mTemperBean = new TemperBean(dateStr, temper);
    }

    @Override
    public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {

    }

    @Override
    public TemperBean getTemperature() {
        return mTemperBean;
    }
}
