package com.yueh.ren.temforecast.app;

import com.yueh.ren.temforecast.model.Collector;
import com.yueh.ren.temforecast.model.IDataManager;
import com.yueh.ren.temforecast.model.IForecastAlgo;
import com.yueh.ren.temforecast.util.Constants;
import com.yueh.ren.temforecast.util.SPUtil;

/**
 * @author wwhhff11
 * @author lixin
 * @author chge
 * @author yueh
 *
 * 2016年6月13日12:19:50 yueh  采用反射替换了大量switch case
 */

public class TFConfiguration {
    private static Double mTempWarnLine = null;
    private static Collector mCollector = null;
    private static IForecastAlgo mAlgo = null;
    private static IDataManager mDataManager = null;


    public static Double getTempWarnLine() {
        if (mTempWarnLine != null) {
            return mTempWarnLine;
        }
        String warnLineStr = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.TEMP_WARN_LINE);
        if (warnLineStr.isEmpty()) {
            mTempWarnLine = Double.valueOf(40);
            SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.TEMP_WARN_LINE, mTempWarnLine.toString());
            return mTempWarnLine;
        } else {
            mTempWarnLine = Double.valueOf(warnLineStr);
            return mTempWarnLine;
        }
    }

    public static void setTempWarnLine(Double temp) {
        if (temp != null) {
            mTempWarnLine = temp;
            SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.TEMP_WARN_LINE, temp.toString());
        }
    }

    public static Collector getCollector()  {
        if (mCollector != null) {
            return mCollector;
        }
        String className = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.COLLECTOR);
        if (className.isEmpty()) {
            className = Constants.COLLECTOR_MYQX;
            SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.COLLECTOR, Constants.COLLECTOR_MYQX);
        }

        try {
            Class<?> objClass = Class.forName(className);
            mCollector = (Collector)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCollector;
    }

    public static void setCollector(String className) {

        try {
            Class<?> objClass = Class.forName(className);
            mCollector = (Collector)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.COLLECTOR, className);
    }

    public static IForecastAlgo getAlgo() {
        if (mAlgo != null) {
            return mAlgo;
        }
        String className = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.FORECAST_ALGO);
        if (className.isEmpty()) {
            className = Constants.FORECAST_ALGO_LEASTSQUARE;
            SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.FORECAST_ALGO, Constants.FORECAST_ALGO_LEASTSQUARE);
        }

        try {
            Class<?> objClass = Class.forName(className);
            mAlgo = (IForecastAlgo)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mAlgo;
    }

    public static void setAlgo(String className)  {

        try {
            Class<?> objClass = Class.forName(className);
            mAlgo = (IForecastAlgo)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.FORECAST_ALGO, className);
    }

    public static IDataManager getDataManager() {
        if (mDataManager != null) {
            return mDataManager;
        }
        String className = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.DATA_MANAGER);
        if (className.isEmpty()) {
            className = Constants.DATA_MANAGER_SQLITE;
            SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.DATA_MANAGER, Constants.DATA_MANAGER_SQLITE);
        }

        try {
            Class<?> objClass = Class.forName(className);
            mDataManager = (IDataManager)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mDataManager;
    }

    public static void setDataManager(String className) {

        try {
            Class<?> objClass = Class.forName(className);
            mDataManager = (IDataManager)objClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.DATA_MANAGER, className);
    }
}
