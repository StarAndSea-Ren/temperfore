package com.yueh.ren.temforecast.model;


import com.yueh.ren.temforecast.model.database.Temper;

import java.util.ArrayList;

/**
 * 预测温度算法接口
 * Created by lixin on 2016/4/17 0017.
 */
public interface IForecastAlgo {
    /**
     * 预测温度
     *
     * @param oldTempers 过去24小时温度数据
     * @param startTime  预测开始时间
     * @param gap        间隔，默认毫秒
     * @param times      次数
     * @return 将来小时预测温度数据
     */
    ArrayList<Temper> forecastTemper(ArrayList<Temper> oldTempers,
                                     Long startTime, Long gap, int times);

    /**
     * @param oldTempers 过去温度数据
     * @param targetTime 时间
     * @return
     */
    double forecastTemper(ArrayList<Temper> oldTempers, Long targetTime);
}
