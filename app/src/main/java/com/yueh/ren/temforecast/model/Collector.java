package com.yueh.ren.temforecast.model;

import com.yueh.ren.temforecast.model.entity.TemperBean;
import com.yueh.ren.temforecast.util.Constants;
import com.yueh.ren.temforecast.util.SPUtil;

/**
 * 温度传感器
 * Created by wwhhff on 2016/4/17 0017.
 */
public abstract class Collector {
    //默认采集间隔,单位:ms
    private static final Long DEFAULT_GAP = 1000L;
    private static Long collectGap = null;

    /**
     * 从传感器获取温度
     *
     * @return
     */
    public TemperBean getTemperature() {
        return null;
    }

    /**
     * 设置采集间隔，单位毫秒
     *
     * @param gapMillis
     */
    public void setCollectGap(Long gapMillis) {
        if (gapMillis == null) {
            return;
        }
        collectGap = gapMillis;
        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.COLLECTOR_OBTAIN_DATA_GAP, gapMillis.toString());
    }

    /**
     * 获取采集间隔，单位毫秒，默认1000ms
     *
     * @return 采集间隔
     */
    public Long getCollectGap() {
        if (collectGap != null) {
            return collectGap;
        }
        String gapStr = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.COLLECTOR_OBTAIN_DATA_GAP);
        if (gapStr.isEmpty()) {
            return DEFAULT_GAP;
        } else {
            collectGap = Long.valueOf(gapStr);
            return collectGap;
        }
    }
}
