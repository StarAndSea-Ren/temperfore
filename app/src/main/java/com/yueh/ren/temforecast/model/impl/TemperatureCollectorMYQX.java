package com.yueh.ren.temforecast.model.impl;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yueh.ren.temforecast.model.Collector;
import com.yueh.ren.temforecast.model.entity.TemperBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wwhhff11 on 2016/5/10.
 * 通过绵阳气象获取温度数据
 */
public class TemperatureCollectorMYQX extends Collector {


    private final String HOST = "http://www.myqx.gov.cn/api/?mod=Weather&action=GetLastSk";
    private final OkHttpClient client = new OkHttpClient();

    private TemperBean mTemperBean = null;

    public TemperatureCollectorMYQX() {
        super();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            collectionTemper();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, getCollectGap());
            }
        }).start();
    }

    public void collectionTemper() throws Exception {
        client.networkInterceptors().add(new StethoInterceptor());//chrome调试
        Request request = new Request.Builder()
                .url(HOST)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject jsonobj = new JSONObject(response.body().string());
                    double temper = Double.valueOf(jsonobj.get("wd").toString());
                    String dateStr = "" + System.currentTimeMillis();
                    mTemperBean = new TemperBean(dateStr, temper);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public TemperBean getTemperature() {
        return mTemperBean;
    }
}
