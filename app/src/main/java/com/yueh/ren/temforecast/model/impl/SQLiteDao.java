package com.yueh.ren.temforecast.model.impl;

import com.yueh.ren.temforecast.app.TFApplication;
import com.yueh.ren.temforecast.model.IDataManager;
import com.yueh.ren.temforecast.model.database.DaoSession;
import com.yueh.ren.temforecast.model.database.Temper;
import com.yueh.ren.temforecast.model.database.TemperDao;
import com.yueh.ren.temforecast.model.entity.TemperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chge on 2016/4/17 0017.
 */
public class SQLiteDao implements IDataManager {

    private DaoSession mSession = TFApplication.getInstance().getDaoSession();
    private TemperDao mDao = mSession.getTemperDao();
    private static Temper lastSavedTemper = null;

    @Override
    public void smartSaveTemper(TemperBean temperBean) {
        Temper temper = new Temper(temperBean.getmTime(), temperBean.getmTempreature());
        if (lastSavedTemper == null || lastSavedTemper.getTemperature() != temper.getTemperature()) {
            saveTemper(temper);
            lastSavedTemper = temper;
        }
    }

    @Override
    public void saveTemper(Temper temper) {
        mDao.insert(temper);
    }

    @Override
    public void saveTemper(TemperBean temperBean) {
        Temper temper = new Temper(temperBean.getmTime(), temperBean.getmTempreature());
        saveTemper(temper);
    }

    @Override
    public void saveTempers(ArrayList<Temper> tempers) {
        if (tempers == null || tempers.size() == 0) {
            return;
        }
        for (int i = 0; i < tempers.size(); i++) {
            saveTemper(tempers.get(i));
        }
    }

    @Override
    public void deleteTemper(Long startTimeMillis, Long endTimeMillis) {
        ArrayList<Temper> tempers;
        List list = mDao.queryBuilder().where(TemperDao.Properties.Time.between(startTimeMillis, endTimeMillis)).list();
        if (list.size() != 0) {
            tempers = (ArrayList) list;
            for (int i = 0; i < list.size(); i++) {
                mDao.delete(tempers.get(i));
            }
        }
    }

    /**
     * 一个间隔时间内有多个数据取第一个
     * 一个间隔时间内无数据则表示温度较上一个间隔无变化，取上一个间隔的数据
     * 本间隔无数据并且之前也无数据，则表示此时间段缺数据
     *
     * @param startTimeMillis 起始时间（ms）
     * @param endTimeMillis   结束时间（ms）
     * @param gapMillis       间隔（ms）
     * @return
     */
    @Override
    public ArrayList<Temper> getTempers(Long startTimeMillis, Long endTimeMillis, Long gapMillis) {
        ArrayList<Temper> resultTempers = new ArrayList<>();
        ArrayList<Temper> tempers;
        List list = mDao.queryBuilder().where(TemperDao.Properties.Time.between(startTimeMillis, endTimeMillis)).orderAsc(TemperDao.Properties.Time).list();
        if (list.size() != 0) {
            tempers = (ArrayList<Temper>) list;
        } else {
            return null;
        }
        long flagTime = startTimeMillis;
        long nextFlagTime = startTimeMillis + gapMillis;
        Double lastResultTemper = null;
        int i = 0;
        while (i < tempers.size()) {
            Temper temp = tempers.get(i);
            long time = Long.valueOf(temp.getTime());
            if (flagTime <= time && time < nextFlagTime) {
                //该时间段有数据
                lastResultTemper = temp.getTemperature();
                resultTempers.add(temp);
                flagTime = nextFlagTime;
                nextFlagTime += gapMillis;
                i++;
            } else if (time >= nextFlagTime) {
                if (lastResultTemper != null) {
                    //该时间段无数据则取上一个时间段数据
                    resultTempers.add(new Temper((flagTime + gapMillis / 2) + "", lastResultTemper));
                    flagTime = nextFlagTime;
                    nextFlagTime += gapMillis;
                } else {
                    //上一个时间段无数据则定位到最近的有数据的时间段
                    flagTime += ((time - flagTime) / gapMillis) * gapMillis;
                    nextFlagTime = flagTime + gapMillis;
                }
            } else {
                i++;
            }
        }
        return resultTempers;
    }
}
