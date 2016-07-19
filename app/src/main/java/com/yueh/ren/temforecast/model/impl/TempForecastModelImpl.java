package com.yueh.ren.temforecast.model.impl;


import com.yueh.ren.temforecast.model.ITempForecastModel;
import com.yueh.ren.temforecast.util.TempEvent;
import com.yueh.ren.temforecast.view.TempChart;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 任跃华 on 2016/3/30 0030.
 */
public class TempForecastModelImpl implements ITempForecastModel {

    protected EventBus bus;

    public TempForecastModelImpl() {
        bus = EventBus.getDefault();
    }

    @Override
    public void getTempData() {
        Long hisEndTime = System.currentTimeMillis();
        Long hisStartTime = hisEndTime - TempChart.getTimeGap() * 12;
        Long foreStartTime = hisEndTime;
        Long foreEndTimes = hisEndTime + TempChart.getTimeGap() * 12;

        TempChart tempChart = new TempChart(hisStartTime,hisEndTime,foreStartTime,foreEndTimes);
        TempEvent event = new TempEvent();
        event.dataSet = tempChart.getDateDataset();
        event.renderer = tempChart.getRenderer();
        event.what = TempEvent.OK;
        bus.post(event);
    }

}
