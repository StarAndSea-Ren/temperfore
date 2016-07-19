package com.yueh.ren.temforecast.model.impl;

import com.yueh.ren.temforecast.model.IDataManager;
import com.yueh.ren.temforecast.model.database.Temper;
import com.yueh.ren.temforecast.model.entity.TemperBean;

import java.util.ArrayList;

/**
 * Created by chge on 2016/4/17 0017.
 */
public class FileDao implements IDataManager {


    @Override
    public void smartSaveTemper(TemperBean temperBean) {

    }

    @Override
    public void saveTemper(Temper temper) {

    }

    @Override
    public void saveTemper(TemperBean temperBean) {

    }

    @Override
    public void saveTempers(ArrayList<Temper> tempers) {

    }

    @Override
    public void deleteTemper(Long startTimeMillis, Long endTimeMillis) {

    }

    @Override
    public ArrayList<Temper> getTempers(Long startTimeMillis, Long endTimeMillis, Long gapMillis) {
        return null;
    }
}
