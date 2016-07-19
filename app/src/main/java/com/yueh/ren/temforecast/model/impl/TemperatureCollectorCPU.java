package com.yueh.ren.temforecast.model.impl;

import com.yueh.ren.temforecast.model.Collector;
import com.yueh.ren.temforecast.model.entity.TemperBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yueh on 2016/6/13.
 */
public class TemperatureCollectorCPU extends Collector {

    private final String CMD = "cat /sys/class/thermal/thermal_zone0/temp";
    private TemperBean mTemper = null;

    public TemperatureCollectorCPU() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            getCPUTemp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, getCollectGap());
            }
        }).start();
    }

    @Override
    public TemperBean getTemperature() {
        return mTemper;
    }

    private void getCPUTemp() {
        String s = "";
        try {
            Process p = Runtime.getRuntime().exec(CMD);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Double temp = Double.valueOf(s);
        String time = System.currentTimeMillis() + "";
        if (temp != null) {
            mTemper = new TemperBean(time,temp);
        }
    }
}
