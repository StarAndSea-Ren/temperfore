package com.yueh.ren.temforecast.model.impl;

import com.yueh.ren.temforecast.model.IForecastAlgo;
import com.yueh.ren.temforecast.model.database.Temper;

import java.util.ArrayList;

/**
 * Created by lixin on 2016/4/18.
 * 最小二乘法
 */
public class ForecastAlgoLeastSquare implements IForecastAlgo {

    private double a = 0.0;
    private double b = 0.0;

    private void init(ArrayList<Temper> Tempers) {
        double t1 = 0, t2 = 0, t3 = 0, t4 = 0;
        for (int i = 0, len = Tempers.size(); i < len; i++) {
            Temper Temper = Tempers.get(i);
            double x = Double.valueOf(Temper.getTime().toString());
            double y = Temper.getTemperature();
            t1 += Math.pow(x, 2);
            t2 += x;
            t3 += x * y;
            t4 += y;
        }
        int n = Tempers.size();
        this.a = (t3 * n - t2 * t4) * 1.0 / (t1 * n - Math.pow(t2, 2));
        this.b = (t1 * t4 - t2 * t3) * 1.0 / (t1 * n - Math.pow(t2, 2));
    }

    @Override
    public ArrayList<Temper> forecastTemper(ArrayList<Temper> oldTempers,
                                            Long startTime, Long gap, int times) {
        init(oldTempers);
        ArrayList<Temper> res = new ArrayList<>();
        Long nextTime = startTime + gap;
        for (int i = 0; i < times; i++) {
            double temp = a * (nextTime) + b;
            Temper Temper = new Temper();
            Temper.setTemperature(temp);
            Temper.setTime(nextTime + "");
            res.add(Temper);
            nextTime = nextTime + gap;
        }
        return res;
    }

    @Override
    public double forecastTemper(ArrayList<Temper> oldTempers, Long targetTime) {
        init(oldTempers);
        return a * targetTime + b;
    }
}
