package com.yueh.ren.temforecast.util;

/**
 * Created by chge on 2016/4/17 0017.
 */
public class Constants {
    //------数据库-----
    public static final String DB_NAME = "TF_DB";

    //------SP配置------
    public static final String CONFIG_SP_NAME = "TF_CONFIG";

    public static final String DATA_MANAGER = "IDataManager";
    public static final String DATA_MANAGER_FILE = "com.yueh.ren.temforecast.model.impl.FileDao";
    public static final String DATA_MANAGER_SQLITE = "com.yueh.ren.temforecast.model.impl.SQLiteDao";

    public static final String FORECAST_ALGO = "IForecastAlgo";
    public static final String FORECAST_ALGO_LEASTSQUARE = "com.yueh.ren.temforecast.model.impl.ForecastAlgoLeastSquare";
    public static final String FORECAST_ALGO_LINEAR = "com.yueh.ren.temforecast.model.impl.ForecastAlgoLinear";

    public static final String COLLECTOR = "Collector";
    public static final String COLLECTOR_GD = "com.yueh.ren.temforecast.model.impl.TemperatureCollectorGD";
    public static final String COLLECTOR_MYQX = "com.yueh.ren.temforecast.model.impl.TemperatureCollectorMYQX";
    public static final String COLLECTOR_CPU = "com.yueh.ren.temforecast.model.impl.TemperatureCollectorCPU";

    public static final String COLLECTOR_OBTAIN_DATA_GAP = "COLLECTOR_OBTAIN_DATA_GAP";
    public static final String DATA_MANAGER_SAVE_DATA_GAP = "DATA_MANAGER_SAVE_DATA_GAP";
    public static final String TEMP_CHART_TIME_GAP = "TEMP_CHART_TIME_GAP";
    public static final String TEMP_WARN_LINE = "TEMP_WARN_LINE";

    //------广播------
    public static final String ACTION_CONFIG_CHG = "temforecast.chg.config";
    public static final String MSG_CONFIG_CHG = "CONFIG_CHG";


}
