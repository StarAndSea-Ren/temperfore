package com.yueh.ren.temforecast.model;

import com.yueh.ren.temforecast.model.database.Temper;
import com.yueh.ren.temforecast.model.entity.TemperBean;

import java.util.ArrayList;

/**
 * 温度存取操作接口
 * Created by chge on 2016/4/17 0017.
 */
public interface IDataManager {

    /**
     * 温度无变化则不存储.
     *
     * @param temperBean
     */
    void smartSaveTemper(TemperBean temperBean);

    /**
     * 储存数据
     *
     * @param temper 本地数据类型
     */
    void saveTemper(Temper temper);

    /**
     * 储存数据
     *
     * @param temperBean 远端数据类型
     */
    void saveTemper(TemperBean temperBean);

    /**
     * 储存一系列数据
     *
     * @param tempers
     */
    void saveTempers(ArrayList<Temper> tempers);

    /**
     * 删除数据
     *
     * @param startTimeMillis 起始时间
     * @param endTimeMillis   结束时间
     */
    void deleteTemper(Long startTimeMillis, Long endTimeMillis);

    /**
     * 获取温度记录数据
     *
     * @param startTimeMillis 起始时间（ms）
     * @param endTimeMillis   结束时间（ms）
     * @param gapMillis       间隔（ms）
     * @return
     */
    ArrayList<Temper> getTempers(Long startTimeMillis, Long endTimeMillis, Long gapMillis);

}
