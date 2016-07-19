package com.yueh.ren.temforecast.model.entity;

/**
 * @author wwhhff11 on 2016/4/6 0006.
 */
public class TemperBean {

    private String mTime;
    private double mTemperature;

    public TemperBean(){
    }

    public TemperBean(String mTime, double mTemperature) {
        this.mTime = mTime;
        this.mTemperature = mTemperature;
    }

    public double getmTempreature() {
        return mTemperature;
    }

    public void setmTempreature(double mTempreature) {
        this.mTemperature = mTempreature;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
